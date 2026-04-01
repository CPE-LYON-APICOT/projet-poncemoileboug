package fr.cpe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import fr.cpe.App;
import fr.cpe.model.EtatInstallation;
import fr.cpe.model.installation.IInstallation;
import fr.cpe.model.installation.decorator.EcoDecorator;
import fr.cpe.model.installation.decorator.GamerDecorator;
import fr.cpe.model.installation.decorator.LumiereDecorator;
import fr.cpe.model.installation.decorator.OlDecorator;
import fr.cpe.model.installation.decorator.VipDecorator;
import fr.cpe.model.observer.SanitaireEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

@Singleton
public class ReservationService {

    private final StockService stockService;
    private final PaymentService paymentService;
    private final UiService uiService;
    private double lastAmountCharged = 0.0; // Montant réellement payé à la dernière réservation

    @Inject
    public ReservationService(StockService stockService, PaymentService paymentService, UiService uiService) {
        this.stockService = stockService;
        this.paymentService = paymentService;
        this.uiService = uiService;
    }

    public boolean reserver(IInstallation installation) {
        // 1. Vérifier la disponibilité initiale
        if (!installation.isLibre()) {
            System.out.println("[RESERVATION] Installation non disponible (Etat: " + installation.getEtat() + ")");
        return false;
    }

        // 2. Choix des options (Décorateurs)
        // Note : En test JUnit, si Platform.startup() n'est pas fait, cela plantera ici.
        IInstallation installationChoisie = afficherDialogueOptions(installation);

        // 3. Choix du mode de paiement et configuration de la stratégie
        double prixTotal = installationChoisie.getPrix();
        double prixPMR = prixTotal - installation.getPrix();
        
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("CB", "Lydia", "PMR");
        choiceDialog.setTitle("Paiement");
        choiceDialog.setHeaderText("Choisissez votre mode de paiement");
        choiceDialog.setContentText(
            "💳 CB : " + String.format("%.2f", prixTotal) + " €\n" +
            "📱 Lydia : " + String.format("%.2f", prixTotal) + " €\n" +
            "♿ PMR (thèmes uniquement) : " + String.format("%.2f", prixPMR) + " €"
        );

        Optional<String> modePaiement = choiceDialog.showAndWait();
        if (modePaiement.isEmpty()) {
            return false; // L'utilisateur a annulé la popup de paiement
        }

        double prixAFacturer = prixTotal;

        if (modePaiement.get().equals("Lydia")) {

            paymentService.setStrategy(App.injector.getInstance(LydiaStrategy.class));

        } else{
            if (modePaiement.get().equals("CB")) {
                paymentService.setStrategy(App.injector.getInstance(CardStrategy.class));
            }
            else {
                paymentService.setStrategy(App.injector.getInstance(PMRStrategy.class));
                prixAFacturer = prixPMR;
            }
        }

        // 4. Tentative de paiement
        boolean paiementOk = paymentService.processPayment(prixAFacturer);

        if (paiementOk) {
            // Stocker le montant réellement payé
            this.lastAmountCharged = prixAFacturer;
            
            // 5. SI PAIEMENT RÉUSSI : On valide la réservation
            installation.setEtat(EtatInstallation.RESERVE);

            // Consommation des stocks (papier, savon, etc.)
            stockService.consume(installation);

            // Notification aux observateurs (pour l'UI et la maintenance)
            installation.notifyObservers(SanitaireEvent.OCCUPATION_CHANGEE);

            // Lancer le timer de 60 secondes
            installation.setTimeReservedUntil(System.currentTimeMillis() + 60000);

            System.out.println("[RESERVATION] Succès pour : " + installation.getDescription());
            System.out.println("[RESERVATION] Montant payé : " + prixAFacturer + "€");
            return true;
        } else {
            System.out.println("[RESERVATION] Échec du paiement.");
            return false;
        }
    }

    private IInstallation afficherDialogueOptions(IInstallation base) {
        Dialog<IInstallation> dialog = new Dialog<>();
        dialog.setTitle("Options de confort");
        dialog.setHeaderText("Personnalisez votre expérience");

        CheckBox cbLumiere = new CheckBox("Option Lumière (+0.50€)");
        CheckBox cbOl = new CheckBox("Thème Olympique Lyonnais (+1.50€)");
        CheckBox cbVIP = new CheckBox("Thème VIP (+2.00€)");
        CheckBox cbGamer = new CheckBox("Thème Gamer (+3.50€)");
        CheckBox cbEco = new CheckBox("Thème Éco-responsable (+7.50€)");

        VBox container = new VBox(10, cbLumiere, cbOl, cbVIP, cbGamer, cbEco);
        dialog.getDialogPane().setContent(container);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                IInstallation result = base;
                if (cbLumiere.isSelected()) result = new LumiereDecorator(result);
                if (cbOl.isSelected()) result = new OlDecorator(result);
                if (cbVIP.isSelected()) result = new VipDecorator(result);
                if (cbGamer.isSelected()) result = new GamerDecorator(result);
                if (cbEco.isSelected()) result = new EcoDecorator(result);

                // AJOUTER : Enregistrer maintenant quels CheckBox étaient cochés
                List<String> selected = new ArrayList<>();
                if (cbLumiere.isSelected()) selected.add("lumiere");
                if (cbOl.isSelected()) selected.add("ol");
                if (cbVIP.isSelected()) selected.add("vip");
                if (cbGamer.isSelected()) selected.add("gamer");
                if (cbEco.isSelected()) selected.add("eco");
                uiService.setDecorations(base, selected);  // Appeler directement ici

                return result;
            }
            return base;
        });

        return dialog.showAndWait().orElse(base);
    }

    public void liberer(IInstallation installation) {
        installation.setEtat(EtatInstallation.LIBRE);
        installation.setTimeReservedUntil(-1);
        installation.notifyObservers(SanitaireEvent.OCCUPATION_CHANGEE);
        installation.notifyObservers(SanitaireEvent.NETTOYAGE_REQUIS);
    }

    /**
     * Retourne le montant réellement payé pour la dernière réservation.
     * Pour PMR : c'est le prix des décorateurs uniquement.
     * Pour CB/Lydia : c'est le prix total (base + décorateurs).
     */
    public double getLastAmountCharged() {
        return lastAmountCharged;
    }
}

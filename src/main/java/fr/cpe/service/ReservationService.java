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
    private double lastAmountCharged = 0.0;

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

        // 2. Choix des options
        IInstallation installationChoisie = afficherDialogueOptions(installation);

        // 3. Choix du mode de paiement
        double prixTotal = installationChoisie.getPrix();
        double prixPMR = prixTotal - installation.getPrix();

        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("CB", "Lydia", "PMR");
        choiceDialog.setTitle("Paiement");
        choiceDialog.setHeaderText("Choisissez votre mode de paiement");
        choiceDialog.setContentText(
            "💳 CB : " + String.format("%.2f", prixTotal) + " €\n" +
            "📱 Lydia : " + String.format("%.2f", prixTotal) + " €\n" +
            "♿ PMR : " + String.format("%.2f", prixPMR) + " €"
        );

        Optional<String> modePaiement = choiceDialog.showAndWait();
        if (modePaiement.isEmpty()) return false;

        double prixAFacturer = prixTotal;
        if (modePaiement.get().equals("Lydia")) {
            paymentService.setStrategy(App.injector.getInstance(LydiaStrategy.class));
        } else if (modePaiement.get().equals("CB")) {
            paymentService.setStrategy(App.injector.getInstance(CardStrategy.class));
        } else {
            paymentService.setStrategy(App.injector.getInstance(PMRStrategy.class));
            prixAFacturer = prixPMR;
        }

        // 4. Tentative de paiement
        boolean paiementOk = paymentService.processPayment(prixAFacturer);

        if (paiementOk) {
            this.lastAmountCharged = prixAFacturer;

            // --- LOGIQUE DERNIÈRE UTILISATION ---

            // On met l'état à RESERVE pour afficher le timer et le point rouge
            installation.setEtat(EtatInstallation.RESERVE);

            // On consomme le stock (L'alerte sera envoyée par StockService si c'est le dernier)
            stockService.consume(installation);

            // Notification pour l'UI (Timer + Couleur)
            installation.notifyObservers(SanitaireEvent.OCCUPATION_CHANGEE);

            // Lancer le timer
            installation.setTimeReservedUntil(System.currentTimeMillis() + 10000);

            System.out.println("[RESERVATION] Succès (Dernière unité possible) pour : " + installation.getDescription());
            return true;
        } else {
            System.out.println("[RESERVATION] Échec du paiement.");
            return false;
        }
    }

    public void liberer(IInstallation installation) {
        // 1. Protection Consommables : On vérifie si la liste existe avant le stream
        boolean ruptureAtteinte = false;
        if (installation.getConsommables() != null) {
            ruptureAtteinte = installation.getConsommables().stream()
                    .anyMatch(c -> c.getQuantite() <= 1);
        }

        if (ruptureAtteinte) {
            installation.setEtat(EtatInstallation.EN_MAINTENANCE);
        } else {
            installation.setEtat(EtatInstallation.LIBRE);
        }

        installation.setTimeReservedUntil(-1);

        // 2. PROTECTION CRUCIALE : On vérifie si uiService est null avant d'appeler clearDecorations
        // Cela évitera le NullPointerException dans tes tests unitaires
        if (uiService != null) {
            uiService.clearDecorations(installation);
        }

        installation.notifyObservers(SanitaireEvent.OCCUPATION_CHANGEE);
        installation.notifyObservers(SanitaireEvent.NETTOYAGE_REQUIS);
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
                List<String> selected = new ArrayList<>();
                if (cbLumiere.isSelected()) { result = new LumiereDecorator(result); selected.add("lumiere"); }
                if (cbOl.isSelected()) { result = new OlDecorator(result); selected.add("ol"); }
                if (cbVIP.isSelected()) { result = new VipDecorator(result); selected.add("vip"); }
                if (cbGamer.isSelected()) { result = new GamerDecorator(result); selected.add("gamer"); }
                if (cbEco.isSelected()) { result = new EcoDecorator(result); selected.add("eco"); }

                uiService.setDecorations(base, selected);
                return result;
            }
            return base;
        });

        return dialog.showAndWait().orElse(base);
    }

    public double getLastAmountCharged() {
        return lastAmountCharged;
    }
}

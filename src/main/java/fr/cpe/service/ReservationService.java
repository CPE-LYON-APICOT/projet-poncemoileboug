package fr.cpe.service;

import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import fr.cpe.App;
import fr.cpe.model.installation.Installation;
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

    @Inject
    public ReservationService(StockService stockService, PaymentService paymentService, UiService uiService) {
        this.stockService = stockService;
        this.paymentService = paymentService;
        this.uiService = uiService;
    }

    public boolean reserver(Installation installation) {
        // 1. Vérifier la disponibilité initiale
        if (!installation.isDisponible()) {
            System.out.println("[RESERVATION] Installation indisponible : " + installation.getDescription());
            return false;
        }

        // 2. Choix des options (Décorateurs)
        // Note : En test JUnit, si Platform.startup() n'est pas fait, cela plantera ici.
        Installation installationChoisie = afficherDialogueOptions(installation);

        // 3. Choix du mode de paiement et configuration de la stratégie
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("CB", "Lydia");
        choiceDialog.setTitle("Paiement");
        choiceDialog.setHeaderText("Choisissez votre mode de paiement");

        Optional<String> modePaiement = choiceDialog.showAndWait();
        if (modePaiement.isEmpty()) {
            return false; // L'utilisateur a annulé la popup de paiement
        }

        if (modePaiement.get().equals("Lydia")) {
            paymentService.setStrategy(App.injector.getInstance(LydiaStrategy.class));
        } else {
            paymentService.setStrategy(App.injector.getInstance(CardStrategy.class));
        }

        // 4. Tentative de paiement
        boolean paiementOk = paymentService.processPayment(installationChoisie.getPrix());

        if (paiementOk) {
            // 5. SI PAIEMENT RÉUSSI : On valide la réservation
            installation.setDisponible(false);

            // Consommation des stocks (papier, savon, etc.)
            stockService.consume(installation);

            // Notification aux observateurs (pour l'UI et la maintenance)
            installation.notifyObservers(SanitaireEvent.OCCUPATION_CHANGEE);

            // Lancer le timer de 60 secondes
            installation.setTimeReservedUntil(System.currentTimeMillis() + 60000);

            System.out.println("[RESERVATION] Succès pour : " + installation.getDescription());
            return true;
        } else {
            System.out.println("[RESERVATION] Échec du paiement.");
            return false;
        }
    }

    private Installation afficherDialogueOptions(Installation base) {
        Dialog<Installation> dialog = new Dialog<>();
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
                Installation result = base;
                if (cbLumiere.isSelected()) result = new LumiereDecorator(result);
                if (cbOl.isSelected()) result = new OlDecorator(result);
                if (cbVIP.isSelected()) result = new VipDecorator(result);
                if (cbGamer.isSelected()) result = new GamerDecorator(result);
                if (cbEco.isSelected()) result = new EcoDecorator(result);
                return result;
            }
            return base;
        });

        return dialog.showAndWait().orElse(base);
    }

    public void liberer(Installation installation) {
        installation.setDisponible(true);
        installation.setTimeReservedUntil(-1);
        installation.notifyObservers(SanitaireEvent.OCCUPATION_CHANGEE);
        installation.notifyObservers(SanitaireEvent.NETTOYAGE_REQUIS);
    }

    void enregistrerDecorateurs(Installation originale, Installation decoree, UiService uiService) {
    if (decoree instanceof LumiereDecorator) {
        uiService.ajouterDecoration(originale, "lumiere");
    }
    if (decoree instanceof OlDecorator) {
        uiService.ajouterDecoration(originale, "ol");
    }
    if (decoree instanceof VipDecorator) {
        uiService.ajouterDecoration(originale, "vip");
    }
    }
}

package fr.cpe.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import fr.cpe.App;
import fr.cpe.model.installation.Installation;
import fr.cpe.model.installation.decorator.LumiereDecorator;
import fr.cpe.model.installation.decorator.OlDecorator;
import fr.cpe.model.installation.decorator.VipDecorator;
import fr.cpe.model.observer.SanitaireEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import java.util.Optional;

@Singleton
public class ReservationService {

    private final StockService stockService;
    private final PaymentService paymentService;

    @Inject
    public ReservationService(StockService stockService, PaymentService paymentService) {
        this.stockService = stockService;
        this.paymentService = paymentService;
    }

    // Retourne true si la réservation a été effectuée, false sinon
    public boolean reserver(Installation installation) {
        // 1. Vérifier la disponibilité
        if (!installation.isDisponible()) {
            System.out.println("[RESERVATION] Installation indisponible : " + installation.getDescription());
            return false;
        }


        Installation installationChoisie = afficherDialogueOptions(installation);

        // 2. Marquer comme occupé
        installation.setDisponible(false);
        installation.notifyObservers(SanitaireEvent.OCCUPATION_CHANGEE);

        ChoiceDialog<String> choiceDialog = new ChoiceDialog<String>("CB","Lydia");
        choiceDialog.showAndWait().ifPresent((String e)->{

            if(e=="Lydia"){
                paymentService.setStrategy(App.injector.getInstance(LydiaStrategy.class));
            }else if (e=="CB"){
                paymentService.setStrategy(App.injector.getInstance(CardStrategy.class));
            }
        });
        boolean paiementOk = paymentService.processPayment(installationChoisie.getPrix());

        if (!paiementOk) {
            return false;
        }

        installation.setDisponible(false);
        installation.notifyObservers(SanitaireEvent.OCCUPATION_CHANGEE);
        stockService.consume(installation);

        // 5. Lancer le timer de 60 secondes
        installation.setTimeReservedUntil(System.currentTimeMillis() + 60000);

        return true;
    }

    private Installation afficherDialogueOptions(Installation base) {
        Dialog<Installation> dialog = new Dialog<>();
        dialog.setTitle("Options de confort");
        dialog.setHeaderText("Personnalisez votre expérience");

        CheckBox cbLumiere = new CheckBox("Option Lumière (+0.50€)");
        CheckBox cbOl = new CheckBox("Thème Olympique Lyonnais (+1.50€)");
        CheckBox cbVIP = new CheckBox("Thème VIP (+2.00€)");

        VBox container = new VBox(10, cbLumiere, cbOl, cbVIP);
        dialog.getDialogPane().setContent(container);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        // On transforme les choix en décorateurs lors du clic sur OK
        dialog.setResultConverter(button -> {
            Installation result = base;
            if (cbLumiere.isSelected()) {
                result = new LumiereDecorator(result);
            }
            if (cbOl.isSelected()) {
                result = new OlDecorator(result);
            }
            if (cbVIP.isSelected()) {
                result = new VipDecorator(result);
            }
            return result;
        });

        Optional<Installation> optionResult = dialog.showAndWait();
        return optionResult.orElse(base);
    }

    // Appelé par le GameEngine quand le timer expire
    public void liberer(Installation installation) {
        installation.setDisponible(true);
        installation.setTimeReservedUntil(-1);
        installation.notifyObservers(SanitaireEvent.OCCUPATION_CHANGEE);
        installation.notifyObservers(SanitaireEvent.NETTOYAGE_REQUIS);
    }
}

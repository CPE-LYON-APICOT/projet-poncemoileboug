package fr.cpe.service;

import com.google.inject.Inject;
import fr.cpe.model.installation.Installation;
import fr.cpe.model.observer.SanitaireEvent;

public class ReservationService {

    private final StockService stockService;
    private final PaymentService paymentService;

    // Guice injecte automatiquement les dépendances via ce constructeur
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

        // 2. Marquer comme occupé
        installation.setDisponible(false);
        installation.notifyObservers(SanitaireEvent.OCCUPATION_CHANGEE);

        // 3. Traiter le paiement
        boolean paiementOk = paymentService.processPayment(installation.getPrix());
        if (!paiementOk) {
            // Rollback si le paiement échoue
            installation.setDisponible(true);
            return false;
        }

        // 4. Décrémenter les stocks
        stockService.consume(installation);

        return true;
    }

    // Appelé par le GameEngine quand le timer expire
    public void liberer(Installation installation) {
        installation.setDisponible(true);
        installation.notifyObservers(SanitaireEvent.OCCUPATION_CHANGEE);
        installation.notifyObservers(SanitaireEvent.NETTOYAGE_REQUIS);
    }
}

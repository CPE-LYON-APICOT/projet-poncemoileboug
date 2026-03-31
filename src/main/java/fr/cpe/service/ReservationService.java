package fr.cpe.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.google.inject.Inject;

import fr.cpe.model.installation.Installation;
import fr.cpe.model.observer.SanitaireEvent;
import fr.cpe.model.reservation.Reservation;
import fr.cpe.model.user.User;

public class ReservationService {
    private final StockService stockService;
    private final PaymentService paymentService;
    private final Map<String, Reservation> reservations = new HashMap<>();
    private final Timer timer = new Timer();

    @Inject
    public ReservationService(StockService stockService, PaymentService paymentService) {
        this.stockService = stockService;
        this.paymentService = paymentService;
    }

    /**
     * Crée une réservation, valide la disponibilité et démarre le timer
     */
    public Reservation reserver(User user, Installation installation, 
                                int durationMinutes, String paymentMethod) {
        // Vérifier la disponibilité
        if (!installation.isDisponible()) {
            throw new IllegalStateException("installation non disponible");
        }

        // Effectuer le paiement
        double prix = installation.getPrix();
        paymentService.processPayment(user, prix, paymentMethod);

        // Créer la réservation
        String qrCode = generateQRCode();
        Reservation reservation = new Reservation(
            UUID.randomUUID().toString(),
            user,
            installation,
            LocalDateTime.now(),
            durationMinutes,
            qrCode
        );

        // Marquer l'installation comme occupée
        installation.setDisponible(false);
        reservations.put(qrCode, reservation);

        // Lancer le timer pour libérer l'installation
        scheduleLiberation(installation, durationMinutes, qrCode);

        return reservation;
    }

    /**
     * À la fin du timer : libérer l'installation, consommer les stocks, notifier
     */
    private void scheduleLiberation(Installation installation, int minutes, String qrCode) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Consommer les stocks
                stockService.consume(installation);

                // Marquer comme disponible
                installation.setDisponible(true);

                // Notifier les observateurs (nettoyage, carte, etc.)
                installation.notifyObservers(SanitaireEvent.OCCUPATION_CHANGEE);
                installation.notifyObservers(SanitaireEvent.NETTOYAGE_REQUIS);

                // Nettoyer la réservation
                reservations.remove(qrCode);
            }
        }, minutes * 60_000L); // convertir en millisecondes
    }

    private String generateQRCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public Reservation getReservation(String qrCode) {
        return reservations.get(qrCode);
    }
}
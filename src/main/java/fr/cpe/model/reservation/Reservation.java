package fr.cpe.model.reservation;

import java.time.LocalDateTime;

import fr.cpe.model.installation.Installation;
import fr.cpe.model.user.User;

/**
 * Représente une réservation d'une installation sanitaire.
 *
 * <p>Chaque réservation est liée à :</p>
 * <ul>
 *   <li>Un utilisateur qui effectue la réservation</li>
 *   <li>Une installation sanitaire (Cabine, Douche, Urinoir)</li>
 *   <li>Un QR code d'accès unique et temporaire</li>
 *   <li>Une durée définie en minutes</li>
 * </ul>
 *
 * <p>Une réservation passe par plusieurs états :</p>
 * <ul>
 *   <li>ACTIVE : L'utilisateur peut utiliser l'installation</li>
 *   <li>COMPLETED : La réservation a expiré, l'installation est libérée</li>
 *   <li>CANCELLED : L'utilisateur a annulé manuellement</li>
 * </ul>
 */
public class Reservation {

    public enum ReservationStatus {
        ACTIVE, COMPLETED, CANCELLED
    }

    private final String reservationId;
    private final User user;
    private final Installation installation;
    private final LocalDateTime createdAt;
    private final int durationMinutes;
    private final String qrCode;
    private ReservationStatus status;

    /**
     * Crée une nouvelle réservation.
     *
     * @param reservationId      Identifiant unique de la réservation
     * @param user               L'utilisateur qui réserve
     * @param installation       L'installation réservée
     * @param createdAt          L'heure de création de la réservation
     * @param durationMinutes    La durée de la réservation en minutes
     * @param qrCode             Code QR unique pour accéder à l'installation
     */
    public Reservation(String reservationId, User user, Installation installation,
                      LocalDateTime createdAt, int durationMinutes, String qrCode) {
        this.reservationId = reservationId;
        this.user = user;
        this.installation = installation;
        this.createdAt = createdAt;
        this.durationMinutes = durationMinutes;
        this.qrCode = qrCode;
        this.status = ReservationStatus.ACTIVE;
    }

    // ========== Getters ==========

    public String getReservationId() {
        return reservationId;
    }

    public User getUser() {
        return user;
    }

    public Installation getInstallation() {
        return installation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getQrCode() {
        return qrCode;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    /**
     * Calcule l'heure d'expiration de la réservation.
     */
    public LocalDateTime getExpiryTime() {
        return createdAt.plusMinutes(durationMinutes);
    }

    /**
     * Vérifie si la réservation est encore valide (n'a pas expiré).
     */
    public boolean isValid() {
        if (status != ReservationStatus.ACTIVE) {
            return false;
        }
        return LocalDateTime.now().isBefore(getExpiryTime());
    }

    // ========== Setters ==========

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    /**
     * Marque la réservation comme complétée.
     */
    public void complete() {
        this.status = ReservationStatus.COMPLETED;
    }

    /**
     * Marque la réservation comme annulée.
     */
    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    // ========== Utilitaires ==========

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", user=" + user.getName() +
                ", installation=" + installation.getDescription() +
                ", createdAt=" + createdAt +
                ", durationMinutes=" + durationMinutes +
                ", status=" + status +
                '}';
    }
}

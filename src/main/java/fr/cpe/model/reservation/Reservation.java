package fr.cpe.model.reservation;

import java.time.LocalDateTime;

import fr.cpe.model.installation.IInstallation;
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
 *   <li>{@link ReservationStatus#ACTIVE} : L'utilisateur peut utiliser l'installation</li>
 *   <li>{@link ReservationStatus#COMPLETED} : La réservation a expiré, l'installation est libérée</li>
 *   <li>{@link ReservationStatus#CANCELLED} : L'utilisateur a annulé manuellement</li>
 * </ul>
 */
public class Reservation {

    /**
     * Énumération des états possibles d'une réservation.
     */
    public enum ReservationStatus {

        /** La réservation est en cours et l'installation est accessible. */
        ACTIVE,

        /** La réservation s'est terminée normalement, l'installation est libérée. */
        COMPLETED,

        /** La réservation a été annulée manuellement par l'utilisateur. */
        CANCELLED
    }

    /** Identifiant unique et immuable de la réservation. */
    private final String reservationId;

    /** Utilisateur ayant effectué la réservation. */
    private final User user;

    /** Installation sanitaire réservée. */
    private final IInstallation installation;

    /** Horodatage de création de la réservation. */
    private final LocalDateTime createdAt;

    /** Durée de la réservation exprimée en minutes. */
    private final int durationMinutes;

    /** QR code unique permettant l'accès à l'installation réservée. */
    private final String qrCode;

    /** État courant de la réservation. */
    private ReservationStatus status;

    /**
     * Crée une nouvelle réservation avec le statut initial {@link ReservationStatus#ACTIVE}.
     *
     * @param reservationId   identifiant unique de la réservation
     * @param user            l'utilisateur qui effectue la réservation
     * @param installation    l'installation sanitaire réservée
     * @param createdAt       l'horodatage de création de la réservation
     * @param durationMinutes la durée de la réservation en minutes
     * @param qrCode          le code QR unique pour accéder à l'installation
     */
    public Reservation(String reservationId, User user, IInstallation installation,
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

    /**
     * Retourne l'identifiant unique de la réservation.
     *
     * @return l'identifiant de la réservation
     */
    public String getReservationId() {
        return reservationId;
    }

    /**
     * Retourne l'utilisateur ayant effectué la réservation.
     *
     * @return l'utilisateur associé à la réservation
     */
    public User getUser() {
        return user;
    }

    /**
     * Retourne l'installation sanitaire réservée.
     *
     * @return l'installation associée à la réservation
     */
    public IInstallation getInstallation() {
        return installation;
    }

    /**
     * Retourne l'horodatage de création de la réservation.
     *
     * @return la date et l'heure de création
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Retourne la durée de la réservation en minutes.
     *
     * @return la durée en minutes
     */
    public int getDurationMinutes() {
        return durationMinutes;
    }

    /**
     * Retourne le QR code d'accès unique associé à la réservation.
     *
     * @return le QR code sous forme de chaîne de caractères
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * Retourne le statut actuel de la réservation.
     *
     * @return le {@link ReservationStatus} courant
     */
    public ReservationStatus getStatus() {
        return status;
    }

    /**
     * Calcule et retourne l'heure d'expiration de la réservation.
     * <p>
     * L'expiration correspond à l'heure de création augmentée de la durée
     * en minutes.
     * </p>
     *
     * @return la date et l'heure d'expiration de la réservation
     */
    public LocalDateTime getExpiryTime() {
        return createdAt.plusMinutes(durationMinutes);
    }

    /**
     * Vérifie si la réservation est encore valide.
     * <p>
     * Une réservation est valide si et seulement si son statut est
     * {@link ReservationStatus#ACTIVE} et que l'heure courante est
     * antérieure à l'heure d'expiration.
     * </p>
     *
     * @return {@code true} si la réservation est active et non expirée,
     *         {@code false} sinon
     */
    public boolean isValid() {
        if (status != ReservationStatus.ACTIVE) {
            return false;
        }
        return LocalDateTime.now().isBefore(getExpiryTime());
    }

    // ========== Setters ==========

    /**
     * Modifie manuellement le statut de la réservation.
     *
     * @param status le nouveau {@link ReservationStatus} à appliquer
     */
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    /**
     * Marque la réservation comme complétée.
     * <p>
     * Passe le statut à {@link ReservationStatus#COMPLETED},
     * indiquant que l'installation peut être libérée.
     * </p>
     */
    public void complete() {
        this.status = ReservationStatus.COMPLETED;
    }

    /**
     * Marque la réservation comme annulée.
     * <p>
     * Passe le statut à {@link ReservationStatus#CANCELLED},
     * indiquant que l'utilisateur a renoncé à utiliser l'installation.
     * </p>
     */
    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    // ========== Utilitaires ==========

    /**
     * Retourne une représentation textuelle de la réservation.
     * <p>
     * Inclut l'identifiant, le nom de l'utilisateur, la description
     * de l'installation, l'horodatage de création, la durée et le statut.
     * </p>
     *
     * @return une chaîne décrivant la réservation
     */
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
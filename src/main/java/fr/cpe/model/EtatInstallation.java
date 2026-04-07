package fr.cpe.model;

/**
 * Énumération représentant les états possibles d'une installation sanitaire.
 * <p>
 * Chaque installation peut se trouver à tout moment dans l'un
 * des trois états suivants :
 * </p>
 * <ul>
 *   <li>{@link #LIBRE} : l'installation est disponible et accessible</li>
 *   <li>{@link #RESERVE} : l'installation est réservée par un utilisateur</li>
 *   <li>{@link #EN_MAINTENANCE} : l'installation est temporairement hors service</li>
 * </ul>
 */
public enum EtatInstallation {

    /**
     * L'installation est disponible et peut être utilisée ou réservée.
     */
    LIBRE,

    /**
     * L'installation a été réservée et n'est plus disponible pour d'autres utilisateurs.
     */
    RESERVE,

    /**
     * L'installation est en cours de maintenance et temporairement hors service.
     */
    EN_MAINTENANCE
}
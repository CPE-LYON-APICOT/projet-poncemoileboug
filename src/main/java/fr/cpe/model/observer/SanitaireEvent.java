package fr.cpe.model.observer;

/**
 * Énumération des événements sanitaires pouvant être émis par une installation.
 * <p>
 * Ces événements sont utilisés dans le cadre du pattern Observer pour notifier
 * les observateurs ({@link IInstallationObserver}) des changements d'état
 * survenus sur une installation sanitaire.
 * </p>
 *
 * @see IInstallationObserver
 */
public enum SanitaireEvent {

    /**
     * Émis lorsque le stock de consommables d'une installation atteint un niveau critique.
     * <p>
     * Déclenche généralement une alerte auprès du service de maintenance.
     * </p>
     */
    STOCK_ALERT,

    /**
     * Émis lorsqu'une installation nécessite un nettoyage.
     * <p>
     * Notifie le service de maintenance afin qu'un agent soit dépêché.
     * </p>
     */
    NETTOYAGE_REQUIS,

    /**
     * Émis lorsque l'état d'occupation d'une installation change.
     * <p>
     * Permet aux observateurs de réagir à une libération ou une prise en charge
     * de l'installation.
     * </p>
     */
    OCCUPATION_CHANGEE
}
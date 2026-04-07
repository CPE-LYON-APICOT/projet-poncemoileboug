package fr.cpe.model.observer;

/**
 * Énumération des événements sanitaires pouvant être émis par une installation.
 * Ces événements sont utilisés dans le cadre du pattern Observer pour notifier
 * les observateurs ({@link IInstallationObserver}) des changements d'état
 * survenus sur une installation sanitaire.
 *
 * @see IInstallationObserver
 */
public enum SanitaireEvent {

    /**
     * Émis lorsque le stock de consommables d'une installation atteint un niveau critique.
     * Déclenche généralement une alerte auprès du service de maintenance.
     */
    STOCK_ALERT,

    /**
     * Émis lorsqu'une installation nécessite un nettoyage.
     * Notifie le service de maintenance afin qu'un agent soit dépêché.
     */
    NETTOYAGE_REQUIS,

    /**
     * Émis lorsque l'état d'occupation d'une installation change.
     * Permet aux observateurs de réagir à une libération ou une prise en charge
     * de l'installation.
     */
    OCCUPATION_CHANGEE
}

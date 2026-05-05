package fr.cpe.service;

import com.google.inject.Inject;

import fr.cpe.model.installation.IInstallation;
import fr.cpe.model.observer.IInstallationObserver;
import fr.cpe.model.observer.SanitaireEvent;

/**
 * Service de maintenance des installations sanitaires.
 * Implémente {@link IInstallationObserver} pour réagir aux événements
 * émis par les installations. Gère deux types d'alertes : les demandes
 * de nettoyage et les alertes de stock critique.
 *
 * @see IInstallationObserver
 * @see SanitaireEvent
 */
public class MaintenanceService implements IInstallationObserver {

    /**
     * Constructeur injecté par Guice.
     */
    @Inject
    public MaintenanceService() {}

   /**
     * Réagit à un événement émis par une installation sanitaire.
     * * Selon la nature de l'événement reçu :
     * <ul>
     * <li>{@link SanitaireEvent#NETTOYAGE_REQUIS} : notifie un agent de maintenance</li>
     * <li>{@link SanitaireEvent#STOCK_ALERT} : déclenche une alerte de stock</li>
     * </ul>
     *
     * @param source l'installation à l'origine de l'événement
     * @param event  le type d'événement sanitaire survenu
     */
    @Override
    public void onEvent(IInstallation source, SanitaireEvent event) {
        if (event == SanitaireEvent.NETTOYAGE_REQUIS) {
            notifierAgent(source);
        } else if (event == SanitaireEvent.STOCK_ALERT) {
            alerteStock(source);
        }
    }

    /**
     * Notifie un agent qu'un nettoyage est requis pour l'installation donnée.
     * <p>
     * Affiche un message en console avec la description de l'installation concernée.
     * </p>
     *
     * @param installation l'installation nécessitant un nettoyage
     */
    public void notifierAgent(IInstallation installation) {
        System.out.println("[MAINTENANCE] Nettoyage requis : " + installation.getDescription());
    }

    /**
     * Déclenche une alerte de stock critique pour l'installation donnée.
     * <p>
     * Affiche un message en console signalant que le stock de l'installation
     * est à un niveau critique.
     * </p>
     *
     * @param installation l'installation en situation de stock critique
     */
    public void alerteStock(IInstallation installation) {
        System.out.println("[MAINTENANCE] Stock critique : " + installation.getDescription());
    }
}

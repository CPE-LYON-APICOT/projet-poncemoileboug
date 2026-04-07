package fr.cpe.model.observer;

import fr.cpe.model.installation.IInstallation;

/**
 * Interface représentant un observateur des installations.
 * <p>
 * Les classes implémentant cette interface peuvent être notifiées
 * lorsqu'un événement survient sur une installation donnée.
 */
public interface IInstallationObserver {

    /**
     * Méthode appelée lorsqu'un événement survient sur une installation.
     *
     * @param source l'installation à l'origine de l'événement
     * @param event l'événement survenu (ex : changement d'état, réservation, etc.)
     */
    void onEvent(IInstallation source, SanitaireEvent event);
}
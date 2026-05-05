package fr.cpe.model.installation;

import java.util.List;

import fr.cpe.model.EtatInstallation;
import fr.cpe.model.consommable.IConsommable;
import fr.cpe.model.observer.SanitaireEvent;

/**
 * Interface représentant une installation sanitaire.
 * Elle définit les caractéristiques principales d'une installation
 * (prix, description, état, position, consommables) ainsi que
 * les mécanismes d'observation et de gestion de réservation.
 */
public interface IInstallation {

    /**
     * Retourne le prix d'utilisation de l'installation.
     *
     * @return le prix
     */
    double getPrix();

    /**
     * Retourne la description de l'installation.
     *
     * @return la description
     */
    String getDescription();

    /**
     * Retourne la liste des consommables associés à l'installation.
     *
     * @return la liste des consommables
     */
    List<IConsommable> getConsommables();

    /**
     * Retourne l'état actuel de l'installation.
     *
     * @return l'état de l'installation
     */
    EtatInstallation getEtat();

    /**
     * Définit l'état de l'installation.
     *
     * @param etat le nouvel état
     */
    void setEtat(EtatInstallation etat);

    /**
     * Indique si l'installation est libre.
     * Méthode utilitaire pour conserver une compatibilité logique.
     *
     * @return true si l'installation est libre, false sinon
     */
    default boolean isLibre() {
        return getEtat() == EtatInstallation.LIBRE;
    }

    /**
     * Notifie les observateurs d'un événement.
     *
     * @param event l'événement à notifier
     */
    void notifyObservers(SanitaireEvent event);

    /**
     * Ajoute un observateur à l'installation.
     *
     * @param observer l'observateur à ajouter
     */
    void addObserver(fr.cpe.model.observer.IInstallationObserver observer);

    /**
     * Retourne la position X de l'installation.
     *
     * @return la coordonnée X
     */
    double getX();

    /**
     * Retourne la position Y de l'installation.
     *
     * @return la coordonnée Y
     */
    double getY();

    /**
     * Définit la position de l'installation.
     *
     * @param x coordonnée X
     * @param y coordonnée Y
     */
    void setPosition(double x, double y);

    /**
     * Retourne le temps jusqu'auquel l'installation est réservée.
     *
     * @return le timestamp de fin de réservation
     */
    long getTimeReservedUntil();

    /**
     * Définit le temps jusqu'auquel l'installation est réservée.
     *
     * @param timeReservedUntil le timestamp de fin de réservation
     */
    void setTimeReservedUntil(long timeReservedUntil);
}

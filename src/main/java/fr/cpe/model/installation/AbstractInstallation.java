package fr.cpe.model.installation;

import java.util.ArrayList;
import java.util.List;

import fr.cpe.model.EtatInstallation;
import fr.cpe.model.consommable.IConsommable;
import fr.cpe.model.observer.IInstallationObserver;
import fr.cpe.model.observer.SanitaireEvent;

/**
 * Classe abstraite représentant une installation sanitaire générique.
 * <p>
 * Elle fournit une implémentation de base pour la gestion des consommables,
 * de l'état, des observateurs, de la position et des réservations.
 */
public abstract class AbstractInstallation implements IInstallation {

    /**
     * Liste des consommables associés à l'installation.
     */
    protected List<IConsommable> consommables;

    /**
     * Indique si l'installation est disponible (non utilisé directement avec EtatInstallation).
     */
    protected boolean disponible = true;

    /**
     * Identifiant unique de l'installation.
     */
    private String id;

    /**
     * Coordonnée X de l'installation.
     */
    private double x;

    /**
     * Coordonnée Y de l'installation.
     */
    private double y;

    /**
     * Temps jusqu'auquel l'installation est réservée (timestamp).
     */
    private long timeReservedUntil = -1;

    /**
     * Liste des observateurs inscrits à cette installation.
     */
    private final List<IInstallationObserver> observers = new ArrayList<>();

    /**
     * Constructeur de la classe AbstractInstallation.
     *
     * @param consommables liste des consommables associés (initialisée vide si null)
     */
    public AbstractInstallation(List<IConsommable> consommables) {
        if (consommables != null) {
            this.consommables = consommables;
        } else {
            this.consommables = new ArrayList<>();
        }
    }

    /**
     * État actuel de l'installation.
     */
    private EtatInstallation etat = EtatInstallation.LIBRE;

    /**
     * Retourne l'état actuel de l'installation.
     *
     * @return l'état
     */
    @Override
    public EtatInstallation getEtat() {
        return this.etat;
    }

    /**
     * Définit l'état de l'installation.
     *
     * @param etat le nouvel état
     */
    @Override
    public void setEtat(EtatInstallation etat) {
        this.etat = etat;
    }

    /**
     * Retourne la liste des consommables associés.
     *
     * @return la liste des consommables
     */
    @Override
    public List<IConsommable> getConsommables() {
        return consommables;
    }

    /**
     * Ajoute un observateur à la liste s'il n'est pas déjà présent.
     *
     * @param observer l'observateur à ajouter
     */
    @Override
    public void addObserver(IInstallationObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Notifie tous les observateurs d'un événement.
     *
     * @param event l'événement à notifier
     */
    @Override
    public void notifyObservers(SanitaireEvent event) {
        for (IInstallationObserver observer : observers) {
            observer.onEvent(this, event);
        }
    }

    /**
     * Retourne l'identifiant de l'installation.
     *
     * @return l'identifiant
     */
    public String getId() { return id; }

    /**
     * Définit l'identifiant de l'installation.
     *
     * @param id identifiant à définir
     */
    public void setId(String id) { this.id = id; }

    /**
     * Définit la coordonnée X.
     *
     * @param x valeur de X
     */
    public void setX(double x) { this.x = x; }

    /**
     * Définit la coordonnée Y.
     *
     * @param y valeur de Y
     */
    public void setY(double y) { this.y = y; }

    /**
     * Définit la position complète de l'installation.
     *
     * @param x coordonnée X
     * @param y coordonnée Y
     */
    @Override
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retourne la coordonnée X.
     *
     * @return X
     */
    @Override
    public double getX() {
        return this.x;
    }

    /**
     * Retourne la coordonnée Y.
     *
     * @return Y
     */
    @Override
    public double getY() {
        return this.y;
    }

    /**
     * Retourne le temps de fin de réservation.
     *
     * @return le timestamp de réservation
     */
    @Override
    public long getTimeReservedUntil() {
        return this.timeReservedUntil;
    }

    /**
     * Définit le temps de fin de réservation.
     *
     * @param timeReservedUntil timestamp de fin de réservation
     */
    @Override
    public void setTimeReservedUntil(long timeReservedUntil) {
        this.timeReservedUntil = timeReservedUntil;
    }
}
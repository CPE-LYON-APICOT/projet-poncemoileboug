package fr.cpe.model.installation.decorator;

import java.util.List;
import fr.cpe.model.EtatInstallation;
import fr.cpe.model.consommable.IConsommable;
import fr.cpe.model.installation.IInstallation;
import fr.cpe.model.observer.IInstallationObserver;
import fr.cpe.model.observer.SanitaireEvent;

/**
 * Classe abstraite pour tous les décorateurs d'installation.
 * Elle permet d'ajouter dynamiquement des fonctionnalités à une installation
 * en déléguant les appels de méthodes à l'objet décoré.
 */
public abstract class InstallationDecorator implements IInstallation {

    /** L'instance d'installation originale qui est enveloppée par ce décorateur. */
    protected IInstallation decorated;

    /**
     * @param decorated L'instance d'installation à décorer.
     */
    public InstallationDecorator(IInstallation decorated) {
        this.decorated = decorated;
    }

    @Override
    public double getPrix() {
        return decorated.getPrix();
    }

    @Override
    public String getDescription() {
        return decorated.getDescription();
    }

    @Override
    public List<IConsommable> getConsommables() {
        return decorated.getConsommables();
    }

    @Override
    public EtatInstallation getEtat() {
        return decorated.getEtat();
    }

    @Override
    public void setEtat(EtatInstallation etat) {
        decorated.setEtat(etat);
    }

    @Override
    public void notifyObservers(SanitaireEvent event) {
        decorated.notifyObservers(event);
    }

    @Override
    public void addObserver(IInstallationObserver observer) {
        decorated.addObserver(observer);
    }

    @Override
    public double getX() {
        return decorated.getX();
    }

    @Override
    public double getY() {
        return decorated.getY();
    }

    @Override
    public void setPosition(double x, double y) {
        decorated.setPosition(x, y);
    }

    @Override
    public long getTimeReservedUntil() {
        return decorated.getTimeReservedUntil();
    }

    @Override
    public void setTimeReservedUntil(long timeReservedUntil) {
        decorated.setTimeReservedUntil(timeReservedUntil);
    }
}

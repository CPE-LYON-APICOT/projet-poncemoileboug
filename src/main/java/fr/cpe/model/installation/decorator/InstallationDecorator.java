package fr.cpe.model.installation.decorator;

import java.util.List;

import fr.cpe.model.consommable.IConsommable;
import fr.cpe.model.installation.IInstallation;
import fr.cpe.model.observer.IInstallationObserver;
import fr.cpe.model.observer.SanitaireEvent;

public abstract class InstallationDecorator implements IInstallation {

    protected IInstallation decorated; // L'instance décorée (protected pour être accessible par les sous-classes)

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
    public boolean isDisponible() {
        return decorated.isDisponible();
    }

    @Override
    public void setDisponible(boolean disponible) {
        decorated.setDisponible(disponible);
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

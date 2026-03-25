package fr.cpe.model.installation;

import fr.cpe.model.consommable.Consommable;
import fr.cpe.model.observer.InstallationObserver;
import fr.cpe.model.observer.SanitaireEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInstallation implements Installation {

    protected List<Consommable> consommables;
    protected boolean disponible = true;

    private final List<InstallationObserver> observers = new ArrayList<>();

    public AbstractInstallation(List<Consommable> consommables) {
        this.consommables = (consommables != null) ? consommables : new ArrayList<>();
    }

    @Override
    public boolean isDisponible() {
        return disponible;
    }

    @Override
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
        // Optionnel : on pourrait notifier du changement d'occupation ici
        // notifyObservers(SanitaireEvent.OCCUPATION_CHANGEE);
    }

    @Override
    public List<Consommable> getConsommables() {
        return consommables;
    }

    public void addObserver(InstallationObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void notifyObservers(SanitaireEvent event) {
        for (InstallationObserver observer : observers) {
            observer.onEvent(this, event);
        }
    }
}

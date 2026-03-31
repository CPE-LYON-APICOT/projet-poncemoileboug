package fr.cpe.model.installation;

import java.util.ArrayList;
import java.util.List;

import fr.cpe.model.consommable.Consommable;
import fr.cpe.model.observer.InstallationObserver;
import fr.cpe.model.observer.SanitaireEvent;

public abstract class AbstractInstallation implements Installation {

    protected List<Consommable> consommables;
    protected boolean disponible = true;
    private String id;
    private double x;
    private double y;
    private long timeReservedUntil = -1;

    private final List<InstallationObserver> observers = new ArrayList<>();

    public AbstractInstallation(List<Consommable> consommables) {
        if (consommables != null) {
            this.consommables = consommables;
        } else {
            this.consommables = new ArrayList<>();
        }
    }

    @Override
    public boolean isDisponible() {
        return disponible;
    }

    @Override
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public List<Consommable> getConsommables() {
        return consommables;
    }
    @Override
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
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    @Override
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public long getTimeReservedUntil() {
        return this.timeReservedUntil;
    }

    @Override
    public void setTimeReservedUntil(long timeReservedUntil) {
        this.timeReservedUntil = timeReservedUntil;
    }
}

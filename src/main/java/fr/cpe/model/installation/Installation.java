package fr.cpe.model.installation;

import java.util.List;

import fr.cpe.model.consommable.Consommable;
import fr.cpe.model.observer.SanitaireEvent;

public interface Installation {

    double getPrix();
    String getDescription();
    List<Consommable> getConsommables();
    boolean isDisponible();
    void setDisponible(boolean disponible);

    void notifyObservers(SanitaireEvent event);
    void addObserver(fr.cpe.model.observer.InstallationObserver observer);
    double getX();
    double getY();
    void setPosition(double x, double y);

    long getTimeReservedUntil();
    void setTimeReservedUntil(long timeReservedUntil);
}

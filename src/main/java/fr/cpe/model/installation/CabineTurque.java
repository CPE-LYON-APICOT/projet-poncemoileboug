package fr.cpe.model.installation;

import java.util.ArrayList;
import fr.cpe.model.observer.SanitaireEvent;
import java.util.List;

import fr.cpe.model.consommable.Consommable;

public class CabineTurque implements Installation {
    private static final double PRIX_TURQUE = 1.00;

    private final String description;
    private final List<Consommable> lesConsommables;
    private boolean disponible;

    public CabineTurque(List<Consommable> lesConsommables) {
        this.description = "Cabine turque";
        this.lesConsommables = new ArrayList<>(lesConsommables);
        this.disponible = true;
    }

    @Override
    public double getPrix() {
        return PRIX_TURQUE;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<Consommable> getConsommables() {
        return new ArrayList<>(lesConsommables); // ici on renvoie une COPIE de la liste pour éviter
        // de se la faire vider avec cabine.getConsommables().clear();
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
    public void notifyObservers(SanitaireEvent event) {
        System.out.println("Notification : " + event + " sur Cabine Turque");
    }
}

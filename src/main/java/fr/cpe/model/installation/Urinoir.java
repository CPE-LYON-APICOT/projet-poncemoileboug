package fr.cpe.model.installation;

import java.util.ArrayList;
import fr.cpe.model.observer.SanitaireEvent;
import java.util.List;

import fr.cpe.model.consommable.Consommable;

public class Urinoir implements Installation {
    private static final double PRIX_URINOIR = 0.5;

    private final String description;
    private final List<Consommable> lesConsommables;
    private boolean disponible;

    public Urinoir(List<Consommable> lesConsommables) {
        this.description = "Urinoir";
        this.lesConsommables = new ArrayList<>(lesConsommables);
        this.disponible = true;
    }

    @Override
    public double getPrix() {
        return PRIX_URINOIR;
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
        System.out.println("Notification : " + event + " sur un Urinoir");
    }

}

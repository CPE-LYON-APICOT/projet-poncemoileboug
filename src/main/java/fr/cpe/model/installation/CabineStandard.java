package fr.cpe.model.installation;

import java.util.ArrayList;
import java.util.List;

import fr.cpe.model.consommable.Consommable;

public class CabineStandard implements Installation {
    private static final double PRIX_STANDARD = 1.50;

    private final String description;
    private final List<Consommable> lesConsommables;
    private boolean disponible;

    public CabineStandard(List<Consommable> lesConsommables) {
        this.description = "Cabine standard";
        this.lesConsommables = new ArrayList<>(lesConsommables);
        this.disponible = true;
    }

    @Override
    public double getPrix() {
        return PRIX_STANDARD;
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
}

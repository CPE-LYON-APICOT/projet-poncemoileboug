package fr.cpe.model.installation;

import java.util.List;
import fr.cpe.model.consommable.Consommable;

public class Douche extends AbstractInstallation {
    private static final double PRIX_DOUCHE = 3.00;
    private final String description;

    public Douche(List<Consommable> lesConsommables, String description) {
        super(lesConsommables);
        this.description = description;
    }

    @Override
    public double getPrix() {
        return PRIX_DOUCHE;
    }

    @Override
    public String getDescription() {
        return description;
    }
}

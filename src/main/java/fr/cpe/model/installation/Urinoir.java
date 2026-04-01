package fr.cpe.model.installation;

import java.util.List;
import fr.cpe.model.consommable.IConsommable;

public class Urinoir extends AbstractInstallation {
    private static final double PRIX_URINOIR = 0.5;

    public Urinoir(List<IConsommable> lesConsommables) {
        super(lesConsommables);
    }

    @Override
    public double getPrix() {
        return PRIX_URINOIR;
    }

    @Override
    public String getDescription() {
        return "Urinoir";
    }
}

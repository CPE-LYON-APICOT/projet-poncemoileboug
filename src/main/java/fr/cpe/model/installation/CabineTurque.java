package fr.cpe.model.installation;

import java.util.List;
import fr.cpe.model.consommable.IConsommable;

public class CabineTurque extends AbstractInstallation {
    private static final double PRIX_TURQUE = 1.00;

    public CabineTurque(List<IConsommable> lesConsommables) {
        super(lesConsommables);
    }

    @Override
    public double getPrix() {
        return PRIX_TURQUE;
    }

    @Override
    public String getDescription() {
        return "Cabine turque";
    }
}

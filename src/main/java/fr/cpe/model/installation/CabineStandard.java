package fr.cpe.model.installation;

import java.util.List;

import fr.cpe.model.consommable.Consommable;

public class CabineStandard extends AbstractInstallation {
    private static final double PRIX_STANDARD = 1.50;

    public CabineStandard(List<Consommable> lesConsommables) {
        super(lesConsommables);
    }

    @Override
    public double getPrix() {
        return PRIX_STANDARD;
    }

    @Override
    public String getDescription() {
        return "Cabine Standard";
    }
}

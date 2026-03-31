package fr.cpe.service;

import java.util.Arrays;
import java.util.List;

import fr.cpe.model.consommable.Consommable;
import fr.cpe.model.consommable.CubeDesodorisant;
import fr.cpe.model.consommable.PapierToilette;
import fr.cpe.model.consommable.Savon;
import fr.cpe.model.consommable.Shampoing;
import fr.cpe.model.installation.CabineStandard;
import fr.cpe.model.installation.Douche;
import fr.cpe.model.installation.Installation;
import fr.cpe.model.installation.Urinoir;
import fr.cpe.model.installation.decorator.LumiereDecorator;
import fr.cpe.model.installation.decorator.VipDecorator;

public class InstallationFactory {

    /**
     * Crée une cabine standard avec ses consommables par défaut
     */
    public static Installation createCabineStandard() {
        var consommables = Arrays.asList(
            new PapierToilette(10, 5),
            new Savon(10, 5),
            new CubeDesodorisant(10, 5)
        );
        return new CabineStandard(consommables);
    }

    /**
     * Crée une douche avec ses consommables
     */
    public static Installation createDouche() {
        var consommables = Arrays.asList(
            new Shampoing(80, 10),
            new Savon(60, 10)
        );
        return new Douche(consommables, "Douche publique");
    }

    /**
     * Crée un urinoir avec lavabo optionnel
     */
    public static Installation createUrinoir(boolean hasLavabo) {
        List<Consommable> consommables = Arrays.asList(
            new CubeDesodorisant(40, 8)
        );
        return new Urinoir(consommables);
    }

    /**
     * Factory pour créer une cabine avec options VIP + LED
     */
    public static Installation createCabineVipLED() {
        Installation base = createCabineStandard();
        Installation vip = new VipDecorator(base);
        return new LumiereDecorator(vip);
    }
}
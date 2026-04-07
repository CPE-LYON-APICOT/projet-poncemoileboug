package fr.cpe.model.installation;

import java.util.List;

import fr.cpe.model.consommable.IConsommable;

/**
 * Classe représentant une installation de type cabine standard.
 * <p>
 * Cette installation possède un prix fixe et une description standard,
 * ainsi qu'une liste de consommables associés.
 */
public class CabineStandard extends AbstractInstallation {

    /**
     * Prix fixe pour l'utilisation d'une cabine standard.
     */
    private static final double PRIX_STANDARD = 1.50;

    /**
     * Constructeur de la classe CabineStandard.
     *
     * @param lesConsommables liste des consommables associés à la cabine
     */
    public CabineStandard(List<IConsommable> lesConsommables) {
        super(lesConsommables);
    }

    /**
     * Retourne le prix d'utilisation de la cabine standard.
     *
     * @return le prix fixe
     */
    @Override
    public double getPrix() {
        return PRIX_STANDARD;
    }

    /**
     * Retourne la description de l'installation.
     *
     * @return une chaîne décrivant l'installation ("Cabine Standard")
     */
    @Override
    public String getDescription() {
        return "Cabine Standard";
    }
}
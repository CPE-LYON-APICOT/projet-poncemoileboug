package fr.cpe.model.installation;

import java.util.List;

import fr.cpe.model.consommable.IConsommable;

/**
 * Classe représentant une installation de type cabine turque.
 * <p>
 * Cette installation possède un prix fixe et une description standard,
 * ainsi qu'une liste de consommables associés.
 */
public class CabineTurque extends AbstractInstallation {

    /**
     * Prix fixe pour l'utilisation d'une cabine turque.
     */
    private static final double PRIX_TURQUE = 1.00;

    /**
     * Constructeur de la classe CabineTurque.
     *
     * @param lesConsommables liste des consommables associés à la cabine
     */
    public CabineTurque(List<IConsommable> lesConsommables) {
        super(lesConsommables);
    }

    /**
     * Retourne le prix d'utilisation de la cabine turque.
     *
     * @return le prix fixe
     */
    @Override
    public double getPrix() {
        return PRIX_TURQUE;
    }

    /**
     * Retourne la description de l'installation.
     *
     * @return une chaîne décrivant l'installation ("Cabine turque")
     */
    @Override
    public String getDescription() {
        return "Cabine turque";
    }
}
package fr.cpe.model.installation;

import java.util.List;

import fr.cpe.model.consommable.IConsommable;

/**
 * Classe représentant une installation de type urinoir.
 * <p>
 * Un urinoir est une installation sanitaire avec un prix fixe
 * et une liste de consommables associés.
 */
public class Urinoir extends AbstractInstallation {

    /**
     * Prix fixe pour l'utilisation d'un urinoir.
     */
    private static final double PRIX_URINOIR = 0.5;

    /**
     * Constructeur de la classe Urinoir.
     *
     * @param lesConsommables liste des consommables associés à l'urinoir
     */
    public Urinoir(List<IConsommable> lesConsommables) {
        super(lesConsommables);
    }

    /**
     * Retourne le prix d'utilisation de l'urinoir.
     *
     * @return le prix fixe de l'urinoir
     */
    @Override
    public double getPrix() {
        return PRIX_URINOIR;
    }

    /**
     * Retourne la description de l'installation.
     *
     * @return une chaîne décrivant l'installation ("Urinoir")
     */
    @Override
    public String getDescription() {
        return "Urinoir";
    }
}
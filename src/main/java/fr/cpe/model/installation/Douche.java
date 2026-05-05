package fr.cpe.model.installation;

import java.util.List;

import fr.cpe.model.consommable.IConsommable;

/**
 * Classe représentant une installation de type douche.
 * Une douche possède un prix fixe et une description personnalisable,
 * ainsi qu'une liste de consommables associés.
 */
public class Douche extends AbstractInstallation {

    /**
     * Prix fixe pour l'utilisation d'une douche.
     */
    private static final double PRIX_DOUCHE = 3.00;

    /**
     * Description de la douche (ex : type, localisation, etc.).
     */
    private final String description;

    /**
     * Constructeur de la classe Douche.
     *
     * @param lesConsommables liste des consommables associés à la douche
     * @param description description spécifique de la douche
     */
    public Douche(List<IConsommable> lesConsommables, String description) {
        super(lesConsommables);
        this.description = description;
    }

    /**
     * Retourne le prix d'utilisation de la douche.
     *
     * @return le prix fixe de la douche
     */
    @Override
    public double getPrix() {
        return PRIX_DOUCHE;
    }

    /**
     * Retourne la description de la douche.
     *
     * @return la description personnalisée
     */
    @Override
    public String getDescription() {
        return description;
    }
}

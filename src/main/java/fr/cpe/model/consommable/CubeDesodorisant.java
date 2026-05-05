package fr.cpe.model.consommable;

/**
 * Représente un cube désodorisant utilisé dans les installations.
 * Cette classe permet de suivre le stock et pourra permettre de définir un seuil d'alerte par la suite.
 */
public class CubeDesodorisant implements IConsommable {
    private final String nom = "Cube desodorisant";
    private int quantite;
    private final int seuilAlerte;

    /**
     * @param quantite Niveau de stock initial.
     * @param seuilAlerte Seuil minimum avant le déclenchement d'une alerte de recharge.
     */
    public CubeDesodorisant(int quantite, int seuilAlerte) {
        this.quantite = quantite;
        this.seuilAlerte = seuilAlerte;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public int getQuantite() {
        return quantite;
    }

    @Override
    public int getSeuilAlerte() {
        return seuilAlerte;
    }

    /**
     * Met à jour la quantité en stock.
     * @throws IllegalArgumentException si la quantité est inférieure ou égale à zéro.
     */
    @Override
    public void setQuantite(int quantite) {
        if (quantite <= 0){
            throw new IllegalArgumentException("La quantité de cube desodorisant doit être supérieure à 0.");
        }
        this.quantite = quantite;
    }
}

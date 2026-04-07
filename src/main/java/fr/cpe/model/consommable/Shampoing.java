package fr.cpe.model.consommable;

/**
 * Représente le consommable Shampoing.
 * Gère les stocks de produits capillaires mis à disposition dans les installations.
 */
public class Shampoing implements IConsommable {
    private final String nom = "Shampoing";
    private int quantite;
    private final int seuilAlerte;

    /**
     * @param quantite Volume ou nombre d'unités de shampoing initial.
     * @param seuilAlerte Niveau de stock en dessous duquel une alerte est générée.
     */
    public Shampoing(int quantite, int seuilAlerte) {
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
     * Met à jour la quantité de shampoing disponible.
     * @throws IllegalArgumentException si la quantité est inférieure ou égale à zéro.
     */
    @Override
    public void setQuantite(int quantite) {
        if (quantite <= 0){
            throw new IllegalArgumentException("La quantité de Shampoing doit être supérieure à 0.");
        }
        this.quantite = quantite;
    }
}

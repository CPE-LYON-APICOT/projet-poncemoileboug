package fr.cpe.model.consommable;

/**
 * Représente un produit désodorisant classique.
 * Gère l'état du stock et le seuo d'alerte pour le réapprovisionnement.
 */
public class Desodorisant implements IConsommable {
    private final String nom = "Desodorisant";
    private int quantite;
    private final int seuilAlerte;

    /**
     * @param quantite Stock initial disponible.
     * @param seuilAlerte Limite basse avant déclenchement d'une alerte.
     */
    public Desodorisant(int quantite, int seuilAlerte) {
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
     * Modifie la quantité en stock.
     * @throws IllegalArgumentException si la quantité fournie est négative ou nulle.
     */
    @Override
    public void setQuantite(int quantite) {
        if (quantite <= 0){
            throw new IllegalArgumentException("La quantité de cube desodorisant doit être supérieure à 0.");
        }
        this.quantite = quantite;
    }
}

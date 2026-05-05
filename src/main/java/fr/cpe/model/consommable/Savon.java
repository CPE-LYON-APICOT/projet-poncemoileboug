package fr.cpe.model.consommable;

/**
 * Représente le consommable Savon.
 * Gère les stocks de savon pour les installations.
 */
public class Savon implements IConsommable {
    private final String nom = "Savon";
    private int quantite;
    private final int seuilAlerte;

    /**
     * @param quantite Volume ou nombre d'unités de savon initial.
     * @param seuilAlerte Seuil de stock en dessous duquel une alerte est levée.
     */
    public Savon(int quantite, int seuilAlerte) {
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
     * Met à jour la quantité de savon.
     * @throws IllegalArgumentException si la quantité est inférieure ou égale à zéro.
     */
    @Override
    public void setQuantite(int quantite) {
        if (quantite <= 0){
            throw new IllegalArgumentException("La quantité de savon doit être supérieure à 0.");
        }
        this.quantite = quantite;
    }
}

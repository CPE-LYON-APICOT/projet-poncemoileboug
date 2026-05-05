package fr.cpe.model.consommable;

/**
 * Représente le consommable Papier Toilette.
 * Cette classe permet de gérer le stock spécifique aux rouleaux de papier.
 */
public class PapierToilette implements IConsommable {
    private final String nom = "Papier toilette"; // le papier toilette s'appellera tjrs papier toilette
    private int quantite;
    private final int seuilAlerte;

    /**
     * @param quantite Nombre de rouleaux initialement en stock.
     * @param seuilAlerte Seuil critique déclenchant une alerte de réapprovisionnement.
     */
    public PapierToilette(int quantite, int seuilAlerte) {
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
     * Met à jour le nombre de rouleaux disponibles.
     * @throws IllegalArgumentException si la quantité est inférieure ou égale à zéro.
     */
    @Override
    public void setQuantite(int quantite) {
        if (quantite <= 0){
            throw new IllegalArgumentException("La quantité de papier toilette doit être supérieure à 0.");
        }
        this.quantite = quantite;
    }
}

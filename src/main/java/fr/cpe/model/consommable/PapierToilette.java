package fr.cpe.model.consommable;

public class PapierToilette implements Consommable {
    private final String nom = "Papier toilette"; // le papier toilette s'appellera tjrs papier toilette
    private int quantite;
    private final int seuilAlerte;

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

    @Override
    public void setQuantite(int quantite) {
        if (quantite <= 0){
            throw new IllegalArgumentException("La quantité de papier toilette doit être supérieure à 0.");
        }
        this.quantite = quantite;
    }
}

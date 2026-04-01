package fr.cpe.model.consommable;

public class Desodorisant implements IConsommable {
    private final String nom = "Desodorisant";
    private int quantite;
    private final int seuilAlerte;

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

    @Override
    public void setQuantite(int quantite) {
        if (quantite <= 0){
            throw new IllegalArgumentException("La quantité de cube desodorisant doit être supérieure à 0.");
        }
        this.quantite = quantite;
    }
}

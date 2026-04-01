package fr.cpe.model.consommable;

public class Savon implements IConsommable {
    private final String nom = "Savon";
    private int quantite;
    private final int seuilAlerte;

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

    @Override
    public void setQuantite(int quantite) {
        if (quantite <= 0){
            throw new IllegalArgumentException("La quantité de savon doit être supérieure à 0.");
        }
        this.quantite = quantite;
    }
}

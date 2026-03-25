package fr.cpe.model.consommable;

public interface Consommable {
    String getNom();
    int getQuantite();
    int getSeuilAlerte();

    void setQuantite(int quantite);
}

package fr.cpe.model.consommable;

public interface IConsommable {
    String getNom();
    int getQuantite();
    int getSeuilAlerte();

    void setQuantite(int quantite);
}

package fr.cpe.model.consommable;

/**
 * Interface définissant le contrat pour tous les objets consommables du système.
 * Un consommable possède un nom, une quantité en stock et un seuil d'alerte.
 */
public interface IConsommable {

    /**
     * @return Le nom unique du consommable (ex: "Savon", "Shampoing").
     */
    String getNom();

    /**
     * @return La quantité actuelle disponible en stock.
     */
    int getQuantite();

    /**
     * @return Le niveau de stock minimum avant de déclencher une alerte de réapprovisionnement.
     */
    int getSeuilAlerte();

    /**
     * Définit la nouvelle quantité en stock.
     * @param quantite La nouvelle valeur de stock (doit être positive).
     * @throws IllegalArgumentException si la quantité ne respecte pas les règles métier.
     */
    void setQuantite(int quantite);
}

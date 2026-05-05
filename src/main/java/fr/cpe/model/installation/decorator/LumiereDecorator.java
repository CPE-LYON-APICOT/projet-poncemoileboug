package fr.cpe.model.installation.decorator;

import fr.cpe.model.installation.IInstallation;

/**
 * Décorateur ajoutant une option d'éclairage spécifique à une installation.
 * Améliore l'ambiance visuelle tout en mettant à jour le coût énergétique.
 */
public class LumiereDecorator extends InstallationDecorator {

    /**
     * @param decorated L'installation de base à laquelle on ajoute l'option Lumière.
     */
    public LumiereDecorator(IInstallation decorated) {
        super(decorated);
    }

    /**
     * Calcule le prix en ajoutant le coût de l'installation lumineuse.
     * @return Le prix total incluant le surcoût de 0.5 pour la consommation électrique.
     */
    @Override
    public double getPrix() {
        return decorated.getPrix() + 0.5;
    }

    /**
     * @return La description enrichie avec les détails de l'éclairage LED.
     */
    @Override
    public String getDescription() {
        return decorated.getDescription() + " + Option Lumière : Éclairage LED dynamique et apaisant";
    }
}

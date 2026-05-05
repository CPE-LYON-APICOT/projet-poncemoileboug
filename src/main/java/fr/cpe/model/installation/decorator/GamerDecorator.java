package fr.cpe.model.installation.decorator;

import fr.cpe.model.installation.IInstallation;

/**
 * Décorateur ajoutant une thématique "Gamer" à une installation.
 * Ajoute des équipements technologiques (LED, console) et ajuste le coût en conséquence.
 */
public class GamerDecorator extends InstallationDecorator {

    /**
     * @param decorated L'installation de base sur laquelle appliquer le thème Gamer.
     */
    public GamerDecorator(IInstallation decorated) {
        super(decorated);
    }

    /**
     * Calcule le prix en ajoutant le coût de l'option.
     * Note : Le surcoût de 3.5 inclut 0.5 pour la consommation électrique supplémentaire.
     * @return Le prix total (base + 3.5).
     */
    @Override
    public double getPrix() {
        return decorated.getPrix() + 3.5;
    }

    /**
     * @return La description enrichie avec les équipements Gamer.
     */
    @Override
    public String getDescription() {
        return decorated.getDescription() + " + Option Gamer : Éclairage LED dynamique et console de jeux";
    }
}

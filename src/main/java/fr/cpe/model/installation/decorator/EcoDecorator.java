package fr.cpe.model.installation.decorator;

import fr.cpe.model.installation.IInstallation;

/**
 * Décorateur ajoutant une option éco-responsable à une installation.
 * Ce décorateur modifie le prix et la description pour inclure des éléments écologiques.
 */
public class EcoDecorator extends InstallationDecorator {

    /**
     * @param decorated L'installation de base à laquelle on ajoute l'option Éco.
     */
    public EcoDecorator(IInstallation decorated) {
        super(decorated);
    }

    /**
     * Calcule le prix total en ajoutant le surcoût de l'option Éco.
     * @return Le prix de l'installation de départ + 7.50€.
     */
    @Override
    public double getPrix() {
        return decorated.getPrix() + 7.5;
    }

    /**
     * @return La description originale complétée par les détails de l'option Éco.
     */
    @Override
    public String getDescription() {
        return decorated.getDescription() + " + Option Éco-responsable : Papier recyclé, savon bio, lumière solaire";
    }
}

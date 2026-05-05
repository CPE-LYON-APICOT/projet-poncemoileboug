package fr.cpe.model.installation.decorator;

import fr.cpe.model.installation.IInstallation;

/**
 * Décorateur appliquant la thématique de l'Olympique Lyonnais à une installation.
 * Personnalise l'ambiance visuelle et sonore aux couleurs du club.
 */
public class OlDecorator extends InstallationDecorator {

    /**
     * @param decorated L'installation de base à personnaliser avec le thème OL.
     */
    public OlDecorator(IInstallation decorated) {
        super(decorated);
    }

    /**
     * Calcule le prix en ajoutant les frais liés au thème OL.
     * Le surcoût de 1.50 inclut les frais de licence et les équipements spécifiques.
     * @return Le prix total (base + 1.50).
     */
    @Override
    public double getPrix() {
        return decorated.getPrix() + 1.50;
    }

    /**
     * @return La description complétée par les détails du thème (couleurs et hymne).
     */
    @Override
    public String getDescription() {
        return decorated.getDescription() + " + Thème OL : Rouge & Bleu, hymne du club au démarrage";
    }
}

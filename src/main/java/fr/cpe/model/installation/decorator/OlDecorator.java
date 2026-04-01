package fr.cpe.model.installation.decorator;

import fr.cpe.model.installation.IInstallation;

public class OlDecorator extends InstallationDecorator {

    public OlDecorator(IInstallation decorated) {
        super(decorated);
    }

    @Override
    public double getPrix() {
        return decorated.getPrix() + 1.50; // +1.5 pour la licence et les équipements OL
    }

    @Override
    public String getDescription() {
        return decorated.getDescription() + " + Thème OL : Rouge & Bleu, hymne du club au démarrage";
    }
}

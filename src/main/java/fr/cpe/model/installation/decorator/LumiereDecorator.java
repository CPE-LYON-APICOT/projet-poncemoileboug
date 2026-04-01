package fr.cpe.model.installation.decorator;

import fr.cpe.model.installation.IInstallation;

public class LumiereDecorator extends InstallationDecorator {

    public LumiereDecorator(IInstallation decorated) {
        super(decorated);
    }

    @Override
    public double getPrix() {
        return decorated.getPrix() + 0.5; //+ 0.5 pour la conso élec
    }

    @Override
    public String getDescription() {
        return decorated.getDescription() + " + Option Lumière : Éclairage LED dynamique et apaisant";
    }
}

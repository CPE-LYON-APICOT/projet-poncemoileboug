package fr.cpe.model.installation.decorator;

import fr.cpe.model.installation.IInstallation;

public class GamerDecorator extends InstallationDecorator {
    public GamerDecorator(IInstallation decorated) {
        super(decorated);
    }

    @Override
    public double getPrix() {
        return decorated.getPrix() + 3.5; //+ 0.5 pour la conso élec
    }

    @Override
    public String getDescription() {
        return decorated.getDescription() + " + Option Gamer : Éclairage LED dynamique et console de jeux";
    }
}

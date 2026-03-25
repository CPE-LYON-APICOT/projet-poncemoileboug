package fr.cpe.model.installation.decorator;

import fr.cpe.model.installation.Installation;

public class LumiereDecorator extends InstallationDecorator {

    public LumiereDecorator(Installation decorated) {
        super(decorated);
    }

    @Override
    public double getPrix() {
        return decorated.getPrix() + 0.50; // Prix de base + 0.50€ pour la consommation électrique des LED
    }

    @Override
    public String getDescription() {
        return decorated.getDescription() + " + Option Lumière : Éclairage LED dynamique et apaisant";
    }
}

package fr.cpe.model.installation.decorator;

import fr.cpe.model.installation.Installation;

public class EcoDecorator extends InstallationDecorator {
        public EcoDecorator(Installation decorated) {
        super(decorated);
    }

    @Override
    public double getPrix() {
        return decorated.getPrix() + 7.5;
    }

    @Override
    public String getDescription() {
        return decorated.getDescription() + " + Option Éco-responsable : Papier recyclé, savon bio, lumière solaire";
    }
}

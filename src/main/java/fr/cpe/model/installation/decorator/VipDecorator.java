package fr.cpe.model.installation.decorator;

import fr.cpe.model.installation.Installation;

public class VipDecorator extends InstallationDecorator {

    public VipDecorator(Installation decorated) {
        super(decorated);
    }

    @Override
    public double getPrix() {
        return decorated.getPrix() + 2.0; // On délègue à l'objet décoré et on ajoute la spécificité VIP
    }

    @Override
    public String getDescription() {
        return decorated.getDescription() + " + Option VIP : Musique d'ambiance et finitions premium"; // Ajout dans la descirption
    }
}

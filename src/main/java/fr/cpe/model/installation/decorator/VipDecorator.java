package fr.cpe.model.installation.decorator;

import fr.cpe.model.installation.IInstallation;

/**
 * Décorateur ajoutant une option "VIP" à une installation.
 * Ce décorateur offre une expérience haut de gamme avec des services exclusifs.
 */
public class VipDecorator extends InstallationDecorator {

    /**
     * @param decorated L'installation de base à laquelle on ajoute les privilèges VIP.
     */
    public VipDecorator(IInstallation decorated) {
        super(decorated);
    }

    /**
     * Calcule le prix en déléguant à l'objet décoré et en ajoutant la spécificité VIP.
     * @return Le prix total incluant le surcoût de 2.0 pour les finitions premium.
     */
    @Override
    public double getPrix() {
        return decorated.getPrix() + 2.0;
    }

    /**
     * @return La description enrichie avec les détails de l'option VIP (musique et finitions).
     */
    @Override
    public String getDescription() {
        return decorated.getDescription() + " + Option VIP : Musique d'ambiance et finitions premium";
    }
}

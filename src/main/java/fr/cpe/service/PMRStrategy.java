package fr.cpe.service;

/**
 * Stratégie de paiement à tarif réduit pour les personnes à mobilité réduite (PMR).
 */
public class PMRStrategy implements IPaymentStrategy {

    /**
     * Valide le paiement PMR sans vérification particulière.
     *
     * @param amount le montant à régler
     * @return toujours {@code true}
     */
    @Override
    public boolean processPayment(double amount) {
        System.out.println("[PMR] Paiement de " + amount + "€ accepté.");
        return true;
    }
}
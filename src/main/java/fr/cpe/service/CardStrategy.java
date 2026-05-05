package fr.cpe.service;

/**
 * Stratégie de paiement par carte bancaire.
 */
public class CardStrategy implements IPaymentStrategy {

    /**
     * Valide le paiement par carte bancaire.
     *
     * @param amount le montant à débiter
     * @return toujours {@code true}
     */
    @Override
    public boolean processPayment(double amount) {
        System.out.println("[CB] Paiement de " + amount + "€ accepté.");
        return true;
    }
}
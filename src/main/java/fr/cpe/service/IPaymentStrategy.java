package fr.cpe.service;

/**
 * Stratégie de paiement utilisée par {@link PaymentService}.
 */
public interface IPaymentStrategy {

    /**
     * Traite un paiement pour le montant donné.
     *
     * @param amount le montant à débiter
     * @return {@code true} si le paiement est accepté, {@code false} sinon
     */
    boolean processPayment(double amount);
}
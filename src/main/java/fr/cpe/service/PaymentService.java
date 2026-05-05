package fr.cpe.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Service de paiement utilisant le pattern Strategy.
 * La stratégie peut être changée dynamiquement selon le mode choisi par l'utilisateur.
 */
@Singleton
public class PaymentService {

    private IPaymentStrategy strategy;

    @Inject
    public PaymentService(IPaymentStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Remplace la stratégie de paiement courante.
     *
     * @param strategy la nouvelle stratégie à utiliser
     */
    public void setStrategy(IPaymentStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Délègue le paiement à la stratégie courante.
     *
     * @param amount le montant à débiter
     * @return {@code true} si le paiement est accepté, {@code false} sinon
     */
    public boolean processPayment(double amount) {
        return strategy.processPayment(amount);
    }
}
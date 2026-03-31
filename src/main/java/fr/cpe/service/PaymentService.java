package fr.cpe.service;

import com.google.inject.Inject;

public class PaymentService {

    private PaymentStrategy strategy;

    @Inject
    public PaymentService(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    // Permet de changer la stratégie à la volée (depuis l'UI JavaFX)
    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean processPayment(double amount) {
        return strategy.processPayment(amount);
    }
}

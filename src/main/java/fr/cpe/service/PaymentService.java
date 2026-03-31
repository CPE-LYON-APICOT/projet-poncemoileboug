package fr.cpe.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PaymentService {

    private IPaymentStrategy strategy;

    @Inject
    public PaymentService(IPaymentStrategy strategy) {
        this.strategy = strategy;
    }

    // Permet de changer la stratégie à la volée (depuis l'UI JavaFX)
    public void setStrategy(IPaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean processPayment(double amount) {
        return strategy.processPayment(amount);
    }
}

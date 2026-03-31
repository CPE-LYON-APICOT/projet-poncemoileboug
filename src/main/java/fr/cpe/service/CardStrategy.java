package fr.cpe.service;

public class CardStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("[CB] Paiement de " + amount + "€ accepté.");
        return true;
    }
}

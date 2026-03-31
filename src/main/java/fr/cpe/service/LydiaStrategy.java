package fr.cpe.service;

public class LydiaStrategy implements IPaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("[Lydia] Paiement de " + amount + "€ accepté.");
        return true;
    }
}

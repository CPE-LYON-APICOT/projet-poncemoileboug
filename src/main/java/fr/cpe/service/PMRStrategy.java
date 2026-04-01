package fr.cpe.service;

public class PMRStrategy implements IPaymentStrategy {
    @Override
    public boolean processPayment(double amount) {

        System.out.println("[PMR] Paiement de " + amount + "€ accepté.");
        return true;
    }
}

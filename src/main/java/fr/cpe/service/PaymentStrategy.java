package fr.cpe.service;

public interface PaymentStrategy {
    boolean processPayment(double amount);
}

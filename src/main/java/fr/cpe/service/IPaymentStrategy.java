package fr.cpe.service;

public interface IPaymentStrategy {
    boolean processPayment(double amount);
}

package fr.cpe.service;

import java.util.HashMap;
import java.util.Map;

import fr.cpe.model.user.User;
import fr.cpe.service.payment.CardStrategy;
import fr.cpe.service.payment.LydiaStrategy;
import fr.cpe.service.payment.PaymentException;
import fr.cpe.service.payment.PaymentStrategy;

/**
 * Service de paiement — gère les différentes méthodes de paiement via le pattern Strategy.
 *
 * <p>Le service coordonne les appels aux différentes stratégies de paiement
 * en fonction du choix de l'utilisateur (Lydia, Carte, etc.).</p>
 */
public class PaymentService {
    
    private final Map<String, PaymentStrategy> strategies = new HashMap<>();

    public PaymentService() {
        this.strategies.put("lydia", new LydiaStrategy());
        this.strategies.put("card", new CardStrategy());
    }

    /**
     * Traite un paiement selon la méthode choisie.
     *
     * @param user   L'utilisateur qui paye
     * @param amount Le montant à payer
     * @param method La méthode de paiement ("lydia" ou "card")
     * @throws IllegalArgumentException Si la méthode n'existe pas
     */
    public void processPayment(User user, double amount, String method) {
        PaymentStrategy strategy = strategies.get(method.toLowerCase());
        
        if (strategy == null) {
            throw new IllegalArgumentException("méthode de paiement inconnue: " + method);
        }

        try {
            strategy.processPayment(amount, user.getPaymentInfo());
            user.addTransaction(amount, method);
        } catch (PaymentException e) {
            System.err.println("err paiement: " + e.getMessage());
            throw new RuntimeException("le paiement a échoué: " + e.getMessage(), e);
        }
    }
}
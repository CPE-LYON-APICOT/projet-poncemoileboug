package fr.cpe.service.payment;

/**
 * Implémentation de paiement par Carte Bancaire.
 * 
 * <p>Effectue les paiements via une API de paiement par carte
 * (Stripe, PayPal, etc.).</p>
 */
public class CardStrategy implements PaymentStrategy {

    @Override
    public void processPayment(double amount, String cardToken) throws PaymentException {
        System.out.println("paiement par CB : " + amount + "€");
        
        if (amount <= 0) {
            throw new PaymentException("montant invalide : " + amount);
        }
        if (cardToken == null || cardToken.isBlank()) {
            throw new PaymentException("pas de token");
        }
        
        System.out.println("paiement OK");
    }
}

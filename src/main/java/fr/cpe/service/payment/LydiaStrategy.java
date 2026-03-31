package fr.cpe.service.payment;

/**
 * Implémentation de paiement par Lydia.
 * 
 * <p>Effectue les paiements via l'API Lydia (app de paiement entre amis).</p>
 */
public class LydiaStrategy implements PaymentStrategy {

    @Override
    public void processPayment(double amount, String accountInfo) throws PaymentException {
        System.out.println("paiement Lydia : " + amount + "€ vers " + accountInfo);
        
        if (amount <= 0) {
            throw new PaymentException("montant invalide : " + amount);
        }
        if (accountInfo == null || accountInfo.isBlank()) {
            throw new PaymentException("informations lydia manquantes");
        }
        
        System.out.println("paiement lydia ok");
    }
}

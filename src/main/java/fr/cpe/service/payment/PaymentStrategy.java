package fr.cpe.service.payment;

/**
 * Interface Strategy pour les différentes méthodes de paiement.
 * 
 * <p>Chaque implémentation gère un système de paiement spécifique
 * (Lydia, Carte bancaire, etc.) de manière indépendante.</p>
 */
public interface PaymentStrategy {

    /**
     * Traite un paiement selon la méthode implémentée.
     *
     * @param amount      Montant à payer
     * @param accountInfo Informations de compte (token, numéro, etc.)
     * @throws PaymentException En cas d'erreur lors du paiement
     */
    void processPayment(double amount, String accountInfo) throws PaymentException;
}

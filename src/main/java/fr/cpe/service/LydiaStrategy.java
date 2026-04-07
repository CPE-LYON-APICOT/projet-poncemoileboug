/**
 * Stratégie de paiement via Lydia.
 * <p>
 * Implémente {@link IPaymentStrategy} pour traiter les paiements
 * en utilisant le service Lydia.
 * </p>
 *
 * @see IPaymentStrategy
 */
package fr.cpe.service;
public class LydiaStrategy implements IPaymentStrategy {

    /**
     * Traite un paiement via Lydia pour le montant spécifié.
     * <p>
     * Cette implémentation accepte systématiquement le paiement
     * et affiche un message de confirmation en console.
     * </p>
     *
     * @param amount le montant à débiter en euros, doit être un nombre positif
     * @return {@code true} si le paiement a été accepté, {@code false} sinon
     */
    @Override
    public boolean processPayment(double amount) {
        System.out.println("[Lydia] Paiement de " + amount + "€ accepté.");
        return true;
    }
}
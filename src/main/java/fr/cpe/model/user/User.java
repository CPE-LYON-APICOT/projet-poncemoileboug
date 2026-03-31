package fr.cpe.model.user;

/**
 * Représente un utilisateur du système (client ou administrateur).
 *
 * <p>Les utilisateurs peuvent faire des réservations et consulter l'état
 * des installations via un compte personnel.</p>
 */
public class User {

    private final String userId;
    private String name;
    private String email;
    private String phone;
    private String paymentInfo;  // Token de paiement, IBAN, etc.
    private boolean isAdmin;
    private double balance;  // Pour un système de crédit/prépayé

    /**
     * Crée un nouvel utilisateur client.
     */
    public User(String userId, String name, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.isAdmin = false;
        this.balance = 0.0;
    }

    /**
     * Crée un utilisateur avec informations complètes.
     */
    public User(String userId, String name, String email, String phone, 
                String paymentInfo, boolean isAdmin) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.paymentInfo = paymentInfo;
        this.isAdmin = isAdmin;
        this.balance = 0.0;
    }

    // ========== Getters ==========

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public double getBalance() {
        return balance;
    }

    // ========== Setters ==========

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setBalance(double balance) {
        this.balance = Math.max(0, balance); // Solde jamais négatif
    }

    // ========== Méthodes métier ==========

    /**
     * Enregistre une transaction de paiement.
     */
    public void addTransaction(double amount, String method) {
        // Log ou enregistrement dans la base de données
        System.out.println("[TRANSACTION] " + this.name + " a payé " + amount 
                          + "€ via " + method);
    }

    /**
     * Crédite le compte de l'utilisateur (pour un système de prépayé).
     */
    public void creditBalance(double amount) {
        this.balance += amount;
        System.out.println("[CREDIT] " + this.name + " a crédité " + amount 
                          + "€. Solde actuel: " + this.balance + "€");
    }

    /**
     * Débite le compte de l'utilisateur.
     * @throws IllegalArgumentException si le solde est insuffisant
     */
    public void debitBalance(double amount) {
        if (this.balance < amount) {
            throw new IllegalArgumentException("Solde insuffisant. Vous avez " 
                                               + this.balance + "€, demandé " 
                                               + amount + "€");
        }
        this.balance -= amount;
        System.out.println("[DEBIT] " + this.name + " a débité " + amount 
                          + "€. Solde restant: " + this.balance + "€");
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", balance=" + balance +
                '}';
    }
}

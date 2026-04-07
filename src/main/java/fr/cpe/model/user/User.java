package fr.cpe.model.user;

/**
 * Représente un utilisateur du système (client ou administrateur).
 *
 * <p>Les utilisateurs peuvent faire des réservations et consulter l'état
 * des installations via un compte personnel.</p>
 */
public class User {

    /** Identifiant unique et immuable de l'utilisateur. */
    private final String userId;

    /** Nom complet de l'utilisateur. */
    private String name;

    /** Adresse e-mail de l'utilisateur. */
    private String email;

    /** Numéro de téléphone de l'utilisateur. */
    private String phone;

    /** Informations de paiement associées (token, IBAN, etc.). */
    private String paymentInfo;

    /** Indique si l'utilisateur possède des droits administrateur. */
    private boolean isAdmin;

    /** Solde du compte prépayé de l'utilisateur, toujours positif ou nul. */
    private double balance;

    /**
     * Crée un nouvel utilisateur client avec les informations de base.
     * <p>
     * Le compte est créé sans droits administrateur et avec un solde nul.
     * </p>
     *
     * @param userId identifiant unique de l'utilisateur
     * @param name   nom complet de l'utilisateur
     * @param email  adresse e-mail de l'utilisateur
     * @param phone  numéro de téléphone de l'utilisateur
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
     * Crée un utilisateur avec des informations complètes.
     * <p>
     * Permet de spécifier les informations de paiement et le rôle
     * administrateur dès la création. Le solde initial est toujours nul.
     * </p>
     *
     * @param userId      identifiant unique de l'utilisateur
     * @param name        nom complet de l'utilisateur
     * @param email       adresse e-mail de l'utilisateur
     * @param phone       numéro de téléphone de l'utilisateur
     * @param paymentInfo informations de paiement (token, IBAN, etc.)
     * @param isAdmin     {@code true} si l'utilisateur est administrateur
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

    /**
     * Retourne l'identifiant unique de l'utilisateur.
     *
     * @return l'identifiant utilisateur
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Retourne le nom complet de l'utilisateur.
     *
     * @return le nom de l'utilisateur
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne l'adresse e-mail de l'utilisateur.
     *
     * @return l'e-mail de l'utilisateur
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retourne le numéro de téléphone de l'utilisateur.
     *
     * @return le téléphone de l'utilisateur
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Retourne les informations de paiement de l'utilisateur.
     *
     * @return le token ou l'IBAN associé au compte
     */
    public String getPaymentInfo() {
        return paymentInfo;
    }

    /**
     * Indique si l'utilisateur possède des droits administrateur.
     *
     * @return {@code true} si l'utilisateur est administrateur, {@code false} sinon
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Retourne le solde actuel du compte prépayé de l'utilisateur.
     *
     * @return le solde en euros, toujours supérieur ou égal à zéro
     */
    public double getBalance() {
        return balance;
    }

    // ========== Setters ==========

    /**
     * Met à jour le nom complet de l'utilisateur.
     *
     * @param name le nouveau nom de l'utilisateur
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Met à jour l'adresse e-mail de l'utilisateur.
     *
     * @param email la nouvelle adresse e-mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Met à jour le numéro de téléphone de l'utilisateur.
     *
     * @param phone le nouveau numéro de téléphone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Met à jour les informations de paiement de l'utilisateur.
     *
     * @param paymentInfo le nouveau token ou IBAN de paiement
     */
    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    /**
     * Modifie le statut administrateur de l'utilisateur.
     *
     * @param admin {@code true} pour accorder les droits administrateur,
     *              {@code false} pour les révoquer
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    /**
     * Définit le solde du compte prépayé de l'utilisateur.
     * <p>
     * Si la valeur fournie est négative, le solde est ramené à zéro
     * afin de garantir qu'il ne soit jamais négatif.
     * </p>
     *
     * @param balance le nouveau solde en euros
     */
    public void setBalance(double balance) {
        this.balance = Math.max(0, balance); // Solde jamais négatif
    }

    // ========== Méthodes métier ==========

    /**
     * Enregistre et affiche une transaction de paiement effectuée par l'utilisateur.
     *
     * @param amount le montant payé en euros
     * @param method le moyen de paiement utilisé (ex. : "Lydia", "CB", "IBAN")
     */
    public void addTransaction(double amount, String method) {
        // Log ou enregistrement dans la base de données
        System.out.println("[TRANSACTION] " + this.name + " a payé " + amount
                          + "€ via " + method);
    }

    /**
     * Crédite le compte prépayé de l'utilisateur du montant spécifié.
     * <p>
     * Affiche un message de confirmation avec le nouveau solde.
     * </p>
     *
     * @param amount le montant à créditer en euros, doit être positif
     */
    public void creditBalance(double amount) {
        this.balance += amount;
        System.out.println("[CREDIT] " + this.name + " a crédité " + amount
                          + "€. Solde actuel: " + this.balance + "€");
    }

    /**
     * Débite le compte prépayé de l'utilisateur du montant spécifié.
     * <p>
     * Affiche un message de confirmation avec le solde restant.
     * </p>
     *
     * @param amount le montant à débiter en euros, doit être positif
     * @throws IllegalArgumentException si le solde est insuffisant pour couvrir le débit
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

    /**
     * Retourne une représentation textuelle de l'utilisateur.
     * <p>
     * Inclut l'identifiant, le nom, l'e-mail, le statut administrateur
     * et le solde actuel. Les informations de paiement sont exclues
     * pour des raisons de sécurité.
     * </p>
     *
     * @return une chaîne décrivant l'utilisateur
     */
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
package fr.cpe.service;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import fr.cpe.model.EtatInstallation;
import fr.cpe.model.installation.AbstractInstallation;
import fr.cpe.model.installation.IInstallation;

/**
 * Service central de gestion de la carte des installations sanitaires de Lyon.
 * <p>
 * Maintient un registre de toutes les installations disponibles sur la carte,
 * réparties géographiquement sur différents quartiers. Assure la liaison entre
 * les installations, le {@link StockService} et le {@link ReservationService}
 * via le pattern Observer.
 * </p>
 * <p>
 * Ce service est un singleton Guice : une seule instance est partagée
 * dans toute l'application.
 * </p>
 *
 * @see StockService
 * @see ReservationService
 * @see IInstallation
 */
@Singleton
public class MapService {

    /**
     * Registre de toutes les installations, indexées par leur identifiant unique.
     */
    private final Map<String, IInstallation> lesInstallations = new HashMap<>();

    /**
     * Service de gestion des stocks, également observateur des installations.
     */
    private final StockService stockService;

    /**
     * Service de gestion des réservations.
     */
    private final ReservationService reservationService;

    /**
     * Constructeur injecté par Guice.
     * <p>
     * Initialise les dépendances et déclenche la création de toutes
     * les installations sur la carte.
     * </p>
     *
     * @param stockService       le service de gestion des stocks
     * @param reservationService le service de gestion des réservations
     */
    @Inject
    public MapService(StockService stockService, ReservationService reservationService) {
        this.stockService = stockService;
        this.reservationService = reservationService;
        initialiserInstallations();
    }

    /**
     * Initialise et positionne toutes les installations sanitaires sur la carte.
     * <p>
     * Les installations sont réparties par zone géographique :
     * extrême ouest, nord, centre, est et sud de Lyon.
     * L'installation de Grange-Blanche est créée directement en état
     * {@link EtatInstallation#EN_MAINTENANCE}.
     * </p>
     */
    private void initialiserInstallations() {
        // --- EXTRÊME OUEST ---
        ajouter("vaise", InstallationFactory.createCabineStandard(), 80, 150);
        ajouter("point_du_jour", InstallationFactory.createDouche(), 100, 340);
        ajouter("saint_just", InstallationFactory.createCabineTurque(), 220, 320);
        // --- NORD ---
        ajouter("croix_rousse", InstallationFactory.createCabineStandard(), 380, 60);
        ajouter("parc_tete_dor", InstallationFactory.createUrinoir(true), 600, 80);
        // --- CENTRE ---
        ajouter("bellecour", InstallationFactory.createCabineStandard(), 400, 365);
        ajouter("hotel_de_ville", InstallationFactory.createUrinoir(false), 410, 180);
        // --- EST ---
        ajouter("partdieu", InstallationFactory.createCabineTurque(), 620, 230);
        IInstallation doucheGrangeBlanche = InstallationFactory.createDouche();
        doucheGrangeBlanche.setEtat(EtatInstallation.EN_MAINTENANCE);
        ajouter("grange_blanche", doucheGrangeBlanche, 630, 400);
        // --- SUD ---
        ajouter("confluence", InstallationFactory.createCabineStandard(), 320, 500);
        ajouter("gerland", InstallationFactory.createUrinoir(false), 510, 520);
    }

    /**
     * Enregistre une installation sur la carte à la position donnée.
     * <p>
     * Cette méthode effectue les opérations suivantes :
     * <ol>
     *   <li>Positionne l'installation aux coordonnées {@code (x, y)}</li>
     *   <li>Assigne son identifiant si elle étend {@link AbstractInstallation}</li>
     *   <li>L'enregistre dans le {@link StockService} (pattern Observer)</li>
     *   <li>Abonne le {@link StockService} aux événements de l'installation</li>
     *   <li>Ajoute l'installation au registre interne</li>
     * </ol>
     * </p>
     *
     * @param id           l'identifiant unique de l'installation
     * @param installation l'instance d'installation à enregistrer
     * @param x            la coordonnée horizontale sur la carte
     * @param y            la coordonnée verticale sur la carte
     */
    private void ajouter(String id, IInstallation installation, double x, double y) {
        installation.setPosition(x, y);
        if (installation instanceof AbstractInstallation) {
            ((AbstractInstallation) installation).setId(id);
        }
        // --- CORRECTION : Liaison du Pattern Observer ---
        // 1. On enregistre l'installation dans le service de stock
        stockService.register(installation);
        // 2. On dit à l'installation d'envoyer ses événements au StockService
        // C'est cette ligne qui permet de déclencher l'alerte !
        installation.addObserver(this.stockService);
        lesInstallations.put(id, installation);
    }

    /**
     * Retourne l'ensemble des installations enregistrées sur la carte.
     *
     * @return une {@link Map} associant chaque identifiant à son installation
     */
    public Map<String, IInstallation> getInstallations() {
        return lesInstallations;
    }

    /**
     * Retourne une installation par son identifiant unique.
     *
     * @param id l'identifiant de l'installation recherchée
     * @return l'installation correspondante
     * @throws IllegalArgumentException si aucune installation ne correspond à l'identifiant
     */
    public IInstallation getInstallationById(String id) {
        IInstallation inst = lesInstallations.get(id);
        if (inst == null) throw new IllegalArgumentException("Installation inconnue : " + id);
        return inst;
    }

    /**
     * Retourne le service de gestion des réservations.
     *
     * @return le {@link ReservationService} associé à ce service
     */
    public ReservationService getReservationService() {
        return reservationService;
    }
}
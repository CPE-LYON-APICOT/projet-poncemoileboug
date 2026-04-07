package fr.cpe.service;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import fr.cpe.model.EtatInstallation;
import fr.cpe.model.installation.AbstractInstallation;
import fr.cpe.model.installation.IInstallation;

@Singleton
public class MapService {

    private final Map<String, IInstallation> lesInstallations = new HashMap<>();
    private final StockService stockService;
    private final ReservationService reservationService;

    @Inject
    public MapService(StockService stockService, ReservationService reservationService) {
        this.stockService = stockService;
        this.reservationService = reservationService;
        initialiserInstallations();
    }

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

    public Map<String, IInstallation> getInstallations() {
        return lesInstallations;
    }

    public IInstallation getInstallationById(String id) {
        IInstallation inst = lesInstallations.get(id);
        if (inst == null) throw new IllegalArgumentException("Installation inconnue : " + id);
        return inst;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }
}

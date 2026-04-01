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

    // On utilise directement Installation comme valeur dans la Map
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
        // --- EXTRÊME OUEST (Gauche de la carte) ---
        ajouter("vaise",
            InstallationFactory.createCabineStandard(),
            80, 150  // Très à gauche, en haut
        );
        ajouter("point_du_jour",
            InstallationFactory.createDouche(),
            100, 340 // Très à gauche, au milieu
        );
        ajouter("saint_just",
            InstallationFactory.createCabineTurque(),
            220, 320 // Entre l'extrême ouest et le Vieux Lyon
        );

        // --- NORD (Haut de la carte) ---
        ajouter("croix_rousse",
            InstallationFactory.createCabineStandard(),
            380, 60
        );
        ajouter("parc_tete_dor",
            InstallationFactory.createUrinoir(true),
            600, 80
        );

        // --- CENTRE (Espacés pour ne pas coller Bellecour) ---
        ajouter("bellecour",
            InstallationFactory.createCabineStandard(),
            400, 365
        );
        ajouter("hotel_de_ville",
            InstallationFactory.createUrinoir(false),
            410, 180 // Plus haut que Bellecour
        );

        // --- EST (Droite de la carte) ---
        ajouter("partdieu",
            InstallationFactory.createCabineTurque(),
            620, 230
        );
        
        // Douche avec état de maintenance
        IInstallation doucheGrangeBlanche = InstallationFactory.createDouche();
        doucheGrangeBlanche.setEtat(EtatInstallation.EN_MAINTENANCE);
        ajouter("grange_blanche",
            doucheGrangeBlanche,
            630, 400
        );

        // --- SUD (Bas de la carte) ---
        ajouter("confluence",
            InstallationFactory.createCabineStandard(),
            320, 500
        );
        ajouter("gerland",
            InstallationFactory.createUrinoir(false),
            510, 520 // Très en bas
        );
    }

    private void ajouter(String id, IInstallation installation, double x, double y) {
        installation.setPosition(x, y);

        if (installation instanceof AbstractInstallation) {
            ((AbstractInstallation) installation).setId(id);
        }

        stockService.register(installation);

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

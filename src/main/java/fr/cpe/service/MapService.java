package fr.cpe.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.cpe.model.consommable.PapierToilette;
import fr.cpe.model.consommable.Savon;
import fr.cpe.model.consommable.Shampoing;
import fr.cpe.model.installation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MapService {

    // On utilise directement Installation comme valeur dans la Map
    private final Map<String, Installation> lesInstallations = new HashMap<>();

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
            new CabineStandard(Arrays.asList(new PapierToilette(15, 5))),
            80, 150  // Très à gauche, en haut
        );
        ajouter("point_du_jour",
            new Douche(Arrays.asList(new Savon(10, 2)), "Douche Ouest"),
            100, 340 // Très à gauche, au milieu
        );
        ajouter("saint_just",
            new CabineTurque(Arrays.asList(new PapierToilette(10, 2))),
            220, 320 // Entre l'extrême ouest et le Vieux Lyon
        );

        // --- NORD (Haut de la carte) ---
        ajouter("croix_rousse",
            new CabineStandard(Arrays.asList(new PapierToilette(30, 10))),
            380, 60
        );
        ajouter("parc_tete_dor",
            new Urinoir(Arrays.asList(new Savon(10, 2))),
            600, 80
        );

        // --- CENTRE (Espacés pour ne pas coller Bellecour) ---
        ajouter("bellecour",
            new CabineStandard(Arrays.asList(new PapierToilette(20, 5), new Savon(15, 3))),
            400, 365
        );
        ajouter("hotel_de_ville",
            new Urinoir(Arrays.asList()),
            410, 180 // Plus haut que Bellecour
        );

        // --- EST (Droite de la carte) ---
        ajouter("partdieu",
            new CabineTurque(Arrays.asList(new PapierToilette(10, 3))),
            620, 230
        );
        ajouter("grange_blanche",
            new Douche(Arrays.asList(new Shampoing(15, 5)), "Douche Hôpital"),
            630, 400 // Très à droite
        );

        // --- SUD (Bas de la carte) ---
        ajouter("confluence",
            new CabineStandard(Arrays.asList(new Savon(25, 5))),
            320, 500
        );
        ajouter("gerland",
            new Urinoir(Arrays.asList()),
            510, 520 // Très en bas
        );
    }

    private void ajouter(String id, Installation installation, double x, double y) {
        installation.setPosition(x, y);

        if (installation instanceof AbstractInstallation) {
            ((AbstractInstallation) installation).setId(id);
        }

        stockService.register(installation);

        lesInstallations.put(id, installation);
    }

    public Map<String, Installation> getInstallations() {
        return lesInstallations;
    }

    public Installation getInstallationById(String id) {
        Installation inst = lesInstallations.get(id);
        if (inst == null) throw new IllegalArgumentException("Installation inconnue : " + id);
        return inst;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }
}

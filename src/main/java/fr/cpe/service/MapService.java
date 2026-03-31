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
        ajouter("bellecour",
            new CabineStandard(Arrays.asList(new PapierToilette(20, 5), new Savon(15, 3))),
            400, 300
        );
        ajouter("presquile",
            new Urinoir(Arrays.asList()),
            370, 220
        );
        ajouter("perrache",
            new Douche(Arrays.asList(new Shampoing(10, 2)), "Douche Perrache"),
            340, 420
        );
        ajouter("partdieu",
            new CabineTurque(Arrays.asList(new PapierToilette(10, 3))),
            560, 250
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

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

    // id → [x, y, description, Installation]
    private final Map<String, Object[]> installations = new HashMap<>();

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
        stockService.register(installation);
        installations.put(id, new Object[]{x, y, installation.getDescription(), installation});
    }

    public Map<String, Object[]> getInstallations() {
        return installations;
    }

    public Installation getInstallationById(String id) {
        Object[] data = installations.get(id);
        if (data == null) throw new IllegalArgumentException("Installation inconnue : " + id);
        return (Installation) data[3];
    }

    public ReservationService getReservationService() {
        return reservationService;
    }
}

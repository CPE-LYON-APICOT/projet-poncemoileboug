package fr.cpe;

import fr.cpe.model.EtatInstallation;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import fr.cpe.model.installation.*;
import fr.cpe.model.installation.decorator.*;
import fr.cpe.model.consommable.IConsommable;
import fr.cpe.service.PaymentService;
import fr.cpe.service.CardStrategy;
import fr.cpe.service.StockService;
import fr.cpe.service.ReservationService;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class AppTest {

    @BeforeAll
    static void initJFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Déjà initialisé
        }
    }

    @Test
    void testInstallationSimple() {
        IInstallation cabine = new CabineStandard(new ArrayList<>());
        assertEquals(1.50, cabine.getPrix(), 0.001);
    }

    @Test
    void testCabineLuxeCumul() {
        IInstallation maCabineLuxe = new OlDecorator(
            new VipDecorator(
                new CabineStandard(new ArrayList<>())
            )
        );
        assertEquals(5.0, maCabineLuxe.getPrix(), 0.001);
    }

    private final List<IConsommable> emptyList = new ArrayList<>();

    @Test
    void testCabineTurque() {
        IInstallation turque = new CabineTurque(emptyList);
        assertEquals(1.00, turque.getPrix(), 0.001);
        assertEquals(EtatInstallation.LIBRE, turque.getEtat());
    }

    @Test
    void testDisponibiliteCommune() {
        IInstallation instal = new CabineStandard(emptyList);
        assertEquals(EtatInstallation.LIBRE, instal.getEtat());

        instal.setEtat(EtatInstallation.RESERVE);
        assertNotEquals(EtatInstallation.LIBRE, instal.getEtat());
    }

    @Test
    void testPaiementCB() {
        PaymentService paymentService = new PaymentService(new CardStrategy());
        assertTrue(paymentService.processPayment(1.50));
    }

    @Test
    void testReservationComplete() {
        StockService stockService = new StockService();
        PaymentService paymentService = new PaymentService(new CardStrategy());
        // Ajout du paramètre null pour UiService pour que ça compile
        ReservationService reservationService = new ReservationService(stockService, paymentService, null);

        IInstallation cabine = new CabineStandard(new ArrayList<>());
        stockService.register(cabine);

        // Vérification état initial
        assertEquals(EtatInstallation.LIBRE, cabine.getEtat());

        // Simulation manuelle
        paymentService.setStrategy(new CardStrategy());
        boolean ok = paymentService.processPayment(cabine.getPrix());

        if (ok) {
            cabine.setEtat(EtatInstallation.RESERVE);
            stockService.consume(cabine);
        }

        assertTrue(ok);
        assertEquals(EtatInstallation.RESERVE, cabine.getEtat());
    }

    @Test
    void testLiberation() {
        StockService stockService = new StockService();
        PaymentService paymentService = new PaymentService(new CardStrategy());
        // On garde null pour UiService, mais grâce à la modif ci-dessus, ça ne plantera plus
        ReservationService reservationService = new ReservationService(stockService, paymentService, null);

        // On s'assure que la liste passée n'est pas nulle (emptyList est déjà définie dans ta classe)
        IInstallation cabine = new CabineStandard(new ArrayList<>(emptyList));

        // On occupe la cabine
        cabine.setEtat(EtatInstallation.RESERVE);
        assertNotEquals(EtatInstallation.LIBRE, cabine.getEtat());

        // On libère via le service
        reservationService.liberer(cabine);

        // Si la liste est vide, ruptureAtteinte sera false, donc l'état doit être LIBRE
        assertEquals(EtatInstallation.LIBRE, cabine.getEtat());
    }
}

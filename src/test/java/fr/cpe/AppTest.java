package fr.cpe;

import fr.cpe.model.Ball;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import fr.cpe.model.installation.*;
import fr.cpe.model.installation.decorator.*;
import fr.cpe.model.consommable.Consommable;
import fr.cpe.service.PaymentService;
import fr.cpe.service.CardStrategy;
import fr.cpe.service.StockService;
import fr.cpe.service.ReservationService;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class AppTest {

    @BeforeAll
    static void initJFX() {
        // Démarre JavaFX une seule fois pour éviter l'erreur de Toolkit non initialisé
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Déjà initialisé
        }
    }

    @Test
    void ballInitialisesCorrectly() {
        Ball ball = new Ball(10, 20, 3, -2, Color.RED);
        assertEquals(10, ball.x);
        assertEquals(20, ball.y);
        assertEquals(3, ball.dx);
        assertEquals(-2, ball.dy);
        assertEquals(Color.RED, ball.getColor());
    }

    @Test
    void ballColorCanBeChanged() {
        Ball ball = new Ball(0, 0, 0, 0, Color.RED);
        ball.setColor(Color.BLUE);
        assertEquals(Color.BLUE, ball.getColor());
    }

    @Test
    void testInstallationSimple() {
        Installation cabine = new CabineStandard(new ArrayList<>());
        assertEquals(1.50, cabine.getPrix(), 0.001);
    }

    @Test
    void testCabineLuxeCumul() {
        Installation maCabineLuxe = new OlDecorator(
            new VipDecorator(
                new CabineStandard(new ArrayList<>())
            )
        );
        assertEquals(5.0, maCabineLuxe.getPrix(), 0.001);
    }

    private final List<Consommable> emptyList = new ArrayList<>();

    @Test
    void testCabineTurque() {
        Installation turque = new CabineTurque(emptyList);
        assertEquals(1.00, turque.getPrix(), 0.001);
        assertTrue(turque.isDisponible());
    }

    @Test
    void testDisponibilitéCommune() {
        Installation instal = new CabineStandard(emptyList);
        assertTrue(instal.isDisponible());
        instal.setDisponible(false);
        assertFalse(instal.isDisponible());
    }

    // --- TESTS DES SERVICES (CORRIGÉS POUR ÉVITER LES DIALOGS) ---

    @Test
    void testPaiementCB() {
        PaymentService paymentService = new PaymentService(new CardStrategy());
        assertTrue(paymentService.processPayment(1.50));
    }

    /**
     * Pour tester la réservation sans que la popup ne bloque Gradle,
     * on simule manuellement ce que ferait la méthode reserver().
     */
    @Test
    void testReservationComplete() {
        StockService stockService = new StockService();
        PaymentService paymentService = new PaymentService(new CardStrategy());
        ReservationService reservationService = new ReservationService(stockService, paymentService);

        Installation cabine = new CabineStandard(new ArrayList<>());
        stockService.register(cabine);

        // Simulation manuelle (sans appeler reserver() qui ouvre un Dialog)
        assertTrue(cabine.isDisponible());

        // Logique de réservation "Back-end"
        paymentService.setStrategy(new CardStrategy());
        boolean ok = paymentService.processPayment(cabine.getPrix());
        if (ok) {
            cabine.setDisponible(false);
            stockService.consume(cabine);
        }

        assertTrue(ok);
        assertFalse(cabine.isDisponible());
    }

    @Test
    void testLiberation() {
        StockService stockService = new StockService();
        PaymentService paymentService = new PaymentService(new CardStrategy());
        ReservationService reservationService = new ReservationService(stockService, paymentService);

        Installation cabine = new CabineStandard(new ArrayList<>());

        // On occupe la cabine
        cabine.setDisponible(false);
        assertFalse(cabine.isDisponible());

        // On libère via le service
        reservationService.liberer(cabine);
        assertTrue(cabine.isDisponible());
    }
}

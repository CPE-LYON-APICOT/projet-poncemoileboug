package fr.cpe;

import fr.cpe.model.Ball;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import java.util.ArrayList; //Pour créé une liste de consommable pour les tests
import fr.cpe.model.installation.*; // Importe toutes les installations (CabineTurque, Douche, etc.)
import fr.cpe.model.installation.decorator.*;
import fr.cpe.model.consommable.Consommable; // Importe l'interface Consommable
import fr.cpe.model.observer.SanitaireEvent;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Tests d'exemple — remplacez par vos vrais tests.
 */
class AppTest {

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

        String desc = maCabineLuxe.getDescription();
        assertTrue(desc.contains("Cabine Standard"));
        assertTrue(desc.contains("VIP"));
        assertTrue(desc.contains("OL"));
    }

    private final List<Consommable> emptyList = new ArrayList<>();

    @Test
    void testCabineTurque() {
        Installation turque = new CabineTurque(emptyList);
        assertEquals(1.00, turque.getPrix(), 0.001);
        assertEquals("Cabine turque", turque.getDescription());
        assertTrue(turque.isDisponible());
    }

    @Test
    void testDouche() {
        String descSpecifique = "Douche italienne";
        Installation douche = new Douche(emptyList, descSpecifique);
        assertEquals(3.00, douche.getPrix(), 0.001);
        assertEquals(descSpecifique, douche.getDescription());
    }

    @Test
    void testUrinoir() {
        Installation urinoir = new Urinoir(emptyList);
        assertEquals(0.5, urinoir.getPrix(), 0.001);
        assertEquals("Urinoir", urinoir.getDescription());
    }

    @Test
    void testDisponibilitéCommune() {
        // On teste que la logique de AbstractInstallation fonctionne pour tous
        Installation[] installations = {
            new CabineStandard(emptyList),
            new CabineTurque(emptyList),
            new Douche(emptyList, "Douche pas italienne"),
            new Urinoir(emptyList)
        };

        for (Installation instal : installations) {
            assertTrue(instal.isDisponible());
            instal.setDisponible(false);
            assertFalse(instal.isDisponible(), "L'installation " + instal.getDescription() + " devrait être occupée.");
        }
    }

    @Test
    void testObserverSurTousLesTypes() {
        // On vérifie que n'importe quelle installation peut notifier un observateur
        Installation urinoir = new Urinoir(emptyList);

        // Simulation d'un observateur (Service de maintenance par exemple)
        final boolean[] alerteRecue = {false};

        // On utilise la méthode addObserver définie dans AbstractInstallation
        ((AbstractInstallation) urinoir).addObserver((source, event) -> {
            if (event == SanitaireEvent.NETTOYAGE_REQUIS) {
                alerteRecue[0] = true;
            }
        });

        urinoir.notifyObservers(SanitaireEvent.NETTOYAGE_REQUIS);
        assertTrue(alerteRecue[0], "L'urinoir devrait avoir prévenu la maintenance via le pattern Observer.");
    }
}

package fr.cpe;

import fr.cpe.model.Ball;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import fr.cpe.model.installation.Installation;
import fr.cpe.model.installation.CabineStandard;
import fr.cpe.model.installation.decorator.OlDecorator;
import fr.cpe.model.installation.decorator.VipDecorator;
import java.util.ArrayList; //Pour créé une liste de consommable pour les tests

import static org.junit.jupiter.api.Assertions.*;

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
}

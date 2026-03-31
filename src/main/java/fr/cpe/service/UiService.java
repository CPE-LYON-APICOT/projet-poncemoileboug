package fr.cpe.service;

import com.google.inject.Singleton;
import fr.cpe.model.installation.Installation;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class UiService {

    // On stocke les Nodes JavaFX ici
    private final Map<Installation, Circle> visualPings = new HashMap<>();

    /**
     * Crée le visuel d'une installation une seule fois.
     */
    public void dessinerInstallation(Pane pane, Installation inst, Runnable onClickAction) {
        double x = inst.getX();
        double y = inst.getY();

        Circle ping = new Circle(x, y, 12, Color.GRAY);
        ping.setStroke(Color.WHITE);
        ping.setStrokeWidth(2);

        Text label = new Text(x + 15, y + 5, inst.getDescription());
        label.setFill(Color.WHITE);
        label.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");

        // On déclenche l'action de réservation passée par le GameService
        ping.setOnMouseClicked(e -> onClickAction.run());

        visualPings.put(inst, ping);
        pane.getChildren().addAll(ping, label);
    }

    /**
     * Met à jour les couleurs sans rien recréer.
     * Appelé 60 fois par seconde par l'update du GameService.
     */
    public void rafraichirAffichage() {
        visualPings.forEach((inst, circle) -> {
            Color color = inst.isDisponible()
                ? Color.web("#22c55e")  // Vert
                : Color.web("#ef4444"); // Rouge

            if (!circle.getFill().equals(color)) {
                circle.setFill(color);
            }
        });
    }
}

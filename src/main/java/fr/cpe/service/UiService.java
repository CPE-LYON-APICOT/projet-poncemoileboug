package fr.cpe.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Singleton;

import fr.cpe.model.installation.Installation;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

@Singleton
public class UiService {

    // On stocke les Nodes JavaFX ici
    private final Map<Installation, Circle> visualPings = new HashMap<>();
    private final Map<Installation, Text> visualLabels = new HashMap<>();
    private final Map<Installation, java.util.List<String>> decorations = new HashMap<>();
    private final Map<Installation, Map<String, ImageView>> decorationImages = new HashMap<>();
    private Pane gamePane;

    /**
     * Crée le visuel d'une installation une seule fois.
     */
    public void dessinerInstallation(Pane pane, Installation inst, Runnable onClickAction) {
        this.gamePane = pane;
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
        visualLabels.put(inst, label);
        pane.getChildren().addAll(ping, label);
    }

    /**
     * Enregistre une décoration (thème) pour une installation
     */
    public void ajouterDecoration(Installation installation, String type) {
        
        if (!decorations.containsKey(installation)) {
            decorations.put(installation, new java.util.ArrayList<>());
        }
        decorations.get(installation).add(type);
    }

    /**
     * Met à jour les couleurs et le timer sans rien recréer.
     * Appelé 60 fois par seconde par l'update du GameService.
     */
    public void rafraichirAffichage() {
        visualPings.forEach((inst, circle) -> {
            // Mise à jour des couleurs
            Color color;
            if (inst.isDisponible()) {
                color = Color.web("#22c55e"); // Vert
            } else {
                color = Color.web("#ef4444"); // Rouge
            }
            if (!circle.getFill().equals(color)) {
                circle.setFill(color);
            }

            // Mise à jour du label (timer)
            Text label = visualLabels.get(inst);
            if (label != null) {
                if (inst.getTimeReservedUntil() > 0 && System.currentTimeMillis() < inst.getTimeReservedUntil()) {
                    long remainingTime = (inst.getTimeReservedUntil() - System.currentTimeMillis()) / 1000;
                    if (remainingTime >= 0) {
                        label.setText(remainingTime + "s");
                    }
                } else {
                    label.setText(inst.getDescription());
                }
            }

            // Mise à jour des images de décoration
            List<String> deco = decorations.get(inst);
            if (deco != null && !deco.isEmpty()) {
                // Créer la Map pour cette installation si elle n'existe pas
                if (!decorationImages.containsKey(inst)) {
                    decorationImages.put(inst, new HashMap<>());
                }
                
                Map<String, ImageView> images = decorationImages.get(inst);
                
                // Pour chaque décoration, créer l'image si elle n'existe pas
                for (String type : deco) {
                    if (!images.containsKey(type)) {
                        ImageView img = new ImageView(new javafx.scene.image.Image(getClass().getResourceAsStream("/" + type + ".png")));
                        img.setFitWidth(30);
                        img.setFitHeight(30);
                        img.setX(inst.getX() - 15);  // Centrer sous le ping
                        img.setY(inst.getY() + 20);
                        images.put(type, img);
                        gamePane.getChildren().add(img);
                    }
                }
            }
        });
    }
}

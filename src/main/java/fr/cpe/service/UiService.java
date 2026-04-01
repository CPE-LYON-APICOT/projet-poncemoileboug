package fr.cpe.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Singleton;

import fr.cpe.model.installation.IInstallation;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

@Singleton
public class UiService {

    // On stocke les Nodes JavaFX ici
    private final Map<IInstallation, Circle> visualPings = new HashMap<>();
    private final Map<IInstallation, Text> visualLabels = new HashMap<>();
    private final Map<IInstallation, java.util.List<String>> decorations = new HashMap<>();
    private final Map<IInstallation, HBox> decorationContainers = new HashMap<>();
    private Pane gamePane;

    /**
     * Crée le visuel d'une installation une seule fois.
     */
    public void dessinerInstallation(Pane pane, IInstallation inst, Runnable onClickAction) {
        this.gamePane = pane;
        double x = inst.getX();
        double y = inst.getY();

        Circle ping = new Circle(x, y, 12, Color.GRAY);
        ping.setStroke(Color.BLACK);
        ping.setStrokeWidth(2);

        Text label = new Text(x + 15, y + 5, inst.getDescription());
        label.setFill(Color.BLACK);
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
    public void ajouterDecoration(IInstallation installation, String type) {

        if (!decorations.containsKey(installation)) {
            decorations.put(installation, new java.util.ArrayList<>());
        }
        decorations.get(installation).add(type);
    }

    /**
     * Crée un HBox pour afficher les images de décoration côte à côte
     */
    private void initialiserContainerDecoration(IInstallation inst) {
        HBox container = new HBox(5); // Espacement de 5px entre les images
        container.setLayoutX(inst.getX() - 15); // Centrer sous le ping
        container.setLayoutY(inst.getY() + 20);
        gamePane.getChildren().add(container);
        decorationContainers.put(inst, container);
    }

    public void setDecorations(IInstallation installation, List<String> types) {
        decorations.put(installation, types);
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
                // Initialiser le conteneur si ce n'est pas déjà fait
                if (!decorationContainers.containsKey(inst)) {
                    initialiserContainerDecoration(inst);
                }

                HBox container = decorationContainers.get(inst);
                container.getChildren().clear(); // Vider avant de remplir

                // Ajouter les images au conteneur
                for (String type : deco) {
                    try {
                        ImageView img = new ImageView(new javafx.scene.image.Image(getClass().getResourceAsStream("/" + type + ".png")));
                        img.setFitWidth(30);
                        img.setFitHeight(30);
                        container.getChildren().add(img);
                    } catch (Exception e) {
                        System.err.println("Image non trouvée: " + type + ".png");
                    }
                }
            }
        });
    }
}

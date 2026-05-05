package fr.cpe.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Singleton;

import fr.cpe.model.EtatInstallation;
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
     * Supprime les décorations enregistrées et nettoie l'affichage
     */
    public void clearDecorations(IInstallation installation) {
        decorations.remove(installation);
        HBox container = decorationContainers.get(installation);
        if (container != null) {
            container.getChildren().clear();
        }
    }

    /**
     * Met à jour les couleurs et le timer sans rien recréer.
     * Appelé 60 fois par seconde par l'update du GameService.
     */
    public void rafraichirAffichage() {
        visualPings.forEach((inst, circle) -> {
            // 1. Mise à jour des couleurs du cercle
            Color color;
            if (inst.getEtat() == EtatInstallation.LIBRE) {
                color = Color.web("#22c55e"); // Vert
            } else if (inst.getEtat() == EtatInstallation.RESERVE) {
                color = Color.web("#ef4444"); // Rouge
            } else {
                color = Color.web("#e0cb0a"); // Jaune (Maintenance)
            }

            if (!circle.getFill().equals(color)) {
                circle.setFill(color);
            }

            // 2. Mise à jour du label (Timer ou Description)
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

            // 3. Mise à jour des images de décoration (Thèmes)
            HBox container = decorationContainers.get(inst);
            List<String> deco = decorations.get(inst);

            // CONDITION CRUCIALE : On affiche les thèmes UNIQUEMENT si l'état est RESERVE
            if (inst.getEtat() == EtatInstallation.RESERVE && deco != null && !deco.isEmpty()) {

                // Initialiser le conteneur s'il n'existe pas encore
                if (container == null) {
                    initialiserContainerDecoration(inst);
                    container = decorationContainers.get(inst);
                }

                // Pour éviter de re-remplir le HBox 60 fois par seconde (lag),
                // on ne le fait que s'il est actuellement vide
                if (container.getChildren().isEmpty()) {
                    for (String type : deco) {
                        try {
                            String imagePath = "/" + type + ".png";
                            ImageView img = new ImageView(new javafx.scene.image.Image(getClass().getResourceAsStream(imagePath)));
                            img.setFitWidth(30);
                            img.setFitHeight(30);
                            container.getChildren().add(img);
                        } catch (Exception e) {
                            System.err.println("Image non trouvée: " + type + ".png");
                        }
                    }
                }
            } else {
                // Si l'installation n'est plus réservée (LIBRE ou MAINTENANCE),
                // on vide le conteneur pour faire disparaître les icônes
                if (container != null && !container.getChildren().isEmpty()) {
                    container.getChildren().clear();
                }
            }
        });
    }
}

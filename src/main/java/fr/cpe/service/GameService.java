package fr.cpe.service;

// ╔══════════════════════════════════════════════════════════════════════════════╗
// ║                                                                            ║
// ║   ✏️  FICHIER MODIFIABLE — C'est le cœur de votre projet                   ║
// ║                                                                            ║
// ║   Le code actuel est un EXEMPLE (une balle qui rebondit).                  ║
// ║   Remplacez-le entièrement par votre propre logique de jeu.                ║
// ║                                                                            ║
// ║   Gardez juste la structure init() / update() car GameEngine              ║
// ║   les appelle automatiquement.                                             ║
// ║                                                                            ║
// ╚══════════════════════════════════════════════════════════════════════════════╝

import java.util.Optional;

import com.google.inject.Inject;

import fr.cpe.model.installation.Installation;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class GameService {

    private final MapService mapService;

    public GameService(MapService mapService) {
        this.mapService = mapService;
    }

    public void init(Pane gamePane) {
        // Image de fond
        Image image = new Image(getClass().getResourceAsStream("/lyon.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(800);
        imageView.setFitHeight(600);
        gamePane.getChildren().add(imageView);

        // --- CORRECTION ICI : On utilise directement l'objet Installation ---
        mapService.getInstallations().forEach((id, installation) -> {
            // On utilise les getters que nous avons ajoutés à l'interface/classe abstraite
            double x = installation.getX();
            double y = installation.getY();
            String description = installation.getDescription();

            ajouterPing(gamePane, x, y, description, installation);
        });
    }

    public void update(double w, double h) {
        // Logique de mise à jour (vide pour l'instant)
    }

    private void ajouterPing(Pane pane, double x, double y,
                              String description, Installation installation) {

        // Couleur selon disponibilité : vert = libre, rouge = occupé
        Color couleurInitiale = installation.isDisponible()
            ? Color.web("#22c55e")  // vert
            : Color.web("#ef4444"); // rouge

        // Le cercle (ping)
        Circle ping = new Circle(x, y, 12, couleurInitiale);
        ping.setStroke(Color.WHITE);
        ping.setStrokeWidth(2);

        // Le label
        Text label = new Text(x + 15, y + 5, description);
        label.setFill(Color.WHITE);
        label.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");

        // Clic sur le ping
        ping.setOnMouseClicked(e -> afficherPopup(installation, ping));

        // Effets de survol
        ping.setOnMouseEntered(e -> {
            ping.setFill(installation.isDisponible()
                ? Color.web("#16a34a")
                : Color.web("#dc2626"));
        });

        ping.setOnMouseExited(e -> {
            ping.setFill(installation.isDisponible()
                ? Color.web("#22c55e")
                : Color.web("#ef4444"));
        });

        pane.getChildren().addAll(ping, label);
    }

    private void afficherPopup(Installation installation, Circle ping) {
        if (!installation.isDisponible()) {
            afficherResultat(false, installation.getDescription());
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Réservation");
        alert.setHeaderText(installation.getDescription());

        alert.setContentText(
            "Statut : Disponible\n" +
            "Prix : " + installation.getPrix() + "€\n\n" +
            "Voulez-vous réserver ?"
        );

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean ok = mapService.getReservationService().reserver(installation);
            if (ok) {
                ping.setFill(Color.web("#ef4444")); // Mise à jour visuelle immédiate
            }
            afficherResultat(ok, installation.getDescription());
        }
    }

    private void afficherResultat(boolean ok, String description) {
        Alert resultat = new Alert(ok ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING);
        resultat.setTitle(ok ? "Confirmée" : "Impossible");
        resultat.setHeaderText(null);
        resultat.setContentText(ok
            ? "✅ " + description + " réservée !"
            : "❌ Déjà occupée ou paiement refusé."
        );
        resultat.show();
    }
}

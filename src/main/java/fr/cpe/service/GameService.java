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

    @Inject
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

        // Ajoute les pings
        mapService.getInstallations().forEach((id, data) -> {
            double x = (double) data[0];
            double y = (double) data[1];
            String description = (String) data[2];
            Installation installation = mapService.getInstallationById(id);

            ajouterPing(gamePane, x, y, description, installation);
        });
    }

    public void update(double w, double h) {}

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
        ping.setOnMouseEntered(e -> {
            if (installation.isDisponible()) {
                ping.setFill(Color.web("#16a34a")); // vert foncé = disponible au survol
            } else {
                ping.setFill(Color.web("#dc2626")); // rouge foncé = occupé au survol
            }
        });
        ping.setOnMouseExited(e -> {
            if (installation.isDisponible()) {
                ping.setFill(Color.web("#22c55e")); // vert = disponible
            } else {
                ping.setFill(Color.web("#ef4444")); // rouge = occupé
            }
        });

        pane.getChildren().addAll(ping, label);
    }

    private void afficherPopup(Installation installation, Circle ping) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Réservation");
        alert.setHeaderText(installation.getDescription());

        String statut = installation.isDisponible() ? "Disponible" : "Occupée";
        alert.setContentText(
            statut + "\n" +
            "Prix : " + installation.getPrix() + "€\n\n" +
            "Voulez-vous réserver ?"
        );

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean ok = mapService.getReservationService().reserver(installation);
            if (ok) {
                ping.setFill(Color.web("#ef4444")); // rouge = occupé
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

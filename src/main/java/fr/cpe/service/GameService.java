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

import fr.cpe.engine.InputService;
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
    private final UiService uiService; // Nouveau service injecté
    private final ReservationService reservationService;

    @Inject
    public GameService(MapService mapService, UiService uiService, ReservationService reservationService) {
        this.mapService = mapService;
        this.uiService = uiService;
        this.reservationService = reservationService;
    }

    public void init(Pane gamePane) {
        // Fond
        Image image = new Image(getClass().getResourceAsStream("/lyon.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(800);
        imageView.setFitHeight(600);
        gamePane.getChildren().add(imageView);

        // Initialisation des visuels
        mapService.getInstallations().forEach((id, inst) -> {
            // On demande à l'UI de dessiner, et on lui dit quoi faire si on clique
            uiService.dessinerInstallation(gamePane, inst, () -> {
                tenterReservation(inst);
            });
        });
    }

    public void update(double w, double h) {
        // Vérifier si des réservations ont expiré
        mapService.getInstallations().forEach((id, installation) -> {
            if (installation.getTimeReservedUntil() > 0 && System.currentTimeMillis() > installation.getTimeReservedUntil()) {
                reservationService.liberer(installation);
            }
        });

        // On demande juste à l'UI de se rafraîchir
        uiService.rafraichirAffichage();
    }


    private void tenterReservation(Installation inst) {
        // 1. Si c'est déjà occupé, on prévient juste
        if (!inst.isDisponible()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Indisponible");
            alert.setHeaderText(null);
            alert.setContentText("Cette installation est déjà occupée !");
            alert.show();
            return;
        }

        // 2. Création de la popup de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Réservation");
        confirmation.setHeaderText(inst.getDescription());
        confirmation.setContentText("Prix : " + inst.getPrix() + "€\nVoulez-vous réserver ?");

        // 3. On attend la réponse de l'utilisateur
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // L'utilisateur a cliqué sur OK, on procède à la réservation
            boolean ok = reservationService.reserver(inst);

            // 4. Petit feedback pour savoir si ça a marché (budget, stock...)
            Alert bilan = new Alert(ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            bilan.setTitle(ok ? "Succès" : "Erreur");
            bilan.setHeaderText(null);
            bilan.setContentText(ok
                ? "✅ Réservation confirmée !"
                : "❌ Impossible de réserver (fonds insuffisants ou stock vide).");
            bilan.show();
        }
    }
}

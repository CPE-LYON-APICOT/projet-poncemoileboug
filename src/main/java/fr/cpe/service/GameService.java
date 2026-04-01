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

import fr.cpe.model.EtatInstallation;
import fr.cpe.model.installation.IInstallation;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

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
            // On ne libère que si l'état est RESERVE et que le temps est écoulé
            if (installation.getEtat() == EtatInstallation.RESERVE
                && installation.getTimeReservedUntil() > 0
                && System.currentTimeMillis() > installation.getTimeReservedUntil()) {

                reservationService.liberer(installation);
            }
        });

        uiService.rafraichirAffichage();
    }


    private void tenterReservation(IInstallation inst) {
        // 1. Gestion des différents états pour le feedback utilisateur
        if (inst.getEtat() == EtatInstallation.EN_MAINTENANCE) {
            afficherAlerte("Indisponible", "Cette installation est actuellement en maintenance.", Alert.AlertType.WARNING);
            return;
        }

        if (inst.getEtat() == EtatInstallation.RESERVE) {
            afficherAlerte("Indisponible", "Cette installation est déjà occupée !", Alert.AlertType.WARNING);
            return;
        }

        // 2. Création de la popup de confirmation (uniquement si LIBRE)
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Réservation");
        confirmation.setHeaderText(inst.getDescription());
        confirmation.setContentText("Prix de base : " + inst.getPrix() + "€\nVoulez-vous personnaliser et réserver ?");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean ok = reservationService.reserver(inst);

            // 4. Feedback final
            if (ok) {
                afficherAlerte("Succès", "✅ Réservation confirmée ! Profitez bien.", Alert.AlertType.INFORMATION);
            } else {
                // Le paiement a pu échouer ou l'utilisateur a annulé dans le sous-menu
                System.out.println("[GAME] Réservation annulée ou échec paiement.");
            }
        }
    }

    /**
     * Méthode utilitaire pour éviter de dupliquer le code des alertes
     */
    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}

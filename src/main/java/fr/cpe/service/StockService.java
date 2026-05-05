package fr.cpe.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import fr.cpe.model.consommable.IConsommable;
import fr.cpe.model.installation.IInstallation;
import fr.cpe.model.observer.IInstallationObserver;
import fr.cpe.model.observer.SanitaireEvent;

/**
 * Gère le stock de consommables pour chaque installation sanitaire.
 * Implémente {@link IInstallationObserver} pour réagir aux alertes de stock.
 */
@Singleton
public class StockService implements IInstallationObserver {

    /** Stock central : associe chaque installation à ses consommables. */
    private final Map<IInstallation, List<IConsommable>> stocks = new HashMap<>();

    @Inject
    public StockService() {}

    /**
     * Enregistre une installation et charge ses consommables dans le stock.
     *
     * @param installation l'installation à enregistrer
     */
    public void register(IInstallation installation) {
        stocks.put(installation, installation.getConsommables());
    }

    /**
     * Décrémente les consommables d'une installation après une prestation.
     * Déclenche une alerte si un consommable est en rupture imminente.
     *
     * @param installation l'installation ayant effectué une prestation
     */
    public void consume(IInstallation installation) {
        List<IConsommable> consommables = stocks.get(installation);
        if (consommables == null) return;

        boolean ruptureProche = false;

        for (IConsommable c : consommables) {
            if (c.getQuantite() <= 1) {
                ruptureProche = true;
            } else {
                c.setQuantite(c.getQuantite() - 1);
            }
        }

        if (ruptureProche) {
            installation.notifyObservers(SanitaireEvent.STOCK_ALERT);
        } else {
            checkLevels(installation);
        }
    }

    /**
     * Vérifie les niveaux de stock d'une installation et notifie si un seuil est atteint.
     *
     * @param installation l'installation à vérifier
     */
    public void checkLevels(IInstallation installation) {
        List<IConsommable> consommables = stocks.get(installation);
        if (consommables == null) return;

        boolean ruptureTotale = false;
        boolean seuilAtteint = false;

        for (IConsommable c : consommables) {
            if (c.getQuantite() <= 0) {
                ruptureTotale = true;
                break;
            } else if (c.getQuantite() <= c.getSeuilAlerte()) {
                seuilAtteint = true;
            }
        }

        if (ruptureTotale || seuilAtteint) {
            installation.notifyObservers(SanitaireEvent.STOCK_ALERT);
        }
    }

    /**
     * Reçoit les événements de stock et affiche une popup adaptée (alerte ou rupture).
     *
     * @param source l'installation émettrice de l'événement
     * @param event  l'événement reçu
     */
    @Override
    public void onEvent(IInstallation source, SanitaireEvent event) {
        if (event == SanitaireEvent.STOCK_ALERT) {

            boolean rupture = source.getConsommables().stream()
                                    .anyMatch(c -> c.getQuantite() <= 1);

            StringBuilder message = new StringBuilder();
            String titre;
            javafx.scene.control.Alert.AlertType typeAlerte;

            if (rupture) {
                titre = "DERNIÈRE UTILISATION - Rupture imminente";
                typeAlerte = javafx.scene.control.Alert.AlertType.ERROR;
                message.append("Attention : C'est la dernière utilisation possible pour ")
                    .append(source.getDescription())
                    .append(".\nL'installation passera en maintenance automatiquement juste après.");
            } else {
                titre = "Attention : Stock Faible";
                typeAlerte = javafx.scene.control.Alert.AlertType.WARNING;
                message.append("Le seuil d'alerte est atteint pour ")
                    .append(source.getDescription()).append(".\n");
            }

            message.append("\n\nÉtat des consommables :\n");
            for (IConsommable c : source.getConsommables()) {
                if (c.getQuantite() <= c.getSeuilAlerte()) {
                    message.append("- ").append(c.getNom())
                        .append(" : ").append(c.getQuantite()).append(" restant(s)\n");
                }
            }

            javafx.application.Platform.runLater(() -> {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(typeAlerte);
                alert.setTitle(titre);
                alert.setHeaderText(null);
                alert.setContentText(message.toString());
                alert.show();
            });
        }
    }
}
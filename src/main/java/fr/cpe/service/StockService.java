package fr.cpe.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import fr.cpe.model.EtatInstallation;
import fr.cpe.model.consommable.IConsommable;
import fr.cpe.model.installation.IInstallation;
import fr.cpe.model.observer.IInstallationObserver;
import fr.cpe.model.observer.SanitaireEvent;

@Singleton
public class StockService implements IInstallationObserver {

    // La map centrale : chaque installation pointe vers sa liste de consommables
    private final Map<IInstallation, List<IConsommable>> stocks = new HashMap<>();

    @Inject
    public StockService() {}

    // Enregistre une installation et ses consommables dans le service
    public void register(IInstallation installation) {
        stocks.put(installation, installation.getConsommables());
    }

    // Appelé après une prestation : décrémente chaque consommable de l'installation
public void consume(IInstallation installation) {
    List<IConsommable> consommables = stocks.get(installation);
    if (consommables == null) return;

    boolean ruptureProche = false;

    for (IConsommable c : consommables) {
        if (c.getQuantite() <= 1) {
            ruptureProche = true;
            // On ne fait PAS c.setQuantite(0) pour éviter ton exception
        } else {
            c.setQuantite(c.getQuantite() - 1);
        }
    }

    if (ruptureProche) {
        // On prévient juste qu'on est au bout du stock
        installation.notifyObservers(SanitaireEvent.STOCK_ALERT);
    } else {
        checkLevels(installation);
    }
}


    public void checkLevels(IInstallation installation) {
        List<IConsommable> consommables = stocks.get(installation);
        if (consommables == null) return;

        boolean ruptureTotale = false;
        boolean seuilAtteint = false;

        for (IConsommable c : consommables) {
            if (c.getQuantite() <= 0) {
                ruptureTotale = true; // Plus rien du tout !
                break;
            } else if (c.getQuantite() <= c.getSeuilAlerte()) {
                seuilAtteint = true; // Juste une alerte
            }
        }

        if (ruptureTotale) {
            // On peut créer un nouvel Event ou réutiliser l'existant avec une logique d'état
            installation.notifyObservers(SanitaireEvent.STOCK_ALERT);
        } else if (seuilAtteint) {
            // Optionnel : notifyObservers(SanitaireEvent.STOCK_LOW); si tu as cet enum
            // Sinon, on gère la distinction dans onEvent via les quantités
            installation.notifyObservers(SanitaireEvent.STOCK_ALERT);
        }
    }

    @Override
    public void onEvent(IInstallation source, SanitaireEvent event) {
        if (event == SanitaireEvent.STOCK_ALERT) {

            // On vérifie si c'est la fin du stock (quantité à 1 car ton code interdit 0)
            boolean rupture = source.getConsommables().stream()
                                    .anyMatch(c -> c.getQuantite() <= 1);

            StringBuilder message = new StringBuilder();
            String titre;
            javafx.scene.control.Alert.AlertType typeAlerte;

            if (rupture) {
                // --- CAS RUPTURE : INFORMATION ---
                // On ne change PAS l'état ici pour laisser le timer actuel finir.
                titre = "DERNIÈRE UTILISATION - Rupture imminente";
                typeAlerte = javafx.scene.control.Alert.AlertType.ERROR; // Rouge car critique
                message.append("Attention : C'est la dernière utilisation possible pour ")
                    .append(source.getDescription())
                    .append(".\nL'installation passera en maintenance automatiquement juste après.");
            } else {
                // --- CAS SEUIL : SIMPLE ALERTE ---
                titre = "Attention : Stock Faible";
                typeAlerte = javafx.scene.control.Alert.AlertType.WARNING; // Orange
                message.append("Le seuil d'alerte est atteint pour ")
                    .append(source.getDescription()).append(".\n");
            }

            // Détail des consommables sous le seuil
            message.append("\n\nÉtat des consommables :\n");
            for (IConsommable c : source.getConsommables()) {
                if (c.getQuantite() <= c.getSeuilAlerte()) {
                    message.append("- ").append(c.getNom())
                        .append(" : ").append(c.getQuantite()).append(" restant(s)\n");
                }
            }

            // Affichage de la popup (Thread-safe)
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

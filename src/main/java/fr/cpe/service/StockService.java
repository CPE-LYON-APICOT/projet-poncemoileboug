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

        for (IConsommable c : consommables) {
            int nouvelleQuantite = c.getQuantite() - 1;
            if (nouvelleQuantite >= 0) {
                c.setQuantite(nouvelleQuantite);
            }
        }
        // Après consommation, on vérifie si un seuil est atteint
        checkLevels(installation);
    }

    // Vérifie les seuils d'alerte pour une installation donnée
    public void checkLevels(IInstallation installation) {
        List<IConsommable> consommables = stocks.get(installation);
        if (consommables == null) return;

        for (IConsommable c : consommables) {
            if (c.getQuantite() <= c.getSeuilAlerte()) {
                // Notifie l'installation elle-même pour propager l'event
                installation.notifyObservers(SanitaireEvent.STOCK_ALERT);
                return; // Une alerte suffit pour cette installation
            }
        }
    }

    // Point d'entrée du pattern Observer
    // Appelé automatiquement quand une installation notifie un event
    @Override
    public void onEvent(IInstallation source, SanitaireEvent event) {
        if (event == SanitaireEvent.STOCK_ALERT) {
            System.out.println("[STOCK] Alerte stock sur : " + source.getDescription());
            // Ici tu pourras brancher MaintenanceService plus tard
        }
    }
}

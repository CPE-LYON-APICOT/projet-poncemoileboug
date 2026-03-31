package fr.cpe.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import fr.cpe.model.consommable.Consommable;
import fr.cpe.model.installation.Installation;
import fr.cpe.model.observer.InstallationObserver;
import fr.cpe.model.observer.SanitaireEvent;

@Singleton
public class StockService implements InstallationObserver {

    // La map centrale : chaque installation pointe vers sa liste de consommables
    private final Map<Installation, List<Consommable>> stocks = new HashMap<>();

    @Inject
    public StockService() {}

    // Enregistre une installation et ses consommables dans le service
    public void register(Installation installation) {
        stocks.put(installation, installation.getConsommables());
    }

    // Appelé après une prestation : décrémente chaque consommable de l'installation
    public void consume(Installation installation) {
        List<Consommable> consommables = stocks.get(installation);
        if (consommables == null) return;

        for (Consommable c : consommables) {
            int nouvelleQuantite = c.getQuantite() - 1;
            if (nouvelleQuantite >= 0) {
                c.setQuantite(nouvelleQuantite);
            }
        }
        // Après consommation, on vérifie si un seuil est atteint
        checkLevels(installation);
    }

    // Vérifie les seuils d'alerte pour une installation donnée
    public void checkLevels(Installation installation) {
        List<Consommable> consommables = stocks.get(installation);
        if (consommables == null) return;

        for (Consommable c : consommables) {
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
    public void onEvent(Installation source, SanitaireEvent event) {
        if (event == SanitaireEvent.STOCK_ALERT) {
            System.out.println("[STOCK] Alerte stock sur : " + source.getDescription());
            // Ici tu pourras brancher MaintenanceService plus tard
        }
    }
}

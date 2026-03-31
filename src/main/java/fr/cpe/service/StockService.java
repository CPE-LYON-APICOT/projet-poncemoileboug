package fr.cpe.service;

import com.google.inject.Singleton;
import fr.cpe.model.consommable.Consommable;
import fr.cpe.model.installation.Installation;
import fr.cpe.model.observer.InstallationObserver;
import fr.cpe.model.observer.SanitaireEvent;

import java.util.*;

@Singleton
public class StockService implements InstallationObserver {
    
    private final Map<Installation, List<Consommable>> stocks = new HashMap<>();
    private final MaintenanceService maintenanceService;

    public StockService(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    /**
     * Consulter le stock d'une installation
     */
    public List<Consommable> getStock(Installation installation) {
        return stocks.getOrDefault(installation, new ArrayList<>());
    }

    /**
     * Décaler les quantités après une utilisation
     */
    public void consume(Installation installation) {
        List<Consommable> items = getStock(installation);
        for (Consommable item : items) {
            int newQuantite = item.getQuantite() - 1;
            item.setQuantite(newQuantite);

            // Vérifier les seuils d'alerte
            if (newQuantite <= item.getSeuilAlerte()) {
                maintenanceService.alerteStock(installation, item);
            }
        }
    }

    /**
     * Observer callback : quand une installation notifie STOCK_ALERT
     */
    @Override
    public void onEvent(Installation source, SanitaireEvent event) {
        if (event == SanitaireEvent.STOCK_ALERT) {
            checkLevels();
        }
    }

    public void checkLevels() {
        for (Map.Entry<Installation, List<Consommable>> entry : stocks.entrySet()) {
            Installation inst = entry.getKey();
            List<Consommable> items = entry.getValue();
            
            for (Consommable item : items) {
                if (item.getQuantite() <= item.getSeuilAlerte()) {
                    maintenanceService.alerteStock(inst, item);
                }
            }
        }
    }

    public void addInstallationStock(Installation installation, List<Consommable> consommables) {
        stocks.put(installation, consommables);
        installation.addObserver(this); // S'enregistrer
    }
}
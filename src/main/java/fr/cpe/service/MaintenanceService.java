package fr.cpe.service;

import com.google.inject.Inject;
import fr.cpe.model.installation.Installation;
import fr.cpe.model.observer.InstallationObserver;
import fr.cpe.model.observer.SanitaireEvent;

public class MaintenanceService implements InstallationObserver {

    @Inject
    public MaintenanceService() {}

    @Override
    public void onEvent(Installation source, SanitaireEvent event) {
        if (event == SanitaireEvent.NETTOYAGE_REQUIS) {
            notifierAgent(source);
        } else if (event == SanitaireEvent.STOCK_ALERT) {
            alerteStock(source);
        }
    }

    public void notifierAgent(Installation installation) {
        System.out.println("[MAINTENANCE] Nettoyage requis : " + installation.getDescription());
    }

    public void alerteStock(Installation installation) {
        System.out.println("[MAINTENANCE] Stock critique : " + installation.getDescription());
    }
}

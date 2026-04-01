package fr.cpe.service;

import com.google.inject.Inject;

import fr.cpe.model.installation.IInstallation;
import fr.cpe.model.observer.IInstallationObserver;
import fr.cpe.model.observer.SanitaireEvent;

public class MaintenanceService implements IInstallationObserver {

    @Inject
    public MaintenanceService() {}

    @Override
    public void onEvent(IInstallation source, SanitaireEvent event) {
        if (event == SanitaireEvent.NETTOYAGE_REQUIS) {
            notifierAgent(source);
        } else if (event == SanitaireEvent.STOCK_ALERT) {
            alerteStock(source);
        }
    }

    public void notifierAgent(IInstallation installation) {
        System.out.println("[MAINTENANCE] Nettoyage requis : " + installation.getDescription());
    }

    public void alerteStock(IInstallation installation) {
        System.out.println("[MAINTENANCE] Stock critique : " + installation.getDescription());
    }
}

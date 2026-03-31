package fr.cpe.service;

import java.util.ArrayList;
import java.util.List;

import fr.cpe.model.consommable.Consommable;
import fr.cpe.model.installation.Installation;

public class MaintenanceService {
    
    private final List<String> alertes = new ArrayList<>();

    /**
     * Alerte sur un stock critique
     */
    public void alerteStock(Installation installation, Consommable item) {
        String message = "stock critique : " + item.getNom() 
                       + " (" + item.getQuantite() + " restant(s)) "
                       + "— Installation : " + installation.getDescription();
        alertes.add(message);
        
        notifierAgent(message);
    }

    /**
     * Alerte sur un nettoyage requis
     */
    public void alerteNettoyage(Installation installation) {
        String message = "nettoyage requis : " + installation.getDescription();
        alertes.add(message);
        notifierAgent(message);
    }

    private void notifierAgent(String message) {
        System.out.println("[MAINTENANCE] " + message);
    }

    public List<String> getAlertes() {
        return new ArrayList<>(alertes);
    }

    public void clearAlertes() {
        alertes.clear();
    }
}
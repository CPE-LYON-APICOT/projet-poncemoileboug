package fr.cpe.model.installation;

import java.util.List;

import fr.cpe.model.consommable.Consommable;

public interface Installation {

    double getPrix();
    String getDescription();
    List<Consommable> getConsommables();
    boolean isDisponible();
    void setDisponible(boolean disponible);

}

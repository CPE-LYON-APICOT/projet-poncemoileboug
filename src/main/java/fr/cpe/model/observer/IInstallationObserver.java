package fr.cpe.model.observer;

import fr.cpe.model.installation.IInstallation;

public interface IInstallationObserver {

    void onEvent(IInstallation source, SanitaireEvent event);
}

package fr.cpe.model.observer;

import fr.cpe.model.installation.Installation;

public interface InstallationObserver {

    void onEvent(Installation source, SanitaireEvent event);
}

package com.jjeanniard.plugins.services;

import com.jjeanniard.plugins.config.MyConfig;

/**
 * Service simple pour gérer la logique des messages de bienvenue.
 * Son seul but est de décider quel message afficher (Premier join ou Retour).
 */
public final class WelcomeService {
    private final String firstJoinMessage;
    private final String rejoinMessage;

    // Injection de dépendance : On lui donne la config, il en extrait ce qu'il veut.
    public WelcomeService(MyConfig config) {
        this.firstJoinMessage = config.welcome.firstJoin;
        this.rejoinMessage = config.welcome.rejoin;
    }

    /**
     * Retourne le message approprié selon le contexte.
     */
    public String getWelcomeMessage(boolean isFirstJoin) {
        // Opérateur ternaire : (condition) ? (si vrai) : (si faux)
        return isFirstJoin ? firstJoinMessage : rejoinMessage;
    }
}

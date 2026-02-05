package com.jjeanniard.plugins.services;

import com.jjeanniard.plugins.config.WelcomeToServerConfig;

/**
 * Service simple pour gérer la logique des messages de bienvenue.
 * Son seul but est de décider quel message afficher (Premier join ou Retour).
 */
public final class WelcomeService {
    private String firstJoinMessage;
    private String rejoinMessage;

    // Injection de dépendance : On lui donne la config, il en extrait ce qu'il veut.
    public WelcomeService(WelcomeToServerConfig config) {
        this.updateConfig(config);
    }

    public void updateConfig(WelcomeToServerConfig config) {
        this.firstJoinMessage = config.getFirstJoin();
        this.rejoinMessage = config.getRejoin();
    }

    /**
     * Retourne le message approprié selon le contexte.
     *
     * @param isFirstJoin Indique si c'est la première connexion du joueur.
     * @return Le message de bienvenue approprié.
     */
    public String getWelcomeMessage(boolean isFirstJoin) {
        // Opérateur ternaire : (condition) ? (si vrai) : (si faux)
        return isFirstJoin ? firstJoinMessage : rejoinMessage;
    }
}

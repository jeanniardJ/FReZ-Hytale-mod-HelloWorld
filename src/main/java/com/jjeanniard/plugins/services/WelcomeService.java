package com.jjeanniard.plugins.services;

import com.jjeanniard.plugins.config.WelcomeToServerConfig;

/**
 * Service simple pour gérer la logique des messages de bienvenue.
 * Son seul but est de décider quel message afficher (Premier join ou Retour).
 */
public final class WelcomeService {
    private final String firstJoinMessage;
    private final String rejoinMessage;

    // Injection de dépendance : On lui donne la config, il en extrait ce qu'il veut.
    public WelcomeService(WelcomeToServerConfig config) {
        this.firstJoinMessage = config.getFirstJoin();
        this.rejoinMessage = config.getFirstJoin();
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

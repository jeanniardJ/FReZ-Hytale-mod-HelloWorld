package com.jjeanniard.plugins.listeners;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import com.jjeanniard.plugins.Log;
import com.jjeanniard.plugins.services.WelcomeService;

/**
 * Cette classe "écoute" ce qui se passe sur le serveur.
 * Ici, elle s'intéresse spécifiquement aux connexions de joueurs.
 */
public final class PlayerListener {
    private final WelcomeService welcomeService;

    public PlayerListener(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }

    /**
     * Cette méthode est appelée automatiquement par Hytale à chaque fois qu'un joueur se connecte.
     *
     * @param event L'objet événement contenant toutes les infos (qui est le joueur, quand, etc.)
     */
    public void onPlayerJoin(PlayerConnectEvent event) {
        // On récupère la référence du joueur qui vient d'arriver
        PlayerRef player = event.getPlayerRef();

        // TODO: Vérifier si c'est vraiment sa première fois (API à venir)
        boolean isFirstJoin = false;

        // 1. On demande au service quel est le texte brut (ex: "Bienvenue {player}")
        String rawMessage = welcomeService.getWelcomeMessage(isFirstJoin);

        // 2. On remplace "{player}" par le vrai nom "Steve"
        String formattedMessage = rawMessage.replace("{player}", player.getUsername());

        // 3. On affiche un grand titre à l'écran du joueur
        EventTitleUtil.showEventTitleToPlayer(
                player,
                Message.raw(formattedMessage), // Titre principal
                Message.raw("Amuse-toi bien !"), // Sous-titre
                true,                          // isMajor (Vrai = Gros titre, Faux = Petit titre au dessus de la barre d'action)
                "ui/icons/announcement.png",   // Chemin vers l'icône (dans les assets du jeu)
                10.0f,                          // Durée d'affichage (secondes)
                1.5f,                          // Temps d'apparition (Fade in)
                1.5f                           // Temps de disparition (Fade out)
        );

        Log.info("Joueur connecté : %s", player.getUsername());
    }
}

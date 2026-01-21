package com.jjeanniard.plugins.listeners;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import com.jjeanniard.plugins.services.WelcomeService;

public final class PlayerListener {
    private final WelcomeService welcomeService;
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public PlayerListener(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }

    public void onPlayerJoin(PlayerConnectEvent event) {
        PlayerRef player = event.getPlayerRef();
        
        // 1. Déterminer si c'est la première connexion
        boolean isFirstJoin = false; 

        // 2. Récupérer le message brut depuis la config via le service
        String rawMessage = welcomeService.getWelcomeMessage(isFirstJoin);

        // 3. Remplacer les variables dynamiquement
        String formattedMessage = rawMessage.replace("{player}", player.getUsername());

        // 4. Envoyer le titre au joueur
        EventTitleUtil.showEventTitleToPlayer(
            player,
            Message.raw(formattedMessage), // Titre principal (ex: "Bienvenue Steve")
            Message.raw("Amuse-toi bien !"), // Sous-titre (vous pouvez aussi le mettre dans la config si vous voulez)
            true,                          // isMajor (Gros titre)
            "ui/icons/announcement.png",   // Icône
            5.0f,                          // Durée (secondes)
            1.5f,                          // Fade in
            1.5f                           // Fade out
        );

        LOGGER.atInfo().log("Player joined: " + player.getUsername() + " (Title sent: " + formattedMessage + ")");
    }
}

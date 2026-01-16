package com.jjeanniard.mods;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public final class PlayerListener {
    private final WelcomeService plugin;
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public PlayerListener(WelcomeService plugin) {
        this.plugin = plugin;
    }

    public static void onPlayerJoin(PlayerConnectEvent event) {
        PlayerRef player = event.getPlayerRef();
        String message = "test";
        player.sendMessage(Message.raw(message));

        LOGGER.atInfo().log("Player joined: " + player.getUsername());
    }

}
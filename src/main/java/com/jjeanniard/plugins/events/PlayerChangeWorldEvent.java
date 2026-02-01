package com.jjeanniard.plugins.events;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.entity.entities.Player;

public class PlayerChangeWorldEvent implements IEvent<Void> {
    private final Player player;
    private final String fromWorld;
    private final String toWorld;

    public PlayerChangeWorldEvent(Player player, String fromWorld, String toWorld) {
        this.player = player;
        this.fromWorld = fromWorld;
        this.toWorld = toWorld;
    }

    public Player getPlayer() {
        return player;
    }

    public String getFromWorld() {
        return fromWorld;
    }

    public String getToWorld() {
        return toWorld;
    }
}
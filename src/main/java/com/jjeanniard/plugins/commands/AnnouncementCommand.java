package com.jjeanniard.plugins.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Commande principale pour gérer les annonces globales.
 * /announce <subcommand> [args...]
 */
public class AnnouncementCommand extends CommandBase {

    public AnnouncementCommand() {
        super("announce", "Gère les annonces automatiques du serveur.");
    }

    @Override
    protected void executeSync(@NonNull CommandContext commandContext) {
        commandContext.sendMessage(Message.raw("Utilisation: /announce <list|create|delete|toggle|edit>"));
    }

}

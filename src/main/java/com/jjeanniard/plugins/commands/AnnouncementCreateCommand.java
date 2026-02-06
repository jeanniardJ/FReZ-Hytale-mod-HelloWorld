package com.jjeanniard.plugins.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.jjeanniard.plugins.Permissions;
import com.jjeanniard.plugins.providers.Announcement;
import com.jjeanniard.plugins.services.AnnouncementManagerService;

public class AnnouncementCreateCommand extends CommandBase {

    private final AnnouncementManagerService service;
    private final RequiredArg<String> messageArg;
    private final OptionalArg<String> worldArg;

    public AnnouncementCreateCommand(AnnouncementManagerService service) {
        super("create", "Créer une nouvelle annonce.");
        this.service = service;

        // Explicit permission
        requirePermission(Permissions.ADMIN_MANAGE);

        // Argument requis : le message (entre guillemets si espaces)
        this.messageArg = withRequiredArg("message", "Le message de l'annonce", ArgTypes.STRING);

        // Argument optionnel : le monde (--world=NomDuMonde)
        this.worldArg = withOptionalArg("world", "Le monde cible", ArgTypes.STRING);
    }

    @Override
    protected void executeSync(CommandContext commandContext) {
        String message = messageArg.get(commandContext);
        String world = null;

        if (worldArg.provided(commandContext)) {
            world = worldArg.get(commandContext);
        }

        service.addAnnouncement(new Announcement(message, world));
        commandContext.sendMessage(Message.raw("Annonce créée !").color("green"));
    }
}
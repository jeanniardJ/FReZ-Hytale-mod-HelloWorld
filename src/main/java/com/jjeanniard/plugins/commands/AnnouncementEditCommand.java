package com.jjeanniard.plugins.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.jjeanniard.plugins.Permissions;
import com.jjeanniard.plugins.providers.Announcement;
import com.jjeanniard.plugins.services.AnnouncementManagerService;

public class AnnouncementEditCommand extends CommandBase {

    private final AnnouncementManagerService service;
    private final RequiredArg<Integer> indexArg;
    private final RequiredArg<String> messageArg;

    public AnnouncementEditCommand(AnnouncementManagerService service) {
        super("edit", "Modifie une annonce existante.");
        this.service = service;

        // Explicit permission
        requirePermission(Permissions.ADMIN_MANAGE);

        this.indexArg = withRequiredArg("index", "L'ID de l'annonce", ArgTypes.INTEGER);
        this.messageArg = withRequiredArg("message", "Le nouveau message", ArgTypes.STRING);
    }

    @Override
    protected void executeSync(CommandContext commandContext) {
        int index = indexArg.get(commandContext);
        String newMessage = messageArg.get(commandContext);

        Announcement old = service.getAnnouncementByIndex(index);
        if (old != null) {
            service.updateAnnouncement(new Announcement(old.id(), newMessage, old.targetWorld()));
            commandContext.sendMessage(Message.raw("Annonce modifiée !").color("green"));
        } else {
            commandContext.sendMessage(Message.raw("ID invalide.").color("red"));
        }
    }
}
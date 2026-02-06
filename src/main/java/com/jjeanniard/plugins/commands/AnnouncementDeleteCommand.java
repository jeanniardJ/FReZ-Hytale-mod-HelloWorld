package com.jjeanniard.plugins.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.jjeanniard.plugins.Permissions;
import com.jjeanniard.plugins.services.AnnouncementManagerService;

public class AnnouncementDeleteCommand extends CommandBase {

    private final AnnouncementManagerService service;
    private final RequiredArg<Integer> indexArg;

    public AnnouncementDeleteCommand(AnnouncementManagerService service) {
        super("delete", "Supprime une annonce existante.");
        this.service = service;

        // Explicit permission
        requirePermission(Permissions.ADMIN_MANAGE);

        this.indexArg = withRequiredArg("index", "L'ID de l'annonce à supprimer", ArgTypes.INTEGER);
    }

    @Override
    protected void executeSync(CommandContext commandContext) {
        int index = indexArg.get(commandContext);

        if (service.deleteAnnouncementByIndex(index)) {
            commandContext.sendMessage(Message.raw("Annonce supprimée.").color("green"));
        } else {
            commandContext.sendMessage(Message.raw("ID invalide.").color("red"));
        }
    }
}
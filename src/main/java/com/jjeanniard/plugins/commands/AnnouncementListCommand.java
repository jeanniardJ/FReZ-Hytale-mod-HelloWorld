package com.jjeanniard.plugins.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.jjeanniard.plugins.Permissions;
import com.jjeanniard.plugins.providers.Announcement;
import com.jjeanniard.plugins.services.AnnouncementManagerService;

import java.util.List;

public class AnnouncementListCommand extends CommandBase {
    private final AnnouncementManagerService announcementManagerService;
    private static final int ITEMS_PER_PAGE = 8;
    private final OptionalArg<Integer> pageArg;

    public AnnouncementListCommand(AnnouncementManagerService announcementManagerService) {
        super("list", "Liste les annonces actuelles.");
        this.announcementManagerService = announcementManagerService;

        // Explicit permission
        requirePermission(Permissions.ADMIN_MANAGE);

        this.pageArg = withOptionalArg("page", "Le numéro de page", ArgTypes.INTEGER);
    }

    @Override
    protected void executeSync(CommandContext commandContext) {
        List<Announcement> announcements = announcementManagerService.getAllAnnouncements();

        if (announcements.isEmpty()) {
            commandContext.sendMessage(Message.raw("Il n'y a aucune annonce configurée.").color("red"));
            return;
        }

        int page = 1;
        if (pageArg.provided(commandContext)) {
            page = pageArg.get(commandContext);
        }

        // Calcul des pages
        int totalPages = (int) Math.ceil((double) announcements.size() / ITEMS_PER_PAGE);
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int startIndex = (page - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, announcements.size());

        commandContext.sendMessage(Message.raw("--- Liste des Annonces (" + page + "/" + totalPages + ") ---").color("gold"));

        for (int i = startIndex; i < endIndex; i++) {
            Announcement a = announcements.get(i);
            String world = a.targetWorld() == null ? "Global" : a.targetWorld();
            // Affichage : [ID] [Monde] Message
            commandContext.sendMessage(
                    Message.join(
                            Message.raw("[" + i + "] ").color("yellow"),
                            Message.raw("[" + world + "] ").color("gray"),
                            Message.raw(a.message()).color("white")
                    )
            );
        }

        if (page < totalPages) {
            commandContext.sendMessage(Message.raw("Utilisez /announce list --page=" + (page + 1) + " pour voir la suite.").color("gray"));
        }
    }
}
package com.jjeanniard.plugins.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.jjeanniard.plugins.Permissions;
import com.jjeanniard.plugins.services.AnnouncementManagerService;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Commande principale pour gérer les annonces.
 * - Joueur : /announce : Ouvre l'interface graphique.
 * - Console : /announce : Affiche l'aide.
 * - Sous-commandes : Disponibles pour l'administration console ou avancée.
 */
public class AnnouncementCommand extends AbstractCommand {

    private final AnnouncementManagerService announcementManagerService;

    public AnnouncementCommand(AnnouncementManagerService announcementManagerService) {
        super("announce", "Gère les annonces automatiques du serveur.");
        this.announcementManagerService = announcementManagerService;

        // Explicit permission
        requirePermission(Permissions.ADMIN_MANAGE);

        // Enregistrement des sous-commandes pour l'accès console ou via chat direct
        addSubCommand(new AnnouncementListCommand(announcementManagerService));
        addSubCommand(new AnnouncementCreateCommand(announcementManagerService));
        addSubCommand(new AnnouncementDeleteCommand(announcementManagerService));
        addSubCommand(new AnnouncementEditCommand(announcementManagerService));
        addSubCommand(new AnnouncementReloadCommand());
    }

    @Override
    protected @Nullable CompletableFuture<Void> execute(@NonNull CommandContext commandContext) {
        if (commandContext.isPlayer()) {
            announcementManagerService.sendManagementPanel(commandContext);
            return CompletableFuture.completedFuture(null);
        }

        // Si c'est la console (ou autre), on affiche l'aide textuelle
        commandContext.sendMessage(Message.raw("[Console] Utilisation: /announce <list|create|delete|edit|reload>"));
        return CompletableFuture.completedFuture(null);
    }
}

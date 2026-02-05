package com.jjeanniard.plugins.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
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

        // Enregistrement des sous-commandes pour l'accès console ou via chat direct
        addSubCommand(new AnnouncementListCommand(announcementManagerService));
        addSubCommand(new AnnouncementCreateCommand());
        addSubCommand(new AnnouncementDeleteCommand());
        addSubCommand(new AnnouncementToggleCommand());
        addSubCommand(new AnnouncementEditCommand());
        addSubCommand(new AnnouncementReloadCommand());
    }

    @Override
    protected @Nullable CompletableFuture<Void> execute(@NonNull CommandContext commandContext) {
        // Vérification de la permission
        if (commandContext.isPlayer()) {
            Player player = commandContext.senderAs(Player.class);
            if (player != null && !player.hasPermission(Permissions.ADMIN_MANAGE)) {
                commandContext.sendMessage(Message.raw("§cVous n'avez pas la permission d'utiliser cette commande."));
                return CompletableFuture.completedFuture(null);
            }

            announcementManagerService.sendManagementPanel(commandContext);
            return CompletableFuture.completedFuture(null);
        }

        // Si c'est la console (ou autre), on affiche l'aide textuelle
        commandContext.sendMessage(Message.raw("§c[Console] Utilisation: /announce <list|create|delete|toggle|edit|reload>"));
        return CompletableFuture.completedFuture(null);
    }
}

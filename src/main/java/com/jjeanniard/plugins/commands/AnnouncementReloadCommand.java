package com.jjeanniard.plugins.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.jjeanniard.plugins.HelloWorld;
import com.jjeanniard.plugins.Permissions;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.CompletableFuture;

public class AnnouncementReloadCommand extends AbstractCommand {

    public AnnouncementReloadCommand() {
        super("reload", "Recharge la configuration du plugin");
        // Explicit permission
        requirePermission(Permissions.ADMIN_RELOAD);
    }

    @Override
    protected @Nullable CompletableFuture<Void> execute(@NonNull CommandContext commandContext) {
        HelloWorld.getInstance().reload();
        commandContext.sendMessage(Message.raw("§aConfiguration rechargée avec succès !"));
        return CompletableFuture.completedFuture(null);
    }
}

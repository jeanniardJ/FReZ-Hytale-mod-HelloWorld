package com.jjeanniard.plugins.commands;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.CompletableFuture;

public class AnnouncementDeleteCommand extends AbstractCommand {
    public AnnouncementDeleteCommand() {
        super("delete", "Supprime une annonce existante.");
    }

    @Override
    protected @Nullable CompletableFuture<Void> execute(@NonNull CommandContext commandContext) {
        return null;
    }
}

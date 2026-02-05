package com.jjeanniard.plugins.commands;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.CompletableFuture;

public class AnnouncementEditCommand extends AbstractCommand {
    public AnnouncementEditCommand() {
        super("edit", "Modifie une annonce existante.");
    }

    @Override
    protected @Nullable CompletableFuture<Void> execute(@NonNull CommandContext commandContext) {
        return null;
    }
}

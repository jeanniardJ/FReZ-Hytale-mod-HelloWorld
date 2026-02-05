package com.jjeanniard.plugins.commands;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.jjeanniard.plugins.services.AnnouncementManagerService;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.CompletableFuture;

public class AnnouncementListCommand extends AbstractCommand {
    private final AnnouncementManagerService announcementManagerService;

    public AnnouncementListCommand(AnnouncementManagerService announcementManagerService) {
        super("list", "Liste les annonces actuelles.");
        this.announcementManagerService = announcementManagerService;
    }

    @Override
    protected CompletableFuture<Void> execute(@NonNull CommandContext commandContext) {
        announcementManagerService.list(commandContext);
        return null;
    }
}

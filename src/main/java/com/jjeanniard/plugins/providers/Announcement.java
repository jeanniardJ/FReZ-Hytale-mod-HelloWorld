package com.jjeanniard.plugins.providers;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * @param targetWorld null = Global
 */
public record Announcement(UUID id, String message, String targetWorld) {
    
    public Announcement(String message, String targetWorld) {
        this(UUID.randomUUID(), message, targetWorld);
    }

    @Override
    @Nonnull
    public String toString() {
        return message;
    }

}
package com.jjeanniard.plugins.providers;

import javax.annotation.Nonnull;

/**
 * @param targetWorld null = Global
 */
public record Announcement(String message, String targetWorld) {
    @Override
    @Nonnull
    public String toString() {
        return message;
    }

}
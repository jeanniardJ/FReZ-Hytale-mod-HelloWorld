package com.jjeanniard.plugins.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nonnull;

public class WelcomeToServerConfig {
    @Nonnull
    private String firstJoin = "Welcome {player} to the server!";
    @Nonnull
    private String rejoin = "Welcome back, {player}!";

    // Codec spécifique pour cette sous-section
    public static final Codec<WelcomeToServerConfig> CODEC = BuilderCodec.builder(WelcomeToServerConfig.class, WelcomeToServerConfig::new)
            .append(new KeyedCodec<>("FirstJoin", Codec.STRING), (c, v, i) -> c.firstJoin = v, (c, i) -> c.firstJoin)
            .add()
            .append(new KeyedCodec<>("Rejoin", Codec.STRING), (c, v, i) -> c.rejoin = v, (c, i) -> c.rejoin)
            .add()
            .build();

    public @NonNull String getFirstJoin() {
        return firstJoin;
    }

    public void setFirstJoin(@NonNull String firstJoin) {
        this.firstJoin = firstJoin;
    }

    public @NonNull String getRejoin() {
        return rejoin;
    }

    public void setRejoin(@NonNull String rejoin) {
        this.rejoin = rejoin;
    }
}

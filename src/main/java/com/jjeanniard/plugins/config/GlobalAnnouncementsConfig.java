package com.jjeanniard.plugins.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.codec.validation.Validators;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nonnull;

/**
 *
 */
public class GlobalAnnouncementsConfig {
    private int interval = 300; // Intervalle global pour toutes les annonces (en secondes)
    @Nonnull
    private String[] announcementArray = {"Astuce : Vérifiez votre inventaire avant l'exploration !", "Événement : Aucun événement n'est prévu.", "Maintenance quotidienne à 03h00"}; // Une seule liste d'annonces

    public static final Codec<GlobalAnnouncementsConfig> CODEC = BuilderCodec.builder(GlobalAnnouncementsConfig.class, GlobalAnnouncementsConfig::new)
            .append(new KeyedCodec<>("CooldownSeconds", Codec.INTEGER),
                    (c, v, i) -> c.interval = v,
                    (c, i) -> c.interval)
            .addValidator(Validators.nonNull())
            .add()
            .append(new KeyedCodec<>("Announcements", new ArrayCodec<>(Codec.STRING, String[]::new)),
                    (c, v, i) -> {
                        c.announcementArray = v;
                    },
                    (c, i) -> c.announcementArray)
            .add()
            .build();

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String[] getAnnouncementArray() {
        return announcementArray;
    }

    public void setStringArray(@NonNull String[] stringArray) {
        this.announcementArray = stringArray;
    }

}

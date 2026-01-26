package com.jjeanniard.plugins.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

import javax.annotation.Nonnull;

/**
 * Représente la structure exacte de notre fichier 'welcome.json'.
 * Cette classe sert de "moule" pour transformer le texte JSON en objets Java utilisables.
 */
public class MyConfig {

    // Ces variables contiendront les données du fichier.
    public WelcomeConfig welcome = new WelcomeConfig();
    public AnnouncementsConfig announcements = new AnnouncementsConfig();

    /**
     * Le CODEC est le "traducteur".
     * Il explique à Hytale comment lire le JSON ligne par ligne pour remplir notre classe.
     * 
     * Structure :
     * - "Welcome" dans le JSON -> remplit l'objet 'welcome'
     * - "Announcements" dans le JSON -> remplit l'objet 'announcements'
     */
    public static final BuilderCodec<MyConfig> CODEC = BuilderCodec.builder(MyConfig.class, MyConfig::new)
            .append(new KeyedCodec<>("Welcome", WelcomeConfig.CODEC),
                    (config, value, info) -> config.welcome = value,
                    (config, info) -> config.welcome)
            .add()
            .append(new KeyedCodec<>("Announcements", AnnouncementsConfig.CODEC),
                    (config, value, info) -> config.announcements = value,
                    (config, info) -> config.announcements)
            .add()
            .build();

    /**
     * Sous-section pour les messages de bienvenue.
     */
    public static class WelcomeConfig {
        @Nonnull
        public String firstJoin = "Welcome {player} to the server!";
        @Nonnull
        public String rejoin = "Welcome back, {player}!";

        // Codec spécifique pour cette sous-section
        public static final Codec<WelcomeConfig> CODEC = BuilderCodec.builder(WelcomeConfig.class, WelcomeConfig::new)
                .append(new KeyedCodec<>("FirstJoin", Codec.STRING), (c, v, i) -> c.firstJoin = v, (c, i) -> c.firstJoin)
                .add()
                .append(new KeyedCodec<>("Rejoin", Codec.STRING), (c, v, i) -> c.rejoin = v, (c, i) -> c.rejoin)
                .add()
                .build();

    }

    /**
     * Sous-section pour les annonces automatiques.
     */
    public static class AnnouncementsConfig {
        private int cooldownSeconds = 300; // Par défaut 5 minutes
        private String[] messages = { "Message par défaut 1", "Message par défaut 2" };

        public static final Codec<AnnouncementsConfig> CODEC = BuilderCodec.builder(AnnouncementsConfig.class, AnnouncementsConfig::new)
                .append(new KeyedCodec<>("CooldownSeconds", Codec.INTEGER), (c, v, i) -> c.cooldownSeconds = v, (c, i) -> c.cooldownSeconds)
                .add()
                // Codec.STRING_ARRAY gère automatiquement les listes JSON ["a", "b"]
                .append(new KeyedCodec<>("Messages", Codec.STRING_ARRAY), (c, v, i) -> c.messages = v, (c, i) -> c.messages)
                .add()
                .build();


        public int getCooldownSeconds() {
            return cooldownSeconds;
        }

        @Nonnull
        public String[] getMessages() {
            return messages;
        }
    }
}

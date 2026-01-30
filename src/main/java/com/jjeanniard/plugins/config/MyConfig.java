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
    public WelcomeToServerConfig welcomeConfig = new WelcomeToServerConfig();
    public GlobalAnnouncementsConfig globalAnnouncementsConfig = new GlobalAnnouncementsConfig();
    public PortalConfig portal = new PortalConfig();

    /**
     * Le CODEC principal est le "traducteur" pour toute la configuration.
     * Il explique à Hytale comment lire le JSON pour remplir notre classe MyConfig.
     */
    public static final BuilderCodec<MyConfig> CODEC = BuilderCodec.builder(MyConfig.class, MyConfig::new)
            .append(new KeyedCodec<>("Welcome", WelcomeToServerConfig.CODEC),
                    (config, value, info) -> {
                        config.welcomeConfig.setFirstJoin(value.getFirstJoin());
                        config.welcomeConfig.setRejoin(value.getRejoin());
                    },
                    (config, info) -> config.welcomeConfig)
            .add()
            .append(new KeyedCodec<>("GlobalsAnnouncements", GlobalAnnouncementsConfig.CODEC),
                    (config, value, info) -> config.globalAnnouncementsConfig = value,
                    (config, info) -> config.globalAnnouncementsConfig)
            .add()
            .append(new KeyedCodec<>("Portal", PortalConfig.CODEC),
                    (config, value, info) -> config.portal = value,
                    (config, info) -> config.portal)
            .add()
            .build();

    /**
     * Configuration pour les portails.
     */
    public static class PortalConfig {
        @Nonnull
        public String title = "Voyage vers {world}";
        @Nonnull
        public String subtitle = "Préparez-vous !";

        public static final Codec<PortalConfig> CODEC = BuilderCodec.builder(PortalConfig.class, PortalConfig::new)
                .append(new KeyedCodec<>("Title", Codec.STRING), (c, v, i) -> c.title = v, (c, i) -> c.title)
                .add()
                .append(new KeyedCodec<>("Subtitle", Codec.STRING), (c, v, i) -> c.subtitle = v, (c, i) -> c.subtitle)
                .add()
                .build();
    }


}

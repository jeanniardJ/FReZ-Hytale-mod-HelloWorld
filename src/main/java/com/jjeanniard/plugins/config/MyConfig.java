package com.jjeanniard.plugins.config;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

/**
 * Représente la structure exacte de notre fichier 'welcome.json'.
 * Cette classe sert de "moule" pour transformer le texte JSON en objets Java utilisables.
 */
public class MyConfig {

    // Ces variables contiendront les données du fichier.
    public WelcomeToServerConfig welcomeConfig = new WelcomeToServerConfig();
    public GlobalAnnouncementsConfig globalAnnouncementsConfig = new GlobalAnnouncementsConfig();
    public UniverseAnnouncementsConfig announceUniverseConfig = new UniverseAnnouncementsConfig();

    /**
     * Le CODEC principal est le "traducteur" pour toute la configuration.
     * Il explique à Hytale comment lire le JSON pour remplir notre classe MyConfig.
     */
    public static final BuilderCodec<MyConfig> CODEC = BuilderCodec.builder(MyConfig.class, MyConfig::new)
            .append(new KeyedCodec<>("Welcome", WelcomeToServerConfig.CODEC),
                    (config, value, info) -> config.welcomeConfig = value,
                    (config, info) -> config.welcomeConfig)
            .add()
            .append(new KeyedCodec<>("GlobalsAnnouncements", GlobalAnnouncementsConfig.CODEC),
                    (config, value, info) -> config.globalAnnouncementsConfig = value,
                    (config, info) -> config.globalAnnouncementsConfig)
            .add()
            .append(new KeyedCodec<>("AnnouncementUniverse", UniverseAnnouncementsConfig.CODEC),
                    (config, value, info) -> config.announceUniverseConfig = value,
                    (config, info) -> config.announceUniverseConfig)
            .add()
            .build();
}

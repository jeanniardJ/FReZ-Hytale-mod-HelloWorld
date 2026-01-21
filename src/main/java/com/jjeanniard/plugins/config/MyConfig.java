package com.jjeanniard.plugins.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

import javax.annotation.Nonnull;

public class MyConfig {

    public WelcomeConfig welcome = new WelcomeConfig();
    public AnnouncementsConfig announcements = new AnnouncementsConfig();

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

    public static class WelcomeConfig {
        @Nonnull
        public String firstJoin = "Welcome {player} to the server!";
        @Nonnull
        public String rejoin = "Welcome back, {player}!";

        public static final Codec<WelcomeConfig> CODEC = BuilderCodec.builder(WelcomeConfig.class, WelcomeConfig::new)
                .append(new KeyedCodec<>("FirstJoin", Codec.STRING), (c, v, i) -> c.firstJoin = v, (c, i) -> c.firstJoin)
                .add()
                .append(new KeyedCodec<>("Rejoin", Codec.STRING), (c, v, i) -> c.rejoin = v, (c, i) -> c.rejoin)
                .add()
                .build();

    }

    public static class AnnouncementsConfig {
        private int cooldownSeconds = 300;
        private String[] messages = { "Astuce: Vérifiez votre inventaire avant l'exploration !", "Maintenance prévue demain 03h00", "ok" };

        public static final Codec<AnnouncementsConfig> CODEC = BuilderCodec.builder(AnnouncementsConfig.class, AnnouncementsConfig::new)
                .append(new KeyedCodec<>("CooldownSeconds", Codec.INTEGER), (c, v, i) -> c.cooldownSeconds = v, (c, i) -> c.cooldownSeconds)
                .add()
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

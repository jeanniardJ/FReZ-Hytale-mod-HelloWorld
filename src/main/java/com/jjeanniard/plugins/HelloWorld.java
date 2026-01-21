package com.jjeanniard.plugins;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;

import com.jjeanniard.plugins.config.MyConfig;
import com.jjeanniard.plugins.listeners.PlayerListener;
import com.jjeanniard.plugins.services.GlobalAnnouncementService;
import com.jjeanniard.plugins.services.WelcomeService;

import javax.annotation.Nonnull;

public class HelloWorld extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
    private final Config<MyConfig> config;
    private GlobalAnnouncementService announcementService;

    public HelloWorld(@Nonnull JavaPluginInit init) {
        super(init);
        // Enregistrement de la configuration
        this.config = withConfig("welcome", MyConfig.CODEC);
        LOGGER.atInfo().log("Plugin " + this.getName() + " loaded.");
    }

    @Override
    public void start() {
        try {
            // On essaie de récupérer la configuration. C'est ici que l'erreur peut se produire.
            MyConfig loadedConfig = config.get();

            // Initialisation des services avec la configuration chargée
            this.announcementService = new GlobalAnnouncementService(loadedConfig.announcements);
            WelcomeService welcomeService = new WelcomeService(loadedConfig);

            // Enregistrement des événements
            getEventRegistry().register(PlayerConnectEvent.class, new PlayerListener(welcomeService)::onPlayerJoin);
            
            // Démarrage des tâches de fond
            announcementService.start();
            
            LOGGER.atInfo().log("Plugin enabled successfully!");

        } catch (IllegalStateException e) {
            LOGGER.atWarning().withCause(e).log(
                "Failed to load 'welcome.json'. Please check for syntax errors or missing values. The plugin will not start."
            );
            // En cas d'erreur, on ne fait rien d'autre, donc le plugin est effectivement désactivé.
        }
    }

    @Override
    public void shutdown() {
        // Arrêt propre des services
        if (announcementService != null) {
            announcementService.stop();
        }
        LOGGER.atInfo().log("Plugin disabled!");
    }
}

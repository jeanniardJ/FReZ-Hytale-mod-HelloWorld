package com.jjeanniard.mods;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.util.Config;

import javax.annotation.Nonnull;

public class HelloWorld extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private final Config<MyConfig> config;
    private WelcomeService welcomeService;
    
    /**
     * Constructor - Called when plugin is loaded.
     */
    public HelloWorld(@Nonnull JavaPluginInit init) {
        super(init);
        config = withConfig("Welcome", MyConfig.CODEC);
        LOGGER.atInfo().log("Hello from " + this.getName() + " version " + this.getManifest().getVersion().toString());
    }

    /**
     * Called when plugin is enabled.
     */
    @Override
    protected void setup() {
        this.welcomeService = new WelcomeService(config.get());
        LOGGER.atInfo().log("Setting up the plugin...");
    }

    /**
     * Called when plugin is enabled.
     */
    @Override
    public void start() {
        // Plugin initialization code

        // TODO: Initialize your plugin here
        // - Load configuration
        // - Register commands
        // - Start services
        getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
        getEventRegistry().register(PlayerConnectEvent.class, this::onPlayerJoin);
               
        
        LOGGER.atInfo().log("Plugin enabled!");
    }

    public void onPlayerJoin(PlayerConnectEvent event) {
        PlayerRef player = event.getPlayerRef();
        String message = "Player joined: " + player.getUsername();
        player.sendMessage(Message.raw(message));

        LOGGER.atInfo().log("Player joined: " + player.getUsername());
    }
    /**
     * Called when plugin is disabled.
     */
    @Override
    public void shutdown() {
        

        // TODO: Cleanup your plugin here
        // - Save data
        // - Stop services
        // - Close connections
        LOGGER.atInfo().log("Plugin disabled!");
    }
}

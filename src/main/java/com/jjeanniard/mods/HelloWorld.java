package com.jjeanniard.mods;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import javax.annotation.Nonnull;


public class HelloWorld extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    /**
     * Constructor - Called when plugin is loaded.
     */
    public HelloWorld(@Nonnull JavaPluginInit init) {
        super(init);

        // Plugin initialization code
        LOGGER.atInfo().log("Hello from " + this.getName() + " version " + this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up plugin " + this.getName());
        this.getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
    }

    /**
     * Called when plugin is enabled.
     */
    public void onEnable() {
        System.out.println("[TemplatePlugin] Plugin enabled!");

        // TODO: Initialize your plugin here
        // - Load configuration
        // - Register event listeners
        // - Register commands
        // - Start services
    }

    /**
     * Called when plugin is disabled.
     */
    public void onDisable() {
        System.out.println("[TemplatePlugin] Plugin disabled!");

        // TODO: Cleanup your plugin here
        // - Save data
        // - Stop services
        // - Close connections
    }
}

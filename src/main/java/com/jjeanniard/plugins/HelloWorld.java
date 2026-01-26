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

/**
 * Point d'entrée principal du plugin.
 * C'est ici que Hytale charge et lance notre code.
 */
public class HelloWorld extends JavaPlugin {
    // Le Logger permet d'écrire dans la console du serveur de manière propre.
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
    // Instance unique du plugin (Singleton) accessible partout.
    private static HelloWorld instance;

    // Conteneur de la configuration (ne contient pas encore les données au début).
    private final Config<MyConfig> config;
    
    // Nos services (logique métier).
    private GlobalAnnouncementService announcementService;

    /**
     * Constructeur : Appelé quand Hytale DÉCOUVRE le plugin (avant le démarrage du serveur).
     * Règle d'or : Ne jamais lancer de logique lourde ici. On ne fait qu'enregistrer les fichiers.
     */
    public HelloWorld(@Nonnull JavaPluginInit init) {
        super(init);
        
        // On sauvegarde l'instance pour pouvoir y accéder via getInstance()
        instance = this;
        
        // On dit à Hytale : "Ce plugin utilise un fichier 'welcome.json' qui respecte le format défini dans MyConfig.CODEC".
        // Note : À ce stade, le fichier n'est pas encore lu !
        this.config = withConfig("welcome", MyConfig.CODEC);
        
        LOGGER.atInfo().log("Plugin " + this.getName() + " chargé en mémoire.");
    }

    /**
     * Récupère l'instance unique du plugin.
     * Permet d'accéder au plugin depuis n'importe quelle autre classe.
     * @return L'instance de HelloWorld.
     */
    public static HelloWorld getInstance() {
        return instance;
    }

    /**
     * Méthode start() : Appelée quand le serveur a fini de charger et est PRÊT.
     * C'est ici qu'on lance tout.
     */
    @Override
    public void start() {
        try {
            // 1. Chargement effectif de la configuration
            // config.get() lit le fichier JSON sur le disque et le transforme en objet Java 'MyConfig'.
            MyConfig loadedConfig = config.get();

            // 2. Initialisation des services (Injection de dépendances)
            // On donne aux services juste ce dont ils ont besoin (la config).
            this.announcementService = new GlobalAnnouncementService(loadedConfig.announcements);
            WelcomeService welcomeService = new WelcomeService(loadedConfig);

            // 3. Enregistrement des Écouteurs (Listeners)
            // On connecte l'événement "PlayerConnectEvent" à notre méthode "onPlayerJoin".
            getEventRegistry().register(PlayerConnectEvent.class, new PlayerListener(welcomeService)::onPlayerJoin);
            
            // 4. Démarrage des tâches automatiques (Timer)
            announcementService.start();
            
            LOGGER.atInfo().log("Plugin démarré avec succès !");

        } catch (IllegalStateException e) {
            // Gestion d'erreur : Si le fichier JSON est mal formé, on attrape l'erreur ici pour ne pas faire crasher tout le serveur.
            LOGGER.atWarning().withCause(e).log(
                "ERREUR CRITIQUE : Impossible de charger 'welcome.json'. Vérifiez la syntaxe du fichier. Le plugin est désactivé."
            );
        }
    }

    /**
     * Méthode shutdown() : Appelée quand le serveur s'éteint.
     * Important pour nettoyer la mémoire et arrêter les threads.
     */
    @Override
    public void shutdown() {
        // On vérifie si le service existe (au cas où le start() aurait échoué)
        if (announcementService != null) {
            announcementService.stop();
        }
        LOGGER.atInfo().log("Plugin éteint.");
    }

    /**
     * Logger
     */

    public HytaleLogger setLog(String )
}

package com.jjeanniard.plugins;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.DrainPlayerFromWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import com.jjeanniard.plugins.config.MyConfig;
import com.jjeanniard.plugins.listeners.PlayerListener;
import com.jjeanniard.plugins.providers.GlobalAnnouncementProvider;
import com.jjeanniard.plugins.providers.UniverseAnnouncementProvider;
import com.jjeanniard.plugins.services.AnnouncementManagerService;
import com.jjeanniard.plugins.services.WelcomeService;

import javax.annotation.Nonnull;
import java.nio.file.Path;

/**
 * Point d'entrée principal du plugin.
 * C'est ici que Hytale charge et lance notre code.
 */
public final class HelloWorld extends JavaPlugin {

    // Instance unique du plugin (Singleton) accessible partout.
    private static HelloWorld instance;

    //Nom du fichier de config
    private static final String CONFIG_FILE_NAME = "welcome";

    // Conteneur de la configuration (ne contient pas encore les données au début).
    private final Config<MyConfig> config;

    // Nos services (logique métier).
    private AnnouncementManagerService announcementService;

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
        this.config = withConfig(CONFIG_FILE_NAME, MyConfig.CODEC);

        Log.info("Plugin " + this.getName() + " chargé en mémoire.");
    }

    /**
     * Récupère l'instance unique du plugin.
     * Permet d'accéder au plugin depuis n'importe quelle autre classe.
     * Cette methode est un pattern "Singleton".
     *
     * @return L'instance de HelloWorld.
     */
    public static HelloWorld getInstance() {
        return instance;
    }

    /**
     * Crée un fichier de configuration par défaut s'il n'existe pas.
     *
     * @param config de l'instance Config à sauvegarder.
     */
    private void ensureConfigFileExists(Config<MyConfig> config) {
        Path configPath = this.getDataDirectory().resolve(CONFIG_FILE_NAME + ".json");
        if (!configPath.toFile().exists()) {
            config.save().thenRun(() -> Log.info("Fichier de configuration créé par défaut.")).exceptionally(ex -> {
                Log.warning("Erreur lors de la création du fichier de configuration : " + ex.getMessage());
                return null;
            });
        } else {
            Log.info("Fichier de configuration chargé depuis : " + configPath);
        }
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
            ensureConfigFileExists(config);
            MyConfig cfg = config.get();

            // 2. Initialisation des services (Injection de dépendances)
            // On donne aux services juste ce dont ils ont besoin (la config).
            this.announcementService = new AnnouncementManagerService(
                    cfg.globalAnnouncementsConfig.getInterval(),
                    new GlobalAnnouncementProvider(cfg.globalAnnouncementsConfig),
                    new UniverseAnnouncementProvider(cfg.announceUniverseConfig)
            );
            WelcomeService welcomeService = new WelcomeService(cfg.welcomeConfig);

            // 3. Enregistrement des commandes
            //getCommandRegistry().registerCommand(new AnnouncementCommand());

            // 4. Enregistrement des Écouteurs (Listeners)
            // On connecte l'événement "PlayerConnectEvent" à notre méthode "onPlayerJoin".
            getEventRegistry().register(PlayerConnectEvent.class, new PlayerListener(welcomeService)::onPlayerJoin);

            getEventRegistry().registerGlobal(DrainPlayerFromWorldEvent.class, event -> {
                Player player = event.getHolder().getComponent(Player.getComponentType());
                String worldName = event.getWorld().getName();
                Log.info("OnPlayerJoinUniver : " + event.toString());
                Log.info("Le " + player + " a rejoint le monde : " + worldName);

            });

            // 5. Démarrage des tâches automatiques (Timer)
            announcementService.start();

            Log.info("Plugin démarré avec succès !");

        } catch (IllegalStateException e) {
            // Gestion d'erreur : Si le fichier JSON est mal formé, on attrape l'erreur ici pour ne pas faire crasher tout le serveur.
            Log.severe("Erreur lors du chargement de la configuration : " + e.getMessage());
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

        Log.info("Plugin éteint.");
    }

    /**
     * Redémarre le service d'annonces pour appliquer les changements de configuration.
     */
    public void restartAnnouncementService() {
        if (announcementService != null) {
            announcementService.stop();
        }
        // Récrée et démarre le service avec la configuration mise à jour
        this.announcementService = new AnnouncementManagerService(config.get().globalAnnouncementsConfig.getInterval());
        announcementService.start();
        Log.info("Service d'annonces redémarré.");
    }
}

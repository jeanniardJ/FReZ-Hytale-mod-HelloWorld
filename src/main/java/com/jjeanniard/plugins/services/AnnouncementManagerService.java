package com.jjeanniard.plugins.services;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jjeanniard.plugins.Log;
import com.jjeanniard.plugins.providers.Announcement;
import com.jjeanniard.plugins.providers.AnnouncementProvider;
import com.jjeanniard.plugins.ui.AnnouncementPanelPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Service gérant la diffusion et la gestion des annonces.
 * Responsabilités :
 * - Stocker la liste des annonces actives.
 * - Diffuser périodiquement une annonce aléatoire.
 * - Fournir les méthodes CRUD pour l'interface utilisateur.
 */
public class AnnouncementManagerService {
    private final int interval;
    // Le "Scheduler" est notre horloge interne.
    private final ScheduledExecutorService scheduler;

    // La tâche planifiée (pour pouvoir l'annuler plus tard).
    private ScheduledFuture<?> scheduledTask;

    private List<Announcement> announcements = new ArrayList<>();

    private final List<AnnouncementProvider> providers;

    // Injection de dépendance : On lui donne la config, il en extrait ce qu'il veut.
    public AnnouncementManagerService(int interval, AnnouncementProvider... announcementProvider) {
        this.interval = interval;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        // Ensure the list is mutable by wrapping it in ArrayList
        this.announcements = new ArrayList<>(Arrays.stream(announcementProvider)
                .flatMap(provider -> provider.getAnnouncements().stream())
                .collect(Collectors.toList()));
        this.providers = Arrays.asList(announcementProvider);
    }

    public void start() {

        if (announcements == null) {
            Log.warning("Aucune annonce configurée. Le service ne démarrera pas.");
            return;
        }

        Log.info("Démarrage du service d'annonces (Intervalle: " + this.interval + " secondes)");
        // On dit au scheduler : "Exécute la méthode 'broadcastNextMessage' toutes les X secondes".
        this.scheduledTask = scheduler.scheduleAtFixedRate(
                this::broadcastNextMessage, // La méthode à appeler
                interval,                   // Délai avant la première exécution
                interval,                   // Délai entre chaque exécution suivante
                TimeUnit.SECONDS            // Unité de temps
        );
    }

    public void stop() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(true);
        }
    }

    private void broadcastNextMessage() {
        String worldNameAnnounce = null;
        try {
            if (announcements.isEmpty()) return;

            // On choisit un message au hasard
            int random = (int) (Math.random() * announcements.size());
            Announcement announcement = announcements.get(random);

            if (announcement.targetWorld() != null && !announcement.targetWorld().isEmpty()) {
                World worldOpt = Universe.get().getWorld(announcement.targetWorld());
                worldNameAnnounce = announcement.targetWorld();
                if (worldOpt == null) {
                    Log.warning("Une liste d'annonce à été créé pour un univers, mais cette univers '" + announcement.targetWorld() + "' n'existe pas !");
                    return;
                }

                worldOpt.getPlayerRefs().forEach(playerRef -> {
                    playerRef.sendMessage(Message.raw("[Annonce]: " + formattedRaw(announcement.message())));
                });
            } else {
                worldNameAnnounce = "Global";
                Universe.get().sendMessage(Message.raw("[Annonce]: " + formattedRaw(announcement.message())));
            }

            Log.debug("Annonce envoyée : %s pour l'univers " + worldNameAnnounce, formattedRaw(announcement.message()));
        } catch (Exception e) {
            Log.warning("Erreur dans la boucle d'annonce : %s", e.getMessage());
        }
    }

    private String formattedRaw(String message) {
        int onlinePlayerCount = Universe.get().getPlayerCount();
        int maxPlayers = HytaleServer.get().getConfig().getMaxPlayers();

        if (message.contains("{online}")) {
            return message.replace("{online}", String.valueOf(onlinePlayerCount));
        }

        if (message.contains("{playersMax}")) {
            return message.replace("{playersMax}", String.valueOf(maxPlayers));
        }

        return message;
    }

    /**
     * Recharge la liste des annonces depuis les providers (fichiers de config)
     * sans arrêter le timer de diffusion.
     */
    public void reload() {
        try {
            // On crée une nouvelle liste temporaire pour éviter les problèmes de concurrence
            // si le timer essaie de lire la liste pendant qu'on la vide.
            List<Announcement> newAnnouncements = new ArrayList<>();

            // On récupère la configuration fraîchement rechargée depuis le disque
            com.jjeanniard.plugins.HelloWorld plugin = com.jjeanniard.plugins.HelloWorld.getInstance();
            if (plugin != null) {
                com.jjeanniard.plugins.config.MyConfig config = plugin.getConfigData();
                
                // On recrée temporairement les providers avec la nouvelle config pour extraire les annonces
                AnnouncementProvider globalProvider = new com.jjeanniard.plugins.providers.GlobalAnnouncementProvider(config.globalAnnouncementsConfig);
                AnnouncementProvider universeProvider = new com.jjeanniard.plugins.providers.UniverseAnnouncementProvider(config.announceUniverseConfig);
                
                newAnnouncements.addAll(globalProvider.getAnnouncements());
                newAnnouncements.addAll(universeProvider.getAnnouncements());
            } else {
                // Fallback sur les providers existants (qui peuvent avoir une vieille config)
                for (AnnouncementProvider announcementProvider : providers) {
                    newAnnouncements.addAll(announcementProvider.getAnnouncements());
                }
            }
            
            // On remplace la liste atomiquement (ou presque, c'est une référence)
            this.announcements = newAnnouncements;

        } catch (Exception e) {
            Log.warning("Erreur lors du rechargement des annonces : %s", e.getMessage());
            return;
        }
        
        Log.info("Annonces rechargées ! Total : " + announcements.size());
    }

    public void list(CommandContext commandContext) {
        for (Announcement announcements : announcements) {
            //Il faut lister par type les annonces
            commandContext.sendMessage(Message.raw(announcements.toString()));
        }
    }

    /**
     * Ouvre le panneau de gestion des annonces pour le joueur.
     * Cette méthode respecte l'architecture ECS en récupérant le monde via le Store de l'entité.
     */
    public void sendManagementPanel(CommandContext ctx) {
        if (!ctx.isPlayer()) {
            ctx.sendMessage(Message.raw("Cette commande doit être exécutée par un joueur."));
            return;
        }
        
        // On récupère le joueur et son monde
        Player playerComponent = ctx.senderAs(Player.class);
        Ref<EntityStore> ref = ctx.senderAsPlayerRef();
        Store<EntityStore> store = ref.getStore();
        
        // IMPORTANT : On récupère le monde depuis le Store de l'entité
        World world = store.getExternalData().getWorld();

        // On exécute la logique sur le thread du monde pour éviter l'erreur "Assert not in thread!"
        world.execute(() -> {
            // Récupération du composant PlayerRef depuis le Store (maintenant safe)
            PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());

            AnnouncementPanelPage page = new AnnouncementPanelPage(playerRef, this);
            playerComponent.getPageManager().openCustomPage(ref, store, page);
        });
    }

    public List<Announcement> getAllAnnouncements() {
        return new ArrayList<>(announcements);
    }

    public void addAnnouncement(Announcement announcement) {
        this.announcements.add(announcement);
        saveConfig();
    }

    public void updateAnnouncement(Announcement announcement) {
        for (int i = 0; i < announcements.size(); i++) {
            if (announcements.get(i).id().equals(announcement.id())) {
                announcements.set(i, announcement);
                saveConfig();
                return;
            }
        }
    }

    public void deleteAnnouncement(UUID id) {
        announcements.removeIf(a -> a.id().equals(id));
        saveConfig();
    }

    public Announcement getAnnouncement(UUID id) {
        return announcements.stream().filter(a -> a.id().equals(id)).findFirst().orElse(null);
    }

    private void saveConfig() {
        // Mise à jour de la configuration globale
        List<String> globalMessages = new ArrayList<>();
        java.util.Map<String, List<String>> universeMessages = new java.util.HashMap<>();

        for (Announcement a : announcements) {
            if (a.targetWorld() == null || a.targetWorld().isEmpty() || "Global".equalsIgnoreCase(a.targetWorld())) {
                globalMessages.add(a.message());
            } else {
                universeMessages.computeIfAbsent(a.targetWorld(), k -> new ArrayList<>()).add(a.message());
            }
        }
        
        // Récupération de l'instance principale pour accéder à la config
        com.jjeanniard.plugins.HelloWorld plugin = com.jjeanniard.plugins.HelloWorld.getInstance();
        if (plugin != null) {
            // Sauvegarde des annonces globales
            plugin.updateGlobalAnnouncements(globalMessages);
            
            // Sauvegarde des annonces par univers
            // Conversion de Map<String, List<String>> vers Map<String, String[]>
            java.util.Map<String, String[]> universeConfigMap = new java.util.HashMap<>();
            for (java.util.Map.Entry<String, List<String>> entry : universeMessages.entrySet()) {
                universeConfigMap.put(entry.getKey(), entry.getValue().toArray(new String[0]));
            }
            plugin.updateUniverseAnnouncements(universeConfigMap);
            
        } else {
            Log.warning("Impossible de sauvegarder la configuration : Instance du plugin introuvable.");
        }
    }
}
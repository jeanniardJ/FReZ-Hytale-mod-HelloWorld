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
import com.jjeanniard.plugins.HelloWorld;
import com.jjeanniard.plugins.Log;
import com.jjeanniard.plugins.config.MyConfig;
import com.jjeanniard.plugins.providers.Announcement;
import com.jjeanniard.plugins.providers.AnnouncementProvider;
import com.jjeanniard.plugins.providers.GlobalAnnouncementProvider;
import com.jjeanniard.plugins.providers.UniverseAnnouncementProvider;
import com.jjeanniard.plugins.ui.AnnouncementPanelPage;

import java.util.*;
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
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledTask;
    private List<Announcement> announcements;

    public AnnouncementManagerService(int interval, AnnouncementProvider... announcementProvider) {
        this.interval = interval;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        List<AnnouncementProvider> providers = Arrays.asList(announcementProvider);
        this.announcements = loadAnnouncementsFromProviders(providers);
    }

    private List<Announcement> loadAnnouncementsFromProviders(List<AnnouncementProvider> providers) {
        return providers.stream()
                .flatMap(provider -> provider.getAnnouncements().stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void start() {
        if (announcements.isEmpty()) {
            Log.warning("Aucune annonce configurée. Le service ne démarrera pas.");
            return;
        }

        Log.info("Démarrage du service d'annonces (Intervalle: " + this.interval + " secondes)");
        this.scheduledTask = scheduler.scheduleAtFixedRate(
                this::broadcastNextMessage,
                interval,
                interval,
                TimeUnit.SECONDS
        );
    }

    public void stop() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(true);
        }
    }

    private void broadcastNextMessage() {
        try {
            if (announcements.isEmpty()) return;

            Announcement announcement = announcements.get((int) (Math.random() * announcements.size()));
            String targetWorld = announcement.targetWorld();
            String formattedMessage = formattedRaw(announcement.message());

            if (targetWorld != null && !targetWorld.isEmpty() && !"Global".equalsIgnoreCase(targetWorld)) {
                broadcastToWorld(targetWorld, formattedMessage);
            } else {
                broadcastGlobal(formattedMessage);
            }

            Log.debug("Annonce envoyée : %s pour l'univers %s", formattedMessage, targetWorld == null ? "Global" : targetWorld);
        } catch (Exception e) {
            Log.warning("Erreur dans la boucle d'annonce : %s", e.getMessage());
        }
    }

    private void broadcastToWorld(String worldName, String message) {
        World world = Universe.get().getWorld(worldName);
        if (world == null) {
            Log.warning("Une annonce cible l'univers '" + worldName + "' qui n'existe pas !");
            return;
        }
        world.getPlayerRefs().forEach(playerRef -> playerRef.sendMessage(Message.raw("[Annonce]: " + message)));
    }

    private void broadcastGlobal(String message) {
        Universe.get().sendMessage(Message.raw("[Annonce]: " + message));
    }

    private String formattedRaw(String message) {
        int onlinePlayerCount = Universe.get().getPlayerCount();
        int maxPlayers = HytaleServer.get().getConfig().getMaxPlayers();

        return message.replace("{online}", String.valueOf(onlinePlayerCount))
                .replace("{playersMax}", String.valueOf(maxPlayers));
    }

    public void reload() {
        try {
            HelloWorld plugin = HelloWorld.getInstance();
            if (plugin == null) {
                Log.warning("Impossible de recharger : Instance du plugin introuvable.");
                return;
            }

            MyConfig config = plugin.getConfigData();
            List<AnnouncementProvider> newProviders = Arrays.asList(
                    new GlobalAnnouncementProvider(config.globalAnnouncementsConfig),
                    new UniverseAnnouncementProvider(config.announceUniverseConfig)
            );

            this.announcements = loadAnnouncementsFromProviders(newProviders);
            Log.info("Annonces rechargées ! Total : " + announcements.size());

        } catch (Exception e) {
            Log.warning("Erreur lors du rechargement des annonces : %s", e.getMessage());
        }
    }

    public void list(CommandContext commandContext) {
        for (Announcement announcement : announcements) {
            commandContext.sendMessage(Message.raw(announcement.toString()));
        }
    }

    public void sendManagementPanel(CommandContext ctx) {
        if (!ctx.isPlayer()) {
            ctx.sendMessage(Message.raw("Cette commande doit être exécutée par un joueur."));
            return;
        }

        Player playerComponent = ctx.senderAs(Player.class);
        Ref<EntityStore> ref = ctx.senderAsPlayerRef();
        assert ref != null;
        Store<EntityStore> store = ref.getStore();
        World world = store.getExternalData().getWorld();

        world.execute(() -> {
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
        if (announcements.removeIf(a -> a.id().equals(id))) {
            saveConfig();
        }
    }

    public boolean deleteAnnouncementByIndex(int index) {
        if (index >= 0 && index < announcements.size()) {
            announcements.remove(index);
            saveConfig();
            return true;
        }
        return false;
    }

    public Announcement getAnnouncementByIndex(int index) {
        if (index >= 0 && index < announcements.size()) {
            return announcements.get(index);
        }
        return null;
    }

    public Announcement getAnnouncement(UUID id) {
        return announcements.stream()
                .filter(a -> a.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    private void saveConfig() {
        HelloWorld plugin = HelloWorld.getInstance();
        if (plugin == null) {
            Log.warning("Impossible de sauvegarder la configuration : Instance du plugin introuvable.");
            return;
        }

        List<String> globalMessages = new ArrayList<>();
        Map<String, List<String>> universeMessages = new HashMap<>();

        for (Announcement a : announcements) {
            if (a.targetWorld() == null || a.targetWorld().isEmpty() || "Global".equalsIgnoreCase(a.targetWorld())) {
                globalMessages.add(a.message());
            } else {
                universeMessages.computeIfAbsent(a.targetWorld(), k -> new ArrayList<>()).add(a.message());
            }
        }

        plugin.updateGlobalAnnouncements(globalMessages);

        Map<String, String[]> universeConfigMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : universeMessages.entrySet()) {
            universeConfigMap.put(entry.getKey(), entry.getValue().toArray(new String[0]));
        }
        plugin.updateUniverseAnnouncements(universeConfigMap);
    }
}
package com.jjeanniard.plugins.services;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.jjeanniard.plugins.Log;
import com.jjeanniard.plugins.providers.Announcement;
import com.jjeanniard.plugins.providers.AnnouncementProvider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AnnouncementManagerService {
    private final int interval;
    // Le "Scheduler" est notre horloge interne.
    private final ScheduledExecutorService scheduler;

    // La tâche planifiée (pour pouvoir l'annuler plus tard).
    private ScheduledFuture<?> scheduledTask;

    private final List<Announcement> announcements;

    // Injection de dépendance : On lui donne la config, il en extrait ce qu'il veut.
    public AnnouncementManagerService(int interval, AnnouncementProvider... announcementProvider) {
        this.interval = interval;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.announcements = Arrays.stream(announcementProvider).flatMap(provider -> provider.getAnnouncements().stream()).toList();
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

            // On choisit un message au hasard
            int random = (int) (Math.random() * announcements.size());
            Announcement announcement = announcements.get(random);

            if (announcement.targetWorld() != null) {
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

            Log.info("Annonce envoyée : %s pour l'univers " + worldNameAnnounce, formattedRaw(announcement.message()));
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
}

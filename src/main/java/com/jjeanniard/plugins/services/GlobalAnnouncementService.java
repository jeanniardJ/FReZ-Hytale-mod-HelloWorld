package com.jjeanniard.plugins.services;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.Universe;
import com.jjeanniard.plugins.Log;
import com.jjeanniard.plugins.config.GlobalAnnouncementsConfig;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Service responsable de la diffusion des annonces globales.
 * Utilise un thread séparé pour compter le temps sans ralentir le jeu.
 */
public class GlobalAnnouncementService {

    private final String[] globalAnnouncements;

    private final int interval;

    // Le "Scheduler" est notre horloge interne.
    private final ScheduledExecutorService scheduler;

    // La tâche planifiée (pour pouvoir l'annuler plus tard).
    private ScheduledFuture<?> scheduledTask;

    public GlobalAnnouncementService(GlobalAnnouncementsConfig config) {
        this.globalAnnouncements = config.getStringArray();
        this.interval = config.getInterval();
        Log.debug("Interval: " + interval);
        //Log.warning("Interval: " + interval);
        // On crée un pool d'un seul thread dédié à ce service.
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Démarre la boucle d'annonces.
     */
    public void start() {
        if (this.globalAnnouncements == null || this.globalAnnouncements.length == 0) {
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

    /**
     * Arrête proprement le service.
     * CRUCIAL : Si on n'arrête pas le scheduler, le thread continuera de tourner même si le serveur s'arrête !
     */
    public void stop() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(true);
        }
        scheduler.shutdown();
        Log.info("Service d'annonces arrêté.");
    }

    /**
     * Logique principale : Choisit le message et l'envoie.
     */
    private void broadcastNextMessage() {
        try {

            // On choisit un message au hasard
            String announcement = globalAnnouncements[(int) (Math.random() * globalAnnouncements.length)];

            // On envoie le message à l'Univers entier (tous les joueurs connectés)
            Universe.get().sendMessage(Message.raw("[Annonce]: " + announcement));

            Log.info("Annonce envoyée : %s", announcement);
        } catch (Exception e) {
            Log.warning("Erreur dans la boucle d'annonce : %s", e.getMessage());
        }
    }

    /**
     * Remplace les variables dynamiques ({online}) par leurs vraies valeurs.
     */
    private String formatMessage(String message) {
        String result = message;
        if (result.contains("{online}")) {
            result = result.replace("{online}", String.valueOf(Universe.get().getPlayerCount()));
        }
        return result;
    }
}

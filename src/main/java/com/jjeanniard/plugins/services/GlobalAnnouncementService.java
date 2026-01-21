package com.jjeanniard.plugins.services;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.Universe;
import com.jjeanniard.plugins.config.MyConfig;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service responsable de la diffusion automatique des annonces globales.
 */
public class GlobalAnnouncementService {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final MyConfig.AnnouncementsConfig config;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledTask;
    private final AtomicInteger messageIndex;

    public GlobalAnnouncementService(MyConfig.AnnouncementsConfig config) {
        this.config = config;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.messageIndex = new AtomicInteger(0);
    }

    /**
     * Démarre la diffusion des annonces.
     */
    public void start() {
        if (config.getMessages().length == 0) {
            LOGGER.atWarning().log("Aucun message d'annonce configuré. Le service ne démarrera pas.");
            return;
        }

        long interval = config.getCooldownSeconds();
        LOGGER.atInfo().log("Démarrage du service d'annonces (Intervalle: " + interval + "s)");

        // Planifie la tâche pour s'exécuter régulièrement
        this.scheduledTask = scheduler.scheduleAtFixedRate(
                this::broadcastNextMessage,
                interval,
                interval,
                TimeUnit.SECONDS
        );
    }

    /**
     * Arrête proprement le service et le timer.
     */
    public void stop() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(true);
        }
        scheduler.shutdown();
        LOGGER.atInfo().log("Service d'annonces arrêté.");
    }

    /**
     * Logique pour envoyer le message suivant.
     */
    private void broadcastNextMessage() {
        List<String> messages = List.of(config.getMessages());
        
        // Récupère l'index actuel et le met à jour pour la prochaine fois (boucle circulaire)
        int index = messageIndex.getAndUpdate(i -> (i + 1) % messages.size());
        String rawMessage = messages.get(index);

        // Formater le message (remplacer les variables)
        String formattedMessage = formatMessage(rawMessage);

        // Envoi du message à tous les joueurs
        Universe.get().sendMessage(Message.raw(formattedMessage));

        LOGGER.atInfo().log("[ANNONCE] " + formattedMessage);
    }

    /**
     * Remplace les variables dans le message avec les vraies valeurs du serveur.
     */
    private String formatMessage(String message) {
        String result = message;
        
        // Récupération dynamique des joueurs en ligne
        if (result.contains("{online}")) {
            int onlineCount = Universe.get().getPlayerCount();
            result = result.replace("{online}", String.valueOf(onlineCount)); 
        }

        return result;
    }
}

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
 * Utilise un thread séparé pour compter le temps sans ralentir le jeu.
 */
public class GlobalAnnouncementService {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final MyConfig.AnnouncementsConfig config;
    
    // Le "Scheduler" est notre horloge interne.
    private final ScheduledExecutorService scheduler;
    
    // La tâche planifiée (pour pouvoir l'annuler plus tard).
    private ScheduledFuture<?> scheduledTask;
    
    // AtomicInteger est un entier "thread-safe". 
    // Cela évite les bugs si deux annonces essayaient de partir exactement en même temps (peu probable ici, mais bonne pratique).
    private final AtomicInteger messageIndex;

    public GlobalAnnouncementService(MyConfig.AnnouncementsConfig config) {
        this.config = config;
        // On crée un pool d'un seul thread dédié à ce service.
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.messageIndex = new AtomicInteger(0);
    }

    /**
     * Démarre la boucle d'annonces.
     */
    public void start() {
        if (config.getMessages().length == 0) {
            LOGGER.atWarning().log("Aucun message d'annonce configuré. Le service ne démarrera pas.");
            return;
        }

        long interval = config.getCooldownSeconds();
        LOGGER.atInfo().log("Démarrage du service d'annonces (Intervalle: " + interval + "s)");

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
        LOGGER.atInfo().log("Service d'annonces arrêté.");
    }

    /**
     * Logique principale : Choisit le message et l'envoie.
     */
    private void broadcastNextMessage() {
        List<String> messages = List.of(config.getMessages());
        
        // Calcul intelligent de l'index : (0 -> 1 -> 2 -> 0 -> 1 ...)
        // Le modulo (%) permet de revenir au début de la liste automatiquement.
        int index = messageIndex.getAndUpdate(i -> (i + 1) % messages.size());
        String rawMessage = messages.get(index);

        // On prépare le message (remplacement des variables)
        String formattedMessage = formatMessage(rawMessage);

        // On envoie le message à l'Univers entier (tous les joueurs connectés)
        Universe.get().sendMessage(Message.raw(formattedMessage));

        LOGGER.atInfo().log("[ANNONCE] " + formattedMessage);
    }

    /**
     * Remplace les variables dynamiques ({online}, {tps}) par leurs vraies valeurs.
     */
    private String formatMessage(String message) {
        String result = message;
        
        if (result.contains("{online}")) {
            // Récupère le nombre de joueurs en temps réel
            int onlineCount = Universe.get().getPlayerCount();
            result = result.replace("{online}", String.valueOf(onlineCount)); 
        }
        
        // Vous pouvez ajouter d'autres variables ici (ex: {time}, {server_name}...)

        return result;
    }
}

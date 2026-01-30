package com.jjeanniard.plugins.services;

import com.jjeanniard.plugins.config.AnnounceUniverseConfig;
import com.jjeanniard.plugins.config.GlobalAnnouncementsConfig;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Service simple pour gérer la logique des messages de bienvenue apres être téléporté par un portal dans un universe.
 * Et la gestion des annonces propres à chaque univers.
 */
public final class AnnouncementUniverseService {
    private final String joinMessageTitleUniverse;
    private final String joinMessageSubTitleUniverse;
    private final Map<String, String[]> globalAnnouncementsUniverses;

    private final int interval;

    // Le "Scheduler" est notre horloge interne.
    private final ScheduledExecutorService scheduler;

    // La tâche planifiée (pour pouvoir l'annuler plus tard).
    private ScheduledFuture<?> scheduledTask;

    // Injection de dépendance : On lui donne la config, il en extrait ce qu'il veut.
    public AnnouncementUniverseService(GlobalAnnouncementsConfig configGlobal, AnnounceUniverseConfig config) {
        this.joinMessageTitleUniverse = config.getTitle();
        this.joinMessageSubTitleUniverse = config.getSubtitle();
        this.globalAnnouncementsUniverses = config.getWorldAnnouncements();
        this.interval = configGlobal.getInterval();

        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public String getJoinMessageSubTitleUniverse() {
        return joinMessageSubTitleUniverse;
    }

    public String getJoinMessageTitleUniverse() {
        return joinMessageTitleUniverse;
    }

    public void start() {

    }

    public void stop() {

    }

    private void BroadcastMessageUnivers() {

    }
}
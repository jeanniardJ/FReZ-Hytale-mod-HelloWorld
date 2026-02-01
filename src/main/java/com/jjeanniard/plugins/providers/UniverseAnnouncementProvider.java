package com.jjeanniard.plugins.providers;

import com.jjeanniard.plugins.config.UniverseAnnouncementsConfig;

import java.util.ArrayList;
import java.util.List;

public class UniverseAnnouncementProvider implements AnnouncementProvider {
    private final List<Announcement> announcements;

    public UniverseAnnouncementProvider(UniverseAnnouncementsConfig config) {
        announcements = new ArrayList<Announcement>();
        for (String world : config.getWorldAnnouncements().keySet()) {
            for (String announcement : config.getWorldAnnouncements().get(world)) {
                announcements.add(new Announcement(announcement, world));
            }
        }
    }

    @Override
    public List<Announcement> getAnnouncements() {
        return announcements;
    }
}
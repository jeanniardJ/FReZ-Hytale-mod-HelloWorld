package com.jjeanniard.plugins.providers;

import com.jjeanniard.plugins.config.GlobalAnnouncementsConfig;

import java.util.ArrayList;
import java.util.List;

public class GlobalAnnouncementProvider implements AnnouncementProvider {
    private final List<Announcement> announcements;

    public GlobalAnnouncementProvider(GlobalAnnouncementsConfig config) {

        this.announcements = new ArrayList<>();

        for (String announcement : config.getAnnouncementArray()) {
            announcements.add(new Announcement(announcement, null));
        }
    }

    @Override
    public List<Announcement> getAnnouncements() {
        return announcements;
    }


}

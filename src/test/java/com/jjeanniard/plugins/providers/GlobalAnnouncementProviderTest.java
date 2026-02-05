package com.jjeanniard.plugins.providers;

import com.jjeanniard.plugins.config.GlobalAnnouncementsConfig;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GlobalAnnouncementProviderTest {

    @Test
    void testGetAnnouncements() {
        GlobalAnnouncementsConfig config = new GlobalAnnouncementsConfig();
        config.setStringArray(new String[]{"Msg 1", "Msg 2"});

        GlobalAnnouncementProvider provider = new GlobalAnnouncementProvider(config);
        List<Announcement> announcements = provider.getAnnouncements();

        assertEquals(2, announcements.size());
        assertEquals("Msg 1", announcements.get(0).message());
        assertNull(announcements.get(0).targetWorld());
    }
}

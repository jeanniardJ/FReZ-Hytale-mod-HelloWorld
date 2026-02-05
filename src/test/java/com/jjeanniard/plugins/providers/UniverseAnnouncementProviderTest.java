package com.jjeanniard.plugins.providers;

import com.jjeanniard.plugins.config.UniverseAnnouncementsConfig;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UniverseAnnouncementProviderTest {

    @Test
    void testGetAnnouncements() {
        UniverseAnnouncementsConfig config = new UniverseAnnouncementsConfig();
        Map<String, String[]> map = new HashMap<>();
        map.put("world1", new String[]{"Msg 1"});
        map.put("world2", new String[]{"Msg 2", "Msg 3"});
        config.setWorldAnnouncements(map);

        UniverseAnnouncementProvider provider = new UniverseAnnouncementProvider(config);
        List<Announcement> announcements = provider.getAnnouncements();

        assertEquals(3, announcements.size());

        // Check content (order is not guaranteed due to HashMap)
        boolean foundWorld1 = false;
        boolean foundWorld2 = false;

        for (Announcement a : announcements) {
            if ("world1".equals(a.targetWorld())) {
                assertEquals("Msg 1", a.message());
                foundWorld1 = true;
            } else if ("world2".equals(a.targetWorld())) {
                assertTrue(a.message().equals("Msg 2") || a.message().equals("Msg 3"));
                foundWorld2 = true;
            }
        }

        assertTrue(foundWorld1);
        assertTrue(foundWorld2);
    }
}

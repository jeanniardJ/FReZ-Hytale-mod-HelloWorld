package com.jjeanniard.plugins.config;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UniverseAnnouncementsConfigTest {

    @Test
    void testDefaultValues() {
        UniverseAnnouncementsConfig config = new UniverseAnnouncementsConfig();
        assertEquals("Voyage vers {world}", config.getTitle());
        assertEquals("Préparez-vous !", config.getSubtitle());
        assertNotNull(config.getWorldAnnouncements());
    }

    @Test
    void testSetters() {
        UniverseAnnouncementsConfig config = new UniverseAnnouncementsConfig();
        config.setTitle("New Title");
        config.setSubtitle("New Subtitle");

        Map<String, String[]> map = new HashMap<>();
        map.put("world1", new String[]{"msg1"});
        config.setWorldAnnouncements(map);

        assertEquals("New Title", config.getTitle());
        assertEquals("New Subtitle", config.getSubtitle());
        assertEquals(1, config.getWorldAnnouncements().size());
        assertTrue(config.getWorldAnnouncements().containsKey("world1"));
    }
}

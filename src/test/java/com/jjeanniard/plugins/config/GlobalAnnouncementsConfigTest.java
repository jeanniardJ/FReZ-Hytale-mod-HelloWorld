package com.jjeanniard.plugins.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlobalAnnouncementsConfigTest {

    @Test
    void testDefaultValues() {
        GlobalAnnouncementsConfig config = new GlobalAnnouncementsConfig();
        assertEquals(300, config.getInterval());
        assertNotNull(config.getAnnouncementArray());
        assertTrue(config.getAnnouncementArray().length > 0);
    }

    @Test
    void testSetters() {
        GlobalAnnouncementsConfig config = new GlobalAnnouncementsConfig();
        config.setInterval(60);
        config.setStringArray(new String[]{"New Announcement"});

        assertEquals(60, config.getInterval());
        assertEquals(1, config.getAnnouncementArray().length);
        assertEquals("New Announcement", config.getAnnouncementArray()[0]);
    }
}

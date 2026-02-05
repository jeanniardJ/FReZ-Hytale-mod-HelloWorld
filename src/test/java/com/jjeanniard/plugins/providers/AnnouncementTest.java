package com.jjeanniard.plugins.providers;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AnnouncementTest {

    @Test
    void testConstructorGeneratesId() {
        Announcement announcement = new Announcement("Message", "World");
        assertNotNull(announcement.id());
    }

    @Test
    void testConstructorWithId() {
        UUID id = UUID.randomUUID();
        Announcement announcement = new Announcement(id, "Message", "World");
        assertEquals(id, announcement.id());
    }

    @Test
    void testToStringReturnsMessage() {
        Announcement announcement = new Announcement("Hello World", null);
        assertEquals("Hello World", announcement.toString());
    }
}

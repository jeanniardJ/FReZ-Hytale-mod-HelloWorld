package com.jjeanniard.plugins.services;

import com.jjeanniard.plugins.providers.Announcement;
import com.jjeanniard.plugins.providers.AnnouncementProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AnnouncementManagerServiceTest {

    private AnnouncementManagerService service;
    private AnnouncementProvider mockProvider;

    @BeforeEach
    void setUp() {
        mockProvider = new AnnouncementProvider() {
            @Override
            public List<Announcement> getAnnouncements() {
                return Collections.emptyList();
            }
        };
        service = new AnnouncementManagerService(300, mockProvider);
    }

    @Test
    void testAddAnnouncement() {
        Announcement announcement = new Announcement("Test Message", "Global");
        service.addAnnouncement(announcement);

        List<Announcement> all = service.getAllAnnouncements();
        assertEquals(1, all.size());
        assertEquals("Test Message", all.get(0).message());
    }

    @Test
    void testUpdateAnnouncement() {
        Announcement original = new Announcement("Original Message", "Global");
        service.addAnnouncement(original);

        Announcement updated = new Announcement(original.id(), "Updated Message", "Global");
        service.updateAnnouncement(updated);

        Announcement retrieved = service.getAnnouncement(original.id());
        assertNotNull(retrieved);
        assertEquals("Updated Message", retrieved.message());
    }

    @Test
    void testDeleteAnnouncement() {
        Announcement announcement = new Announcement("To Delete", "Global");
        service.addAnnouncement(announcement);

        service.deleteAnnouncement(announcement.id());

        Announcement retrieved = service.getAnnouncement(announcement.id());
        assertNull(retrieved);
        assertEquals(0, service.getAllAnnouncements().size());
    }

    @Test
    void testGetAnnouncement() {
        Announcement announcement = new Announcement("Find Me", "Global");
        service.addAnnouncement(announcement);

        Announcement found = service.getAnnouncement(announcement.id());
        assertNotNull(found);
        assertEquals(announcement.id(), found.id());
    }

    @Test
    void testReload() {
        // Setup service with a provider that returns one item
        AnnouncementProvider provider = () -> List.of(new Announcement("Provider Item", null));
        service = new AnnouncementManagerService(300, provider);

        // Add a manual item (simulating runtime addition)
        service.addAnnouncement(new Announcement("Manual Item", null));
        assertEquals(2, service.getAllAnnouncements().size());

        // Reload should reset to provider's state
        service.reload();

        assertEquals(1, service.getAllAnnouncements().size());
        assertEquals("Provider Item", service.getAllAnnouncements().get(0).message());
    }
}

package com.jjeanniard.plugins.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jjeanniard.plugins.providers.Announcement;
import com.jjeanniard.plugins.services.AnnouncementManagerService;

import java.util.List;
import java.util.UUID;

public class AnnouncementPanelPage extends InteractiveCustomUIPage<AnnouncementPanelPage.PanelEventData> {

    private final AnnouncementManagerService service;

    public static class PanelEventData {
        public String action; // "ADD", "EDIT", "DELETE"
        public String targetId; // UUID of the announcement

        public static final BuilderCodec<PanelEventData> CODEC = BuilderCodec
            .builder(PanelEventData.class, PanelEventData::new)
            .append(new KeyedCodec<>("Action", Codec.STRING),
                (obj, val) -> obj.action = val,
                obj -> obj.action)
            .add()
            .append(new KeyedCodec<>("TargetId", Codec.STRING),
                (obj, val) -> obj.targetId = val,
                obj -> obj.targetId)
            .add()
            .build();
    }

    public AnnouncementPanelPage(PlayerRef playerRef, AnnouncementManagerService service) {
        super(playerRef, CustomPageLifetime.CanDismiss, PanelEventData.CODEC);
        this.service = service;
    }

    @Override
    public void build(Ref<EntityStore> ref, UICommandBuilder cmd, UIEventBuilder events, Store<EntityStore> store) {
        cmd.append("Pages/AnnouncementPanel.ui");

        // Bind Add Button
        EventData addEvent = new EventData();
        addEvent.append("Action", "ADD");
        events.addEventBinding(CustomUIEventBindingType.Activating, "#addButton", addEvent);
        
        // Bind Refresh Button
        EventData refreshEvent = new EventData();
        refreshEvent.append("Action", "REFRESH");
        events.addEventBinding(CustomUIEventBindingType.Activating, "#refreshButton", refreshEvent);
        
        // Bind Close Button
        EventData closeEvent = new EventData();
        closeEvent.append("Action", "CLOSE");
        events.addEventBinding(CustomUIEventBindingType.Activating, "#closeButton", closeEvent);

        // Populate List
        List<Announcement> announcements = service.getAllAnnouncements();
        
        int index = 0;
        for (Announcement announcement : announcements) {
            // 1. Ajouter le template à la liste
            cmd.append("#AnnouncementList", "Pages/AnnouncementItem.ui");
            
            // 2. Construire le sélecteur pour cibler cet élément spécifique
            String rowSelector = "#AnnouncementList[" + index + "]";
            
            // 3. Modifier les textes en utilisant le sélecteur relatif
            String message = announcement.message();
            if (message == null) message = "Erreur: Message null";
            cmd.set(rowSelector + " #messageText.Text", message);
            
            String world = announcement.targetWorld();
            if (world == null) world = "Global";
            cmd.set(rowSelector + " #worldText.Text", world);
            
            // 4. Binder les événements aux boutons spécifiques
            // Actions Group
            String actionsId = "actions_" + index;
            
            // Edit Button
            String editBtnId = "edit_" + index;
            // Note: On utilise les IDs du template, mais on les cible via le rowSelector
            // Le template a #editButton et #deleteButton
            
            // Suppression des tentatives de modification de couleur qui font crasher le client
            // cmd.set(rowSelector + " #editButton.Color", "#22c55e");
            // cmd.set(rowSelector + " #editButton.HoverColor", "#4ade80");
            
            EventData editEvent = new EventData();
            editEvent.append("TargetId", announcement.id().toString());
            editEvent.append("Action", "EDIT");
            events.addEventBinding(CustomUIEventBindingType.Activating, rowSelector + " #editButton", editEvent);
            
            // Delete Button
            // Suppression des tentatives de modification de couleur qui font crasher le client
            // cmd.set(rowSelector + " #deleteButton.Color", "#ef4444");
            // cmd.set(rowSelector + " #deleteButton.HoverColor", "#f87171");
            
            EventData deleteEvent = new EventData();
            deleteEvent.append("TargetId", announcement.id().toString());
            deleteEvent.append("Action", "DELETE");
            events.addEventBinding(CustomUIEventBindingType.Activating, rowSelector + " #deleteButton", deleteEvent);
            
            index++;
        }
    }

    @Override
    public void handleDataEvent(Ref<EntityStore> ref, Store<EntityStore> store, PanelEventData data) {
        Player player = store.getComponent(ref, Player.getComponentType());
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());

        // Sécurité : Vérification de la permission avant toute action
        if (!player.hasPermission(com.jjeanniard.plugins.Permissions.ADMIN_MANAGE)) {
            playerRef.sendMessage(com.hypixel.hytale.server.core.Message.raw("§cAction refusée : Permission manquante."));
            this.close();
            return;
        }

        if ("ADD".equals(data.action)) {
            player.getPageManager().openCustomPage(ref, store, new AnnouncementEditPage(playerRef, service, null));
        } else if ("EDIT".equals(data.action)) {
            UUID id = UUID.fromString(data.targetId);
            Announcement announcement = service.getAnnouncement(id);
            if (announcement != null) {
                player.getPageManager().openCustomPage(ref, store, new AnnouncementEditPage(playerRef, service, announcement));
            }
        } else if ("DELETE".equals(data.action)) {
            UUID id = UUID.fromString(data.targetId);
            service.deleteAnnouncement(id);
            // Log de l'action
            com.jjeanniard.plugins.Log.info("L'utilisateur " + playerRef.getUsername() + " a supprimé une annonce.");
            // Refresh the page
            player.getPageManager().openCustomPage(ref, store, new AnnouncementPanelPage(playerRef, service));
        } else if ("REFRESH".equals(data.action)) {
            service.reload();
            player.getPageManager().openCustomPage(ref, store, new AnnouncementPanelPage(playerRef, service));
        } else if ("CLOSE".equals(data.action)) {
            this.close();
        }
    }
}
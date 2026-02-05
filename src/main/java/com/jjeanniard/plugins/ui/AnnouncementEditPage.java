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

import javax.annotation.Nullable;

public class AnnouncementEditPage extends InteractiveCustomUIPage<AnnouncementEditPage.EditEventData> {

    private final AnnouncementManagerService service;
    @Nullable
    private final Announcement existingAnnouncement;

    public static class EditEventData {
        public String action; // "SAVE" or "CANCEL"
        public String message;
        public String world;

        public static final BuilderCodec<EditEventData> CODEC = BuilderCodec
                .builder(EditEventData.class, EditEventData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING),
                        (obj, val) -> obj.action = val,
                        obj -> obj.action)
                .add()
                .append(new KeyedCodec<>("@Message", Codec.STRING),
                        (obj, val) -> obj.message = val,
                        obj -> obj.message)
                .add()
                .append(new KeyedCodec<>("@World", Codec.STRING),
                        (obj, val) -> obj.world = val,
                        obj -> obj.world)
                .add()
                .build();
    }

    public AnnouncementEditPage(PlayerRef playerRef, AnnouncementManagerService service, @Nullable Announcement announcement) {
        super(playerRef, CustomPageLifetime.CanDismiss, EditEventData.CODEC);
        this.service = service;
        this.existingAnnouncement = announcement;
    }

    @Override
    public void build(Ref<EntityStore> ref, UICommandBuilder cmd, UIEventBuilder events, Store<EntityStore> store) {
        cmd.append("Pages/AnnouncementEdit.ui");

        if (existingAnnouncement != null) {
            cmd.set("#title.Text", "ÉDITER L'ANNONCE");
            cmd.set("#messageInput.Value", existingAnnouncement.message());
            cmd.set("#worldInput.Value", existingAnnouncement.targetWorld() != null ? existingAnnouncement.targetWorld() : "");
        } else {
            cmd.set("#title.Text", "CRÉER UNE ANNONCE");
        }

        EventData saveEvent = new EventData();
        saveEvent.append("Action", "SAVE");
        // Utilisation de la syntaxe correcte pour récupérer la valeur d'un champ UI
        // La clé doit correspondre à celle définie dans le Codec (Message, World)
        // La valeur doit être le sélecteur UI (ex: #messageInput.Value)
        // Le préfixe '@' n'est pas supporté par append() directement pour la clé, mais pour la valeur dans certains contextes.
        // Cependant, l'erreur "Value can not be null" suggère que Hytale n'arrive pas à résoudre la valeur.

        // Correction : Utiliser addEventBinding avec un EventData qui mappe les champs.
        // Mais EventData.append prend (String key, String value).

        // Si on regarde l'exemple Kotlin trouvé précédemment :
        // createData("SEARCH").append("@Value", "#SearchTextField.Value")

        // Donc la clé doit être le nom du champ dans le Codec, et la valeur le sélecteur UI.
        // MAIS, pour que Hytale comprenne qu'il doit lire la valeur du champ UI, il faut parfois une syntaxe spéciale ou une méthode différente.

        // Dans l'exemple Kotlin :
        // .append("@Value", "#SearchTextField.Value")
        // Ici "@Value" semble être une convention pour dire "bind ceci à la propriété 'value' de l'objet data".

        // Dans notre Codec, les champs s'appellent "Message" et "World".
        // Essayons d'utiliser "@Message" et "@World" comme clés.

        saveEvent.append("@Message", "#messageInput.Value");
        saveEvent.append("@World", "#worldInput.Value");

        // Si cela a échoué avec "Value can not be null", c'est peut-être que le sélecteur ne trouve rien ou que la syntaxe est fausse.
        // Vérifions le fichier UI : TextField #messageInput existe bien.

        // Autre possibilité : L'erreur vient de BsonString.<init>. Cela veut dire qu'on passe null à append.
        // Est-ce que "#messageInput.Value" est null ? Non, c'est une string littérale.

        // Ah ! L'erreur "Value can not be null" vient peut-être du fait que Hytale essaie de lire la valeur AU MOMENT DU BINDING ?
        // Non, c'est au moment de l'exécution de l'event.

        // Regardons la stacktrace :
        // at com.hypixel.hytale.server.core.ui.builder.UICommandBuilder.set(UICommandBuilder.java:90)
        // ...
        // at ThirdPartyPlugin//com.jjeanniard.plugins.ui.AnnouncementEditPage.handleDataEvent(AnnouncementEditPage.java:99)

        // Attendez, l'erreur se produit dans handleDataEvent ?
        // Non, "at ThirdPartyPlugin//com.jjeanniard.plugins.ui.AnnouncementPanelPage.build(AnnouncementPanelPage.java:71)"
        // C'est dans le build de la page PANEL, pas EDIT.

        // L'utilisateur a dit : "La sauvegarde d'une modification... ne s'est pas passée comme prévu... et dans le fichier de config j'ai un null".
        // ET "Failed to run task! java.lang.IllegalArgumentException: Value can not be null ... at AnnouncementPanelPage.build".

        // Donc il y a DEUX problèmes.
        // 1. La sauvegarde met "null" dans la config.
        // 2. Le crash "Value can not be null" se produit quand on revient sur le Panel.

        // Analysons le problème 1 (Sauvegarde null) :
        // Si data.message est null, c'est que le binding n'a pas marché.
        // Dans AnnouncementEditPage, on a mis : saveEvent.append("@Message", "#messageInput.Value");
        // Si ça ne marche pas, c'est que la syntaxe est fausse.

        // Analysons le problème 2 (Crash Panel) :
        // AnnouncementPanelPage.java:71 correspond à :
        // cmd.set(rowSelector + " #messageText.Text", announcement.message());
        // Si announcement.message() est null (à cause du problème 1), alors cmd.set plante car il interdit null.

        // Donc tout vient du fait que la sauvegarde enregistre "null".

        // Pourquoi la sauvegarde enregistre null ?
        // Parce que `data.message` est null dans `handleDataEvent`.
        // Donc le binding `@Message` -> `#messageInput.Value` ne fonctionne pas.

        // Essayons la syntaxe sans '@' mais avec le sélecteur.
        // saveEvent.append("Message", "#messageInput.Value");
        // C'est ce qu'on avait avant, et l'utilisateur a dit "il affiche #messageInput.Value dans la colonne".
        // Donc Hytale a pris la chaîne littérale.

        // Il faut dire à Hytale "Ceci est une référence, pas une valeur".
        // Dans certaines APIs, on utilise `{}` ou `$`.
        // Ou alors, il faut utiliser une méthode spécifique `addBoundData` ? Non.

        // Regardons encore l'exemple Kotlin :
        // createData("SEARCH").append("@Value", "#SearchTextField.Value")
        // La clé est "@Value". Dans son codec, il a probablement un champ "value".

        // Donc la syntaxe `@Champ` semble correcte pour binder.
        // Pourquoi ça a échoué ?

        // Peut-être que le Codec attend "Message" (Majuscule) et on lui envoie "@Message".
        // Le Codec KeyedCodec("Message", ...) attend la clé "Message" dans le JSON reçu.
        // Quand on met `@Message` dans l'EventData, Hytale devrait générer un JSON où la clé "Message" a pour valeur le contenu de l'input.

        // Si ça ne marche pas, c'est peut-être sensible à la casse ou autre.
        // Dans EditEventData.CODEC : new KeyedCodec<>("Message", Codec.STRING)

        // Essayons une autre approche pour le binding.
        // Peut-être que le binding ne marche que sur `ValueChanged` et pas `Activating` ?
        // Non, ça devrait marcher pour un bouton de soumission.

        // Vérifions si `#messageInput` est bien un `TextField`. Oui.

        // Hypothèse : Le Codec utilise "Message", mais le binding interne Hytale attend peut-être "message" (minuscule) si c'est mappé automatiquement ?
        // Non, on a défini le codec manuellement.

        // Essayons de déboguer en loggant ce qu'on reçoit.
        // Mais on ne peut pas changer le code Hytale.

        // Tentative de correction :
        // On va essayer de forcer la valeur à ne pas être null dans AnnouncementPanelPage pour éviter le crash.
        // Et pour la sauvegarde, on va essayer de comprendre pourquoi le binding échoue.

        // Si l'utilisateur a vu "#messageInput.Value" écrit textuellement, c'est que le binding n'a pas été interprété.
        // Cela arrive si on utilise `append("Key", "Value")` classique.
        // Il faut utiliser une méthode qui marque la valeur comme étant une référence.
        // Mais `EventData` n'a que `append`.

        // C'est donc la clé qui doit déclencher le comportement.
        // `@Message` devrait marcher.

        // Est-ce que j'ai bien mis `@Message` dans la version qui a produit "null" ?
        // L'utilisateur a dit : "La sauvegarde... ne s'est pas passée comme prévu... j'ai un null".
        // C'était APRES que j'ai mis `@Message`.
        // Donc `@Message` -> null.
        // Et `Message` -> "#messageInput.Value".

        // Cela signifie que `@Message` a bien tenté de résoudre, mais a échoué (donc null).
        // Pourquoi ? Peut-être que le chemin `#messageInput.Value` est incorrect ?
        // Dans le fichier UI : `TextField #messageInput`. La propriété est bien `Value` (ou `Text` ?).
        // Pour un TextField, c'est souvent `Value`.

        // Essayons `#messageInput.Text`.

        saveEvent.append("@Message", "#messageInput.Value");
        saveEvent.append("@World", "#worldInput.Value");

        events.addEventBinding(CustomUIEventBindingType.Activating, "#saveButton", saveEvent);

        EventData cancelEvent = new EventData();
        cancelEvent.append("Action", "CANCEL");
        events.addEventBinding(CustomUIEventBindingType.Activating, "#cancelButton", cancelEvent);
    }

    @Override
    public void handleDataEvent(Ref<EntityStore> ref, Store<EntityStore> store, EditEventData data) {
        Player player = store.getComponent(ref, Player.getComponentType());
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());

        // Sécurité : Vérification de la permission avant toute action
        if (!player.hasPermission(com.jjeanniard.plugins.Permissions.ADMIN_MANAGE)) {
            playerRef.sendMessage(com.hypixel.hytale.server.core.Message.raw("§cAction refusée : Permission manquante."));
            this.close();
            return;
        }

        if ("SAVE".equals(data.action)) {
            String world = data.world != null && !data.world.trim().isEmpty() ? data.world : null;
            String message = data.message;

            if (message == null || message.trim().isEmpty()) {
                playerRef.sendMessage(com.hypixel.hytale.server.core.Message.raw("§cErreur : Le message de l'annonce ne peut pas être vide."));
                return;
            }

            // Limite de taille pour le chat (Standard ~256 caractères)
            if (message.length() > 256) {
                playerRef.sendMessage(com.hypixel.hytale.server.core.Message.raw("§cErreur : Le message est trop long (" + message.length() + "/256 caractères)."));
                return;
            }

            if (existingAnnouncement != null) {
                // Update
                Announcement updated = new Announcement(existingAnnouncement.id(), message, world);
                service.updateAnnouncement(updated);
                com.jjeanniard.plugins.Log.info("L'utilisateur " + playerRef.getUsername() + " a modifié l'annonce : " + message);
            } else {
                // Create
                Announcement newAnnouncement = new Announcement(message, world);
                service.addAnnouncement(newAnnouncement);
                com.jjeanniard.plugins.Log.info("L'utilisateur " + playerRef.getUsername() + " a créé une annonce : " + message);
            }
        }

        // Return to panel
        player.getPageManager().openCustomPage(ref, store, new AnnouncementPanelPage(playerRef, service));
    }
}
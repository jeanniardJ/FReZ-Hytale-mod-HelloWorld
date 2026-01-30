# 🖥️ UI & Affichage

Gestion de l'interface utilisateur, des titres et du chat.

## Titres à l'écran (`EventTitleUtil`)
Permet d'afficher un grand texte au milieu de l'écran du joueur.

```java
import com.hypixel.hytale.server.core.util.EventTitleUtil;

EventTitleUtil.showEventTitleToPlayer(
    playerRef,                      // Le joueur cible
    Message.raw("TITRE PRINCIPAL"), // Ligne 1 (Gros)
    Message.raw("Sous-titre"),      // Ligne 2 (Petit)
    true,                           // isMajor (true = Gros titre, false = Action bar ?)
    "ui/icons/announcement.png",    // Icône (chemin dans les assets)
    5.0f,                           // Durée (secondes)
    1.5f,                           // Fade In (secondes)
    1.5f                            // Fade Out (secondes)
);
```

## Messages Chat (`Message`)
Construction de messages riches.

```java
import com.hypixel.hytale.server.core.Message;

// Message simple
Message msg = Message.raw("Texte simple");

// Message traduit (si supporté)
// Message trans = Message.translatable("welcome.message");
```

# Permissions API

Hytale dispose d'un système de permissions intégré pour gérer les droits des joueurs.

## Vérifier une permission

### Dans une commande (`CommandContext`)

```java
@Override
protected void execute(CommandContext context) {
    // Si l'expéditeur est un joueur
    if (context.isPlayer()) {
        Player player = context.senderAs(Player.class);
        
        if (player.hasPermission("mon.plugin.admin")) {
            // Action autorisée
        } else {
            context.sendMessage(Message.raw("Permission refusée."));
        }
    }
    
    // Si l'expéditeur est la console (ou autre)
    else if (context.getSender().hasPermission("mon.plugin.admin")) {
        // Action autorisée
    }
}
```

### Sur une entité Joueur (`Player` / `PlayerRef`)

L'entité `Player` implémente l'interface `PermissionHolder`.

```java
// Depuis un PlayerRef (via l'entité associée)
// Note : PlayerRef n'a pas hasPermission directement, il faut récupérer le composant Player
// ou utiliser PermissionsModule.get().hasPermission(uuid, node)

// Méthode recommandée si vous avez l'entité Player (ECS)
if (playerEntity.hasPermission("mon.plugin.node")) {
    // ...
}

// Méthode via le module global (si vous avez juste l'UUID)
import com.hypixel.hytale.server.core.modules.permissions.PermissionsModule; // (Package à vérifier selon version)

// PermissionsModule.get().hasPermission(playerUuid, "mon.plugin.node");
```

## Définir des permissions

Les permissions sont généralement définies dans des fichiers de configuration du serveur (`permissions.json` ou
équivalent) ou gérées par un plugin de permissions tiers.

Votre plugin doit simplement définir les nœuds qu'il utilise (ex: dans une classe `Permissions.java`) et les vérifier
aux endroits critiques.

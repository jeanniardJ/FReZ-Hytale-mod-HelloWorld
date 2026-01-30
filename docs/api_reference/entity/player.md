# 👤 Player API

L'objet `PlayerRef` représente un joueur connecté au serveur. C'est une référence à l'entité joueur dans l'univers.

## Récupérer un joueur
> Voir la [Documentation Universe](../general/universe.md) pour les différentes méthodes de recherche.
```java
import com.hypixel.hytale.server.core.universe.PlayerRef;

// Depuis un événement
PlayerRef player = event.getPlayerRef();
```

## Propriétés du Joueur
```java
import java.util.UUID;
import com.hypixel.hytale.math.vector.Transform;

UUID uuid = player.getUuid();
String username = player.getUsername();
String language = player.getLanguage();
Transform transform = player.getTransform();
UUID currentWorldUuid = player.getWorldUuid();
```

## Actions sur le joueur

### Communication
> Pour construire des messages complexes, voir la [Documentation Message](../ui/message.md).
```java
import com.hypixel.hytale.server.core.Message;

player.sendMessage(Message.raw("Bonjour !"));
```

### Téléportation & Changement de Serveur
```java
// Téléporter le joueur vers un autre serveur (BungeeCord/Velocity-like)
// player.referToServer("adresse.du.serveur", 25565, null);
```

### Composants du Joueur (ECS)
`PlayerRef` est une entité. Pour accéder à ses données spécifiques, il faut récupérer ses composants.
> Voir les [Principes de l'Architecture ECS](../../1_ARCHITECTURE.md).
```java
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.server.core.entity.entities.Player;

// Holder<EntityStore> playerHolder = player.getHolder(); 
// Player playerComponent = playerHolder.getComponent(Player.getComponentType());
```

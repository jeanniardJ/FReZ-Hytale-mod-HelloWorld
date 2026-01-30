# 🗺️ World API

La classe `World` représente un monde chargé sur le serveur. Chaque monde gère ses propres entités, chunks et joueurs.

> Voir aussi : [Universe API](../general/universe.md) pour la gestion globale des mondes.

## 1. Accès à un Monde
```java
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;

World myWorld = Universe.get().getWorld("NomDuMonde");
```

## 2. Propriétés du Monde
> Pour plus de détails, voir la [Documentation WorldConfig](world_config.md).
```java
import com.hypixel.hytale.server.core.universe.world.WorldConfig;

WorldConfig config = myWorld.getWorldConfig();
String name = myWorld.getName();
```

## 3. Gestion des Joueurs dans un Monde
> Pour plus de détails, voir la [Documentation Joueur](../entity/player.md).
```java
import java.util.List;
import com.hypixel.hytale.server.core.universe.PlayerRef;

List<PlayerRef> playersInThisWorld = myWorld.getPlayers();
```

## 4. Gestionnaire de Notifications (`WorldNotificationHandler`)
Chaque monde possède un `WorldNotificationHandler` pour envoyer des paquets optimisés aux joueurs (particules, dégâts sur les blocs, etc.).

```java
// Accès (hypothétique)
WorldNotificationHandler notifier = myWorld.getNotificationHandler();

// Envoyer une particule à tous les joueurs qui voient le chunk
notifier.sendBlockParticle(x, y, z, id, BlockParticleEvent.BREAK);
```

## 5. Événements Spécifiques au Monde
Chaque monde a son propre `EventBus`.
> Voir la [Documentation sur les Listeners](../event/listeners.md).
```java
import com.hypixel.hytale.event.EventBus;

EventBus worldEventBus = myWorld.getEventBus();
```

---

## 6. Le Pattern `WorldProvider`
Certaines classes de l'API Hytale (notamment les événements) peuvent implémenter l'interface `WorldProvider`.

```java
public interface WorldProvider {
    @Nonnull
    World getWorld();
}
```
Si un objet implémente cette interface, vous pouvez simplement appeler `.getWorld()` pour obtenir le monde concerné.

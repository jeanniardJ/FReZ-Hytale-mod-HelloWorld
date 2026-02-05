# Moteur de Stockage (Storage Engine)

Hytale utilise un système de stockage pour persister les données des joueurs et des mondes.

## PlayerStorage

Gère la sauvegarde des données des joueurs (inventaire, position, stats).

```java
import com.hypixel.hytale.server.core.universe.Universe;

// Sauvegarder un joueur manuellement
Universe.get().getPlayerStorage().save(playerUuid, playerHolder);
```

## ChunkStorage

Gère la sauvegarde des chunks du monde.

## Format

Les données sont généralement sérialisées en binaire ou en JSON, selon la configuration et le type de données. Le
système ECS utilise des `Codecs` pour transformer les composants en données stockables.

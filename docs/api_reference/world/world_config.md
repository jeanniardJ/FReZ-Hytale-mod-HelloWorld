# WorldConfig API

La classe `WorldConfig` contient tous les paramètres de configuration d'un monde spécifique.

## Accès

```java
WorldConfig config = world.getWorldConfig();
```

## Propriétés Principales

### 1. Mode de Jeu (`GameMode`)

Définit le mode de jeu par défaut pour les joueurs rejoignant ce monde.

```java
import com.hypixel.hytale.protocol.GameMode;

GameMode mode = config.getGameMode(); // SURVIVAL, CREATIVE, ADVENTURE
// config.setGameMode(GameMode.CREATIVE);
```

### 2. Règles du Jeu (`GameRules`)

Active ou désactive certaines mécaniques.

```java
// Exemple hypothétique (l'API exacte peut varier)
boolean pvp = config.isPvpEnabled();
boolean mobSpawning = config.isMobSpawningEnabled();
```

### 3. Génération (`ChunkGenerator`)

Définit comment le terrain est généré.

```java
// Récupérer le générateur
ChunkGenerator generator = config.getChunkGenerator();
```

### 4. Point d'Apparition (`SpawnPoint`)

```java
import com.hypixel.hytale.math.vector.Vector3d;

Vector3d spawn = config.getSpawnPoint();
```

## Modification

Certaines propriétés peuvent être modifiées à la volée, d'autres nécessitent un redémarrage du monde ou ne sont définies
qu'à la création.

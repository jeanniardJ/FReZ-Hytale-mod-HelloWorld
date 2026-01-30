# ⚙️ World Configuration (WorldConfig)

Chaque monde sur un serveur Hytale possède son propre fichier de configuration, géré par la classe `WorldConfig`.

## 1. Emplacement du Fichier
La configuration d'un monde se trouve dans le dossier de ce monde, sous le nom `config.json`.
```
<dossier_serveur>/
└── universe/
    └── worlds/
        └── <nom_du_monde>/
            └── config.json
```

## 2. Accès à la Configuration d'un Monde
Vous pouvez accéder à l'objet `WorldConfig` d'un monde chargé via l'instance `World`.

```java
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.WorldConfig;

// Récupérer le monde
World myWorld = Universe.get().getWorld("NomDuMonde");

if (myWorld != null) {
    // Récupérer sa configuration
    WorldConfig worldConfig = myWorld.getWorldConfig();
    
    // Utiliser la configuration
    long seed = worldConfig.getSeed();
    boolean isPvpEnabled = worldConfig.isPvpEnabled();
}
```

## 3. Paramètres Principaux
La classe `WorldConfig` contient de nombreux paramètres pour personnaliser le comportement d'un monde :

*   **Identification** : `uuid`, `displayName`, `seed`.
*   **Génération** : `spawnProvider`, `worldGenProvider`, `worldMapProvider`, `chunkStorageProvider`.
*   **Règles du jeu** : `isTicking`, `isBlockTicking`, `isPvpEnabled`, `isFallDamageEnabled`, `gameMode`.
*   **Temps et Météo** : `gameTime`, `isGameTimePaused`, `forcedWeather`, `daytimeDurationSecondsOverride`, `nighttimeDurationSecondsOverride`.
*   **Entités** : `isSpawningNPC`, `isAllNPCFrozen`.
*   **Sauvegarde** : `isSavingPlayers`, `canSaveChunks`, `saveNewChunks`, `canUnloadChunks`.
*   **Cycle de vie** : `deleteOnUniverseStart`, `deleteOnRemove`.
*   **Plugins** : `requiredPlugins`, `pluginConfig`.

## 4. Configuration des Chunks (`ChunkConfig`)
La classe interne `ChunkConfig` permet de configurer des régions spécifiques :
*   **`pregenerateRegion`** : Une `Box2D` qui définit une zone à pré-générer au démarrage du monde.
*   **`keepLoadedRegion`** : Une `Box2D` qui définit une zone de chunks qui ne sera jamais déchargée.

## 5. Configuration des Plugins par Monde (`pluginConfig`)
Le système `pluginConfig` permet à un plugin de stocker sa propre configuration spécifique à un monde directement dans le `config.json` du monde. C'est une fonctionnalité très puissante pour les plugins qui ont besoin de paramètres différents pour chaque monde.

## 6. Le `WorldConfigProvider`
L'interface `WorldConfigProvider` est le mécanisme interne utilisé par Hytale pour lire (`load`) et écrire (`save`) ces fichiers de configuration. Les développeurs de plugins n'ont généralement pas besoin d'interagir directement avec cette interface.

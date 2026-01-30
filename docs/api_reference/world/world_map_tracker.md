# 🗺️ WorldMapTracker API

La classe `WorldMapTracker` est un composant attaché à chaque joueur (`Player`) qui gère l'affichage de la carte du monde, les marqueurs, et la découverte de zones/biomes pour ce joueur spécifique.

## 1. Accès au Tracker
Le `WorldMapTracker` est un composant du joueur. Il est probablement accessible via l'objet `Player` (qui est un composant de `PlayerRef`).

```java
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker;

// Hypothèse: Obtenir le Player component depuis PlayerRef
// Player playerComponent = playerRef.getComponent(Player.getComponentType());
// WorldMapTracker tracker = playerComponent.getWorldMapTracker(); // Hypothétique
```

## 2. Découverte de Zones et Titres d'Événements

### `ZoneDiscoveryInfo`
C'est un `record` qui contient toutes les informations nécessaires pour afficher un titre d'événement lors de la découverte d'une zone.

```java
public record ZoneDiscoveryInfo(
    @Nonnull String zoneName,
    @Nonnull String regionName,
    boolean display,
    @Nullable String discoverySoundEventId,
    @Nullable String icon,
    boolean major,
    float duration,
    float fadeInDuration,
    float fadeOutDuration
) {
    // ...
}
```

### Affichage d'un Titre lors de la Découverte
La méthode `onZoneDiscovered` utilise `EventTitleUtil` avec les données de `ZoneDiscoveryInfo`.

```java
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import com.hypixel.hytale.server.core.Message;

// Exemple d'appel interne (similaire à ce que nous ferions pour un portail)
EventTitleUtil.showEventTitleToPlayer(
    playerRefComponent, // Le joueur
    Message.translation(String.format("server.map.region.%s", zoneDiscoveryInfo.regionName())), // Titre principal (traduit)
    Message.translation(String.format("server.map.zone.%s", zoneDiscoveryInfo.zoneName())),   // Sous-titre (traduit)
    zoneDiscoveryInfo.major(),      // Est-ce un titre majeur ?
    zoneDiscoveryInfo.icon(),       // Icône
    zoneDiscoveryInfo.duration(),   // Durée
    zoneDiscoveryInfo.fadeInDuration(), // Fade In
    zoneDiscoveryInfo.fadeOutDuration() // Fade Out
);
```

## 3. Gestion des Marqueurs de Carte (`MapMarker`)
Permet d'envoyer des points d'intérêt personnalisés sur la carte du joueur.

```java
import com.hypixel.hytale.protocol.packets.worldmap.MapMarker;

// Envoyer un marqueur (méthode interne, l'API publique peut varier)
// trySendMarker(chunkViewRadius, playerChunkX, playerChunkZ, marker);
```

## 4. Contrôle de Téléportation via Carte
```java
// Permettre/interdire la téléportation aux coordonnées via la carte
// setAllowTeleportToCoordinates(world, true);

// Permettre/interdire la téléportation aux marqueurs via la carte
// setAllowTeleportToMarkers(world, true);
```

## 5. Implication pour les Développeurs de Plugins
*   **Bannière de Portail** : Le pattern `onZoneDiscovered` est une excellente référence pour implémenter la bannière de portail. Nous pouvons directement appeler `EventTitleUtil` avec des paramètres similaires.
*   **Carte Personnalisée** : Potentiellement utile pour ajouter des marqueurs personnalisés sur la carte des joueurs.
*   **Découverte de Zones** : L'événement `DiscoverZoneEvent` pourrait être écouté pour réagir à la découverte de zones par les joueurs.

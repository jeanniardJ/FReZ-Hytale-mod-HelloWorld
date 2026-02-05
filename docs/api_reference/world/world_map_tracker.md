# WorldMapTracker API

Le `WorldMapTracker` gère les icônes et les marqueurs affichés sur la carte (minimap et grande carte) pour un joueur ou
un monde.

## Fonctionnalités

* **Marqueurs de Joueurs** : Affiche la position des autres joueurs.
* **Points d'Intérêt (POI)** : Affiche des lieux importants (villes, donjons).
* **Waypoints** : Points définis par le joueur.

## Utilisation (Hypothétique)

```java
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker;

// Accès via le monde ou le joueur
WorldMapTracker tracker = world.getMapTracker();

// Ajouter un marqueur
tracker.addMarker(position, "icon_name", "Description");
```

> **Note** : Cette API est souvent utilisée en interne par le jeu. Son utilisation par les plugins pour ajouter des
> marqueurs personnalisés dépend de l'exposition de ces méthodes.

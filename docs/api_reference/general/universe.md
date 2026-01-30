# 🌍 Universe & Server API

La classe `Universe` est votre "tour de contrôle" pour tout ce qui concerne l'état global du jeu. C'est le point d'entrée principal pour interagir avec les joueurs et les mondes.

> **Analogie :** Si `HytaleServer` est le bâtiment de l'aéroport (avec ses systèmes de sécurité, ses terminaux), `Universe` est la tour de contrôle qui voit tous les avions (joueurs) et toutes les pistes (mondes).

> Voir aussi : [HytaleServer API](../general/hytaleserver.md) pour les managers de plus bas niveau.

## Accès à l'instance
Pour utiliser la tour de contrôle, il suffit de demander l'instance unique qui existe.

```java
import com.hypixel.hytale.server.core.universe.Universe;

// Récupérer l'instance unique (Singleton)
Universe universe = Universe.get();
```

## Gestion des Joueurs
> Pour plus de détails sur l'objet `PlayerRef`, voir la [Documentation Joueur](../../entity/player.md).

### Nombre de joueurs
```java
// Savoir combien de joueurs sont en ligne sur tout le serveur
int onlineCount = Universe.get().getPlayerCount();
```

### Liste des joueurs
```java
import java.util.List;
import com.hypixel.hytale.server.core.universe.PlayerRef;

// Obtenir la liste de tous les joueurs connectés
List<PlayerRef> players = Universe.get().getPlayers();
```

### Récupérer un joueur spécifique
> Pour plus de détails sur les stratégies de recherche, voir [NameMatching](../../util/name_matching.md).
```java
import java.util.UUID;
import com.hypixel.hytale.server.core.NameMatching;

// Par UUID (la méthode la plus fiable)
PlayerRef playerByUuid = Universe.get().getPlayer(UUID.fromString("votre-uuid-ici"));

// Par nom d'utilisateur (très utile pour les commandes)
PlayerRef playerByName = Universe.get().getPlayerByUsername("ste", NameMatching.DEFAULT); // Trouvera "Steve" ou "steve"
```

### Broadcast (Message à tous)
> Pour construire des messages complexes (couleurs, styles), voir la [Documentation Message](../../ui/message.md).
```java
// Envoyer un message dans le chat de tous les joueurs
Universe.get().sendMessage(Message.raw("Ceci est une annonce globale !"));
```

## Gestion des Mondes
> Pour plus de détails sur l'objet `World`, voir la [Documentation Monde](../../world/world.md).

### Liste des mondes
```java
import java.util.Map;
import com.hypixel.hytale.server.core.universe.world.World;

// Obtenir la liste de tous les mondes actuellement chargés
Map<String, World> worlds = Universe.get().getWorlds();
```

### Récupérer un monde spécifique
```java
// Obtenir un monde par son nom
World worldByName = Universe.get().getWorld("NomDuMonde");
```

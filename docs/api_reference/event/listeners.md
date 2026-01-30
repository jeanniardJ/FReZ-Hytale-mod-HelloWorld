# 👂 Événements (Listeners)

Le système d'événements permet de réagir aux actions du jeu.

## Créer un Listener
Une classe simple qui contient des méthodes publiques.

```java
public class MyListener {
    
    // La méthode peut avoir n'importe quel nom
    public void onPlayerJoin(PlayerConnectEvent event) {
        // Logique ici
    }
}
```

## Enregistrer un Listener
L'enregistrement se fait dans la méthode `start()` de votre classe principale (`JavaPlugin`).

```java
@Override
public void start() {
    MyListener listener = new MyListener();
    
    // Enregistrement via référence de méthode
    getEventRegistry().register(PlayerConnectEvent.class, listener::onPlayerJoin);
}
```

## Liste des Événements Connus

### Joueurs
*   `PlayerConnectEvent` : Quand un joueur se connecte.
*   `PlayerDisconnectEvent` : Quand un joueur se déconnecte.
*   `PlayerTeleportEvent` : Quand un joueur est téléporté.
    *   **Détails** : Cet événement est déclenché lors de toute téléportation d'un joueur, y compris via des portails, des commandes ou d'autres mécanismes. Il est crucial pour détecter les changements de monde.
    *   **Accès aux données** : L'événement devrait contenir des informations sur la source et la destination de la téléportation (joueur, monde de départ, monde d'arrivée, position de départ, position d'arrivée).
        ```java
        import com.hypixel.hytale.server.core.event.events.player.PlayerTeleportEvent;
        import com.hypixel.hytale.server.core.universe.PlayerRef;
        import com.hypixel.hytale.server.core.universe.world.World; // Hypothétique
        import com.hypixel.hytale.math.vector.Transform; // Hypothétique

        public void onPlayerTeleport(PlayerTeleportEvent event) {
            PlayerRef player = event.getPlayerRef();
            // World fromWorld = event.getFromWorld(); // Hypothétique
            // World toWorld = event.getToWorld();     // Hypothétique
            // Transform fromTransform = event.getFromTransform(); // Hypothétique
            // Transform toTransform = event.getToTransform();     // Hypothétique

            // Exemple de détection de changement de monde
            // if (!fromWorld.equals(toWorld)) {
            //     // Le joueur a changé de monde
            // }
        }
        ```

### Mondes
*   `AddWorldEvent` : Quand un nouveau monde est ajouté.
*   `RemoveWorldEvent` : Quand un monde est supprimé.
*   `AllWorldsLoadedEvent` : Quand tous les mondes ont fini de charger au démarrage.

*(Liste à compléter)*

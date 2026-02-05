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

### 1. Enregistrement Standard (`register`)
C'est la méthode la plus courante. Elle enregistre un écouteur pour un événement spécifique.

```java
@Override
public void start() {
    MyListener listener = new MyListener();
    
    // Enregistrement via référence de méthode
    getEventRegistry().register(PlayerConnectEvent.class, listener::onPlayerJoin);
}
```

### 2. Enregistrement Global (`registerGlobal`)
Cette méthode permet d'écouter un événement **partout**, indépendamment du contexte (monde, joueur, etc.). C'est utile pour les plugins qui doivent surveiller l'ensemble du serveur.

```java
// Écoute l'événement sur tous les mondes/contextes
getEventRegistry().registerGlobal(MyGlobalEvent.class, listener::onGlobalAction);
```

### 3. Enregistrement Asynchrone (`registerAsync`)
Pour les tâches lourdes qui ne doivent pas bloquer le serveur (ex: requêtes base de données).

```java
getEventRegistry().registerAsync(MyHeavyEvent.class, future -> {
    return future.thenApply(event -> {
        // Code asynchrone ici
        return event;
    });
});
```

## Liste des Événements Connus

### Joueurs
*   `PlayerConnectEvent` : Quand un joueur se connecte au serveur.
*   `PlayerDisconnectEvent` : Quand un joueur se déconnecte.
*   `AddPlayerToWorldEvent` : Quand un joueur est ajouté à un monde (connexion ou changement de monde).
    *   **Utilité** : Idéal pour détecter les changements de monde.
    *   **Données** : Contient le joueur (`Holder`) et le monde cible (`World`).

### Mondes
*   `AddWorldEvent` : Quand un nouveau monde est ajouté.
*   `RemoveWorldEvent` : Quand un monde est supprimé.
*   `AllWorldsLoadedEvent` : Quand tous les mondes ont fini de charger au démarrage.
*   `DiscoverZoneEvent` : Quand un joueur découvre une nouvelle zone.

*(Liste à compléter)*

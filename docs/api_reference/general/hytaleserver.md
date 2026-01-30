# 💻 HytaleServer API

La classe `HytaleServer` est le cœur du serveur. On peut y accéder de n'importe où grâce à son instance statique (Singleton).

## Accès à l'instance
```java
import com.hypixel.hytale.server.core.HytaleServer;

// Récupérer l'instance principale du serveur
HytaleServer server = HytaleServer.get();
```

## Managers principaux
Depuis l'instance du serveur, on peut accéder aux gestionnaires principaux de l'API.

### Plugin Manager
```java
// Gère le cycle de vie et l'état des plugins
PluginManager pluginManager = server.getPluginManager();
```

### Command Manager
```java
// Gère l'enregistrement et l'exécution des commandes
CommandManager commandManager = server.getCommandManager();
```

### Event Bus
```java
// Le bus central qui distribue les événements
EventBus eventBus = server.getEventBus();
```

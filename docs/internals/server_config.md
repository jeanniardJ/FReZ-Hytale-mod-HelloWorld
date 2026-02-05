# Configuration du Serveur (Server Config)

Détails sur la classe `ServerConfig` et les fichiers de configuration.

## Structure

La configuration est chargée au démarrage depuis `server.properties` et peut être surchargée par les arguments de ligne
de commande.

## API

```java
import com.hypixel.hytale.server.core.config.ServerConfig;

ServerConfig config = HytaleServer.get().getConfig();

// Propriétés
int maxViewRadius = config.getMaxViewRadius();
boolean onlineMode = config.isOnlineMode();
String motd = config.getMotd();
```

## Rechargement

Certaines configurations peuvent être rechargées à chaud, mais la plupart nécessitent un redémarrage.

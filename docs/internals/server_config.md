# ⚙️ Configuration du Serveur (HytaleServerConfig)

Ce document décrit la structure de la configuration principale du serveur Hytale, basée sur l'analyse de `HytaleServerConfig.java`.

**Note :** Il s'agit de la configuration du serveur lui-même, pas de la configuration de votre plugin.

## 1. Vue d'ensemble
La classe `HytaleServerConfig` gère tous les paramètres fondamentaux du serveur, tels que le nom, le MOTD, les limites de joueurs, et la gestion des plugins. Le fichier de configuration par défaut est `config.json`.

## 2. Accès à la Configuration du Serveur
Un plugin peut accéder à l'instance de la configuration du serveur via `HytaleServer.get().getConfig()`.

```java
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.HytaleServerConfig;

HytaleServerConfig serverConfig = HytaleServer.get().getConfig();

String serverName = serverConfig.getServerName();
int maxPlayers = serverConfig.getMaxPlayers();
```

## 3. Paramètres Principaux

*   **`serverName`** : Nom affiché du serveur.
*   **`motd`** : Message du jour.
*   **`password`** : Mot de passe du serveur.
*   **`maxPlayers`** : Nombre maximum de joueurs.
*   **`maxViewRadius`** : Rayon de vue maximum des joueurs.
*   **`logLevels`** : Configuration des niveaux de log pour différents modules du serveur.
*   **`playerStorageProvider`** : Définit le type de stockage pour les données des joueurs.

## 4. Gestion des Plugins/Mods (`ModConfig`)
Le serveur a un système intégré pour gérer la configuration de ses plugins.

### `ModConfig`
Cette classe interne représente la configuration d'un plugin spécifique.
```java
public static class ModConfig {
    @Nullable
    private Boolean enabled; // Si le plugin est activé ou désactivé
    @Nullable
    private SemverRange requiredVersion; // Version requise du plugin
}
```

### Accès et Modification
```java
import com.hypixel.hytale.common.plugin.PluginIdentifier;

// Récupérer la configuration d'un mod/plugin spécifique
Map<PluginIdentifier, HytaleServerConfig.ModConfig> modConfigs = serverConfig.getModConfig();

// Activer/désactiver un plugin (exemple, l'API exacte peut varier)
// HytaleServerConfig.ModConfig.setBoot(serverConfig, new PluginIdentifier("mon-plugin"), true);
```

## 5. Sous-Configurations
Le fichier `HytaleServerConfig` contient également des classes internes pour des configurations plus spécifiques :
*   **`Defaults`** : Paramètres par défaut (monde par défaut, gamemode par défaut).
*   **`ConnectionTimeouts`** : Délais d'attente pour les connexions.
*   **`RateLimitConfig`** : Configuration des limites de taux pour les paquets réseau.

## 6. Chargement et Sauvegarde
Le serveur gère automatiquement le chargement (`load()`) et la sauvegarde (`save()`) de sa configuration dans `config.json`. Les plugins ne devraient pas manipuler directement ces méthodes pour la configuration du serveur.

# 🔌 Système d'Arrêt (Shutdown)

Ce document explique comment le serveur Hytale gère son processus d'arrêt, basé sur l'analyse de `ShutdownReason.java` et `HytaleServer.java`.

## 1. Le Processus d'Arrêt
L'arrêt du serveur est initié via la méthode `HytaleServer.get().shutdownServer(ShutdownReason reason)`.
Ce processus déclenche l'événement `ShutdownEvent`, qui est utilisé par les différents modules du serveur (y compris les plugins) pour se nettoyer proprement.

## 2. Les Raisons d'Arrêt (`ShutdownReason`)
Chaque arrêt est associé à une raison spécifique, qui inclut un code de sortie standard pour les scripts externes.

| Raison | Code de Sortie | Description |
| :--- | :--- | :--- |
| `SHUTDOWN` | 0 | Arrêt normal et propre (ex: commande `/stop`). |
| `CRASH` | 1 | Le serveur a rencontré une erreur fatale inattendue. |
| `SIGINT` | 130 | L'utilisateur a appuyé sur `Ctrl+C` dans la console. |
| `AUTH_FAILED` | 2 | Une erreur d'authentification a empêché le démarrage. |
| `WORLD_GEN` | 3 | Une erreur est survenue lors de la génération d'un monde. |
| `CLIENT_GONE` | 4 | Le client s'est déconnecté (utilisé en mode solo). |
| `MISSING_REQUIRED_PLUGIN` | 5 | Un plugin listé comme dépendance est introuvable. |
| `VALIDATE_ERROR` | 6 | Une erreur de validation des assets ou d'autres fichiers a eu lieu. |

### Personnaliser le Message d'Arrêt
Il est possible d'ajouter un message personnalisé à une raison d'arrêt.
```java
// Dans HytaleServer.java, on voit cette utilisation :
this.shutdownServer(ShutdownReason.CRASH.withMessage("Failed to start server!"));
```

## 3. Implication pour les Développeurs de Plugins
*   **Écouter `ShutdownEvent`** : C'est la méthode la plus propre pour sauvegarder les données de votre plugin avant que le serveur ne s'éteigne complètement.
*   **Ne pas appeler `System.exit()`** : N'arrêtez jamais le serveur vous-même. Utilisez `HytaleServer.get().shutdownServer(...)` si vous devez absolument provoquer un arrêt.

# 🔄 Server Lifecycle

Ce document décrit l'ordre de démarrage (`boot`) et d'arrêt (`shutdown`) du serveur Hytale, basé sur l'analyse de `HytaleServer.java`.

## 1. Phase de Démarrage (`boot`)

L'ordre d'exécution est strict. Voici les étapes clés qui vous concernent :

1.  **`HytaleServer()` (Constructeur)**
    *   Le `PluginManager` est créé.
    *   Les plugins sont découverts, et leur **constructeur** est appelé.
    *   ✅ C'est ici que `withConfig()` doit être appelé.

2.  **`boot()` (Méthode principale)**
    *   **`pluginManager.setup()`**
        *   Le `PluginManager` parcourt tous les plugins et appelle leur méthode `setup()`.
        *   ✅ C'est un bon endroit pour initialiser des variables ou des singletons.

    *   **`LoadAssetEvent`**
        *   Le serveur charge et valide les assets. Un événement `LoadAssetEvent` est lancé.

    *   **`pluginManager.start()`**
        *   Le `PluginManager` appelle la méthode `start()` de chaque plugin.
        *   ✅ C'est ici que vous devez lire la config (`config.get()`), initialiser les services et enregistrer les listeners.

    *   **`Universe.get().getUniverseReady().join()`**
        *   Le serveur attend que les mondes soient chargés et prêts.

    *   **`eventBus.dispatch(BootEvent.class)`**
        *   Un événement `BootEvent` est lancé pour signaler que le serveur est entièrement démarré et prêt à accepter des joueurs.

## 2. Phase d'Arrêt (`shutdown0`)

1.  **`eventBus.dispatch(ShutdownEvent.class)`**
    *   Un événement `ShutdownEvent` est lancé pour signaler le début de l'arrêt.
    *   ✅ Idéal pour commencer à sauvegarder les données.

2.  **`pluginManager.shutdown()`**
    *   Le `PluginManager` appelle la méthode `shutdown()` de chaque plugin.
    *   ✅ C'est ici que vous devez arrêter vos services, timers et threads.

3.  **`commandManager.shutdown()`**
    *   Le gestionnaire de commandes est arrêté.

4.  **Sauvegarde finale de la config**
    *   Le fichier `server.json` est sauvegardé.

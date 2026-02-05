# 📜 Changelog de la Documentation API Hytale

Ce fichier retrace l'historique des découvertes et des mises à jour de la documentation de l'API Hytale pour ce projet.

---

### **Session UI & Gestion des Annonces (Date actuelle)**

#### ✨ Nouvelles Documentations
*   **Interface Utilisateur (UI)**
    *   `api_reference/ui/ui_system.md` : Documentation complète du système d'UI déclaratif de Hytale.
        *   Structure des fichiers `.ui` et `manifest.json`.
        *   Utilisation de `InteractiveCustomUIPage` pour les formulaires.
        *   Gestion des événements UI avec `EventData` et `CODEC`.
        *   Styles et variables `Common.ui`.

#### 🔄 Améliorations
*   **Commandes** : Mise à jour implicite de la gestion des commandes pour inclure le contexte joueur/console (`CommandContext.isPlayer()`, `senderAs(Player.class)`).
*   **Threading** : Découverte de l'importance de `world.execute(() -> {})` pour accéder aux composants ECS (`Store`) depuis une commande asynchrone.
*   **Logging** : Adaptation de la classe `Log` pour être compatible avec les tests unitaires (fallback sur `System.out`).

---

### **Session Initiale (Date de création du projet)**

#### ✨ Nouvelles Documentations
*   **Architecture & Principes Fondamentaux**
    *   Création de `1_ARCHITECTURE.md` : Explication du pattern ECS et du cycle de vie des plugins.
    *   Création de `3_CONFIGURATION.md` : Documentation du système de `Codec` pour les fichiers de configuration JSON.

*   **Premières Découvertes API**
    *   `api_reference/general/universe.md` : Découverte de `Universe.get()` pour l'accès global au serveur, aux joueurs et aux mondes.
    *   `api_reference/entity/player.md` : Documentation de base de `PlayerRef` (nom, UUID, envoi de message).
    *   `api_reference/ui/titles.md` : Découverte de `EventTitleUtil` pour afficher des titres à l'écran.
    *   `api_reference/event/listeners.md` : Documentation du système d'enregistrement des événements.

#### 🔄 Améliorations
*   Mise en place d'une structure de dossiers pour la référence API (`general`, `entity`, `ui`, `event`).
*   Création du fichier `RULES.md` et `AGENTS.md` pour définir les standards de développement et les directives pour l'IA.

---

### **Session d'Analyse des Fichiers Décompilés**

#### ✨ Nouvelles Documentations
*   **API Générales**
    *   `api_reference/general/hytaleserver.md` : Ajout de la documentation pour `HytaleServer.get()` et l'accès aux managers (`PluginManager`, `CommandManager`, `EventBus`).
    *   `api_reference/general/logging.md` : Documentation détaillée du `HytaleLogger` et de son utilisation (`at(Level.INFO)`, `withCause()`).

*   **API du Monde**
    *   `api_reference/world/world.md` : Documentation de la classe `World` (gestion des joueurs par monde, cycle de vie, EventBus par monde).
    *   `api_reference/world/world_config.md` : Documentation de la configuration spécifique à chaque monde (`config.json`).
    *   `api_reference/world/world_map_tracker.md` : Documentation du `WorldMapTracker` pour la gestion de la carte et la découverte de zones.

*   **API Utilitaires**
    *   `api_reference/ui/message.md` : Documentation du "builder" `Message` pour créer des textes formatés (couleurs, styles, liens).
    *   `api_reference/util/name_matching.md` : Documentation de l'enum `NameMatching` pour la recherche de joueurs.
    *   `api_reference/util/validate_util.md` : Documentation de `ValidateUtil` pour la validation des données numériques.
    *   `api_reference/util/wildcard_match.md` : Documentation de `WildcardMatch` pour la correspondance avec des jokers.

*   **Mécanismes Internes (Avancé)**
    *   `internals/core_modules.md` : Liste et description des plugins principaux du serveur.
    *   `internals/launch_options.md` : Documentation des options de ligne de commande du serveur.
    *   `internals/server_config.md` : Documentation de la configuration centrale du serveur (`HytaleServerConfig`).
    *   `internals/shutdown_system.md` : Documentation des raisons et du processus d'arrêt du serveur.
    *   `internals/sneaky_throw.md` : Explication du pattern "Sneaky Throw".
    *   `internals/storage_engine.md` : Documentation du moteur de stockage de bas niveau.

#### 🔄 Améliorations
*   Création d'un `README.md` central pour la documentation, servant de portail.
*   Ajout de liens hypertextes entre les différents fichiers de la documentation pour faciliter la navigation.
*   Refactorisation des documents principaux pour être plus accessibles à un développeur junior.

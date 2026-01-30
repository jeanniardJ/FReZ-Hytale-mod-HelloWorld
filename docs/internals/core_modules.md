# 🧩 Modules Principaux du Serveur

Ce document liste les modules principaux (core plugins) qui composent le serveur Hytale, basé sur l'analyse de `Constants.java`.

## 1. Vue d'ensemble
Le serveur Hytale est lui-même composé d'un ensemble de plugins internes, appelés "modules". Chaque module gère une partie spécifique du jeu.

## 2. Liste des Modules Principaux

### Gestion du Serveur & Joueurs
*   **`ConsoleModule`** : Gère la console du serveur.
*   **`PermissionsModule`** : Gère les permissions des joueurs.
*   **`AccessControlModule`** : Gère le contrôle d'accès (probablement les listes blanches/noires).
*   **`ServerPlayerListModule`** : Gère la liste des joueurs affichée aux clients.
*   **`SingleplayerModule`** : Gère la logique spécifique au mode solo.
*   **`ServerManager`** : Gère les connexions réseau.

### Monde & Univers
*   **`Universe`** : Le module central pour la gestion des mondes et des joueurs.
*   **`TimeModule`** : Gère le temps dans le jeu (jour/nuit).
*   **`MigrationModule`** : Gère la migration des anciens formats de données.

### Contenu du Jeu
*   **`AssetModule`** / **`CommonAssetModule`** : Gèrent le chargement des assets.
*   **`ItemModule`** : Gère les objets.
*   **`BlockModule`** / **`BlockTypeModule`** / **`BlockStateModule`** : Gèrent les blocs et leurs états.
*   **`EntityModule`** : Gère les entités.
*   **`CosmeticsModule`** : Gère les cosmétiques.
*   **`I18nModule`** : Gère la traduction (internationalisation).

### Mécaniques de Jeu
*   **`CollisionModule`** : Gère la physique des collisions.
*   **`DamageModule`** : Gère les dégâts.
*   **`InteractionModule`** : Gère les interactions des joueurs avec le monde.
*   **`StaminaModule`** : Gère l'endurance des joueurs.
*   **`ProjectileModule`** : Gère les projectiles.
*   **`EntityStatsModule`** / **`EntityUIModule`** : Gèrent les statistiques et l'interface utilisateur des entités.

### Utilitaires & Débogage
*   **`DebugPlugin`** : Contient des outils de débogage.
*   **`FlyCameraModule`** : Gère la caméra libre.

## 3. Implication pour les Développeurs de Plugins
*   **Dépendances** : Il est possible que votre plugin doive déclarer une dépendance à l'un de ces modules pour pouvoir interagir avec lui.
*   **Accès à l'API** : L'API Hytale pourrait fournir des méthodes pour récupérer des instances de ces modules et utiliser leurs fonctionnalités (ex: `HytaleServer.get().getModule(PermissionsModule.class)`).

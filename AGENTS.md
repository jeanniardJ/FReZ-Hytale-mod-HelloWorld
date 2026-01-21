# Règles de Développement - Projet HelloWorld

Ce fichier définit les standards de code et l'architecture à respecter pour ce projet de plugin Hytale.

## 1. Architecture ECS (Entity Component System)
Le projet suit l'architecture ECS native de Hytale. Il est impératif de respecter la séparation stricte entre données et logique.

### 🟢 Composants (Components)
*   **Rôle** : Conteneurs de données uniquement.
*   **Règle** : Un composant ne doit contenir **aucune logique métier**.
*   **Contenu** : Uniquement des champs (fields), des getters/setters, et éventuellement des méthodes de sérialisation (Codec).
*   *Exemple : `PositionComponent` contient `x, y, z`, mais ne contient pas de méthode `move()`.*

### 🔵 Systèmes (Systems)
*   **Rôle** : Moteurs logiques.
*   **Règle** : Toute la logique du jeu doit résider ici.
*   **Fonctionnement** : Un système itère sur les entités possédant un jeu spécifique de composants pour effectuer des actions.
*   *Exemple : `MovementSystem` lit `PositionComponent` et `VelocityComponent` pour déplacer l'entité.*

### 🟣 Entités (Entities)
*   **Rôle** : Identifiants uniques.
*   **Règle** : Ne pas utiliser l'héritage complexe (ex: `class SuperZombie extends Zombie`).
*   **Pratique** : Pour créer un nouveau type d'objet, assemblez des composants existants ou créez-en de nouveaux. **Composition > Héritage**.

---

## 2. Bonnes Pratiques Générales
*   **Immutabilité** : Préférez les variables `final` quand c'est possible.
*   **Configuration** : Ne jamais coder de valeurs en dur (hardcode). Utilisez `MyConfig` et les fichiers JSON.
*   **Logging** : Utilisez toujours `HytaleLogger` au lieu de `System.out.println`.

---

## 3. Instructions pour l'Assistant IA
*   **Respect de l'ECS** : L'assistant doit systématiquement proposer des solutions basées sur l'architecture ECS (séparation Données/Logique) et éviter l'héritage abusif.
*   **Code Concis** : Éviter le code verbeux inutile.
*   **Contexte Hytale** : Utiliser les APIs Hytale (Logger, Config, Events) par défaut.

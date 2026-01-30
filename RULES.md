#================================================#
#      RÈGLES DE DÉVELOPPEMENT - PROJET HELLOWORLD      #
#================================================#

Ce document définit les standards de code et l'architecture à respecter pour ce projet.

## 1. Architecture ECS (Entity Component System)
Le projet suit l'architecture ECS native de Hytale. Il est impératif de respecter la séparation stricte entre **Données (Components)** et **Logique (Systems)**.

*   #### 🟢 Composants (Components)
    *   **Rôle** : Conteneurs de données **uniquement**.
    *   **Règle** : Un composant ne doit contenir **aucune logique métier**.
    *   **Contenu** : Uniquement des champs (fields), des getters/setters, et éventuellement des méthodes de sérialisation (Codec).
    *   *Exemple : `PositionComponent` contient `x, y, z`, mais ne contient pas de méthode `move()`.*

*   #### 🔵 Systèmes (Systems)
    *   **Rôle** : Moteurs logiques.
    *   **Règle** : Toute la logique du jeu doit résider ici.
    *   **Fonctionnement** : Un système itère sur les entités possédant un jeu spécifique de composants pour effectuer des actions.
    *   *Exemple : `MovementSystem` lit `PositionComponent` et `VelocityComponent` pour déplacer l'entité.*

*   #### 🟣 Entités (Entities)
    *   **Rôle** : Identifiants uniques.
    *   **Règle** : Privilégier la **Composition** à l'Héritage. Ne pas créer d'arbres d'héritage complexes.

## 2. Bonnes Pratiques Générales
*   **Immutabilité** : Préférez les variables `final` quand c'est possible.
*   **Configuration** : Ne jamais coder de valeurs en dur (hardcode). Utilisez `MyConfig` et les fichiers JSON.
*   **Logging** : Utilisez toujours `HytaleLogger` au lieu de `System.out.println`.

## 3. Standards de Code et Documentation
*   **Commentaires** : Expliquez le **"pourquoi"** d'un code complexe, pas le "comment".
*   **Javadoc** : Toutes les classes et méthodes publiques doivent avoir un Javadoc (`/** ... */`).
*   **Lisibilité** : Le code et la documentation doivent être clairs et accessibles pour un développeur junior.

---

## 4. Directives pour l'Assistant IA
@include(./AGENTS.md)

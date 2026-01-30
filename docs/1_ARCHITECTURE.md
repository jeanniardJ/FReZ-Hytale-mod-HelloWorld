# 🏗️ Architecture & Règles de Développement

Ce document est votre guide pour comprendre comment construire des plugins Hytale de manière propre et efficace.

---

## 1. Le Pattern ECS (Entity Component System)

Hytale est un jeu moderne qui utilise une architecture spéciale appelée **ECS** (Entity Component System). Imaginez que vous construisez avec des briques LEGO :

*   **Entité (Entity)** : C'est juste un numéro d'identification, comme le numéro de série d'une brique. Une entité seule ne fait rien.
*   **Composant (Component)** : C'est une petite boîte de données. Par exemple, une boîte "Position" avec X, Y, Z. Une boîte "Vie" avec des points de vie. Un composant ne contient **aucune logique**, juste des informations.
*   **Système (System)** : C'est la logique, le "cerveau" qui agit sur les composants. Par exemple, un système "Mouvement" va chercher toutes les entités qui ont une boîte "Position" et une boîte "Vitesse", puis il va mettre à jour la "Position" de ces entités en fonction de leur "Vitesse".

### Pourquoi ECS ?
*   **Performance** : Le jeu peut gérer des milliers d'objets sans ralentir.
*   **Flexibilité** : Vous pouvez créer n'importe quel objet en combinant des composants. Un "chien robot" peut avoir des composants de "robot" et de "chien" sans héritage compliqué.

### 🟢 Composants (Components) - Les Boîtes de Données
*   **Rôle** : Conteneurs de données **uniquement**.
*   **Règle** : Un composant ne doit contenir **aucune logique métier**.
*   **Contenu** : Uniquement des champs (variables), des getters/setters.
*   *Exemple : `PositionComponent` contient `x, y, z`, mais ne contient pas de méthode `move()`.*

### 🔵 Systèmes (Systems) - Les Cerveaux Logiques
*   **Rôle** : Moteurs logiques.
*   **Règle** : Toute la logique du jeu doit résider ici.
*   **Fonctionnement** : Un système parcourt les entités qui ont les composants dont il a besoin et effectue des actions.
*   *Exemple : `MovementSystem` lit `PositionComponent` et `VelocityComponent` pour déplacer l'entité.*

### 🟣 Entités (Entities) - Les Numéros d'Identification
*   **Rôle** : Identifiants uniques.
*   **Règle** : Privilégiez la **Composition** (assembler des composants) à l'Héritage (faire hériter des classes).

---

## 2. Cycle de Vie du Plugin (`JavaPlugin`)

Votre plugin est comme un programme qui a différentes étapes de vie.

### A. Constructeur `public MyPlugin(JavaPluginInit init)`
*   **Quand ?** Hytale découvre votre plugin (très tôt, avant même que le serveur ne démarre complètement).
*   **Quoi faire ?**
    *   Enregistrer vos fichiers de configuration (`withConfig`).
    *   Sauvegarder l'instance de votre plugin (`instance = this`) si vous utilisez le pattern Singleton.
*   **Ce qu'il ne faut PAS faire :** Ne lancez pas de logique de jeu ici, ne lisez pas la configuration (`config.get()`). Le serveur n'est pas encore prêt !

### B. Démarrage `public void start()`
*   **Quand ?** Le serveur a fini de charger et est prêt à fonctionner.
*   **Quoi faire ?**
    *   **Lire la configuration** (`config.get()`) : C'est le moment !
    *   **Initialiser vos Services et Managers** : Créez les objets qui gèrent la logique de votre plugin.
    *   **Enregistrer vos Listeners (Événements)** : Dites à Hytale : "Je veux être prévenu quand un joueur se connecte, quand il parle, etc."
    *   **Lancer vos Timers/Tasks** : Démarrez vos boucles d'annonces automatiques, par exemple.

### C. Arrêt `public void shutdown()`
*   **Quand ?** Le serveur s'éteint.
*   **Quoi faire ?**
    *   **Nettoyer** : Arrêtez tous vos threads, timers, sauvegardez les données importantes. C'est crucial pour éviter les fuites de mémoire ou que le serveur ne s'arrête pas correctement.

---

## 3. Bonnes Pratiques Générales
*   **Immutabilité** : Rendez vos variables `final` dès que possible. Cela rend le code plus sûr et plus facile à comprendre.
*   **Configuration** : Ne mettez jamais de valeurs "en dur" dans votre code. Utilisez toujours `MyConfig` et les fichiers JSON pour que les utilisateurs puissent personnaliser votre plugin.
*   **Logging** : Utilisez toujours `HytaleLogger` pour écrire dans la console du serveur. C'est plus propre et plus puissant que `System.out.println`.

---

## 4. Standards de Code et Documentation
*   **Commentaires** : Expliquez le **"pourquoi"** d'un code complexe ou d'une décision de conception, pas le "comment". Le code doit être assez clair pour expliquer le "comment".
*   **Javadoc** : Toutes les classes et méthodes publiques doivent avoir un Javadoc (`/** ... */`) expliquant leur rôle, leurs paramètres et ce qu'elles retournent.
*   **Lisibilité** : Le code et la documentation doivent être clairs, concis et accessibles pour un développeur junior.

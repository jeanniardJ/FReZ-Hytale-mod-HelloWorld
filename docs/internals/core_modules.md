# Modules Principaux (Core Modules)

Le serveur Hytale est construit de manière modulaire. Voici les principaux modules internes.

## 1. EntityModule

Gère le cycle de vie des entités, les composants ECS de base et le tracking.

## 2. InteractionModule

Gère les interactions entre entités et blocs (clic droit, gauche, utilisation d'objets).

## 3. CollisionModule

Gère la physique, les collisions et les boîtes englobantes (Bounding Boxes).

## 4. PermissionsModule

Gère les groupes, les utilisateurs et les permissions.

## 5. CommandModule

Gère l'enregistrement et l'exécution des commandes.

## Accès aux Modules

La plupart des modules sont des singletons accessibles via une méthode statique `get()`.

```java
import com.hypixel.hytale.server.core.modules.entity.EntityModule;

EntityModule entityModule = EntityModule.get();
```

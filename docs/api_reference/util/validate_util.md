# 🛡️ ValidateUtil (Validation des Données)

La classe `ValidateUtil` fournit des méthodes statiques pour valider des valeurs numériques et des objets liés à la position/direction, afin de s'assurer de leur "sécurité" (absence de `NaN` ou de valeurs infinies).

## 1. Validation des Nombres
Ces méthodes vérifient qu'un nombre n'est pas `NaN` (Not a Number) et qu'il est fini (pas infini).

```java
import com.hypixel.hytale.server.core.util.ValidateUtil;

double myDouble = 1.0 / 0.0; // Ceci est Infinity
float myFloat = Float.NaN;   // Ceci est NaN

boolean safeDouble = ValidateUtil.isSafeDouble(10.5); // true
boolean unsafeDouble = ValidateUtil.isSafeDouble(myDouble); // false

boolean safeFloat = ValidateUtil.isSafeFloat(5.0f); // true
boolean unsafeFloat = ValidateUtil.isSafeFloat(myFloat); // false
```

## 2. Validation des Objets de Jeu
Ces méthodes utilisent les validations numériques pour vérifier la sécurité des objets `Position` et `Direction`.

```java
import com.hypixel.hytale.protocol.Direction;
import com.hypixel.hytale.protocol.Position;

// Hypothèse: Position et Direction sont des classes de l'API Hytale
Position safePos = new Position(10.0, 20.0, 30.0);
Position unsafePos = new Position(Double.NaN, 10.0, 20.0);

Direction safeDir = new Direction(0.0f, 0.0f, 0.0f);
Direction unsafeDir = new Direction(Float.POSITIVE_INFINITY, 0.0f, 0.0f);

boolean isPosSafe = ValidateUtil.isSafePosition(safePos); // true
boolean isPosUnsafe = ValidateUtil.isSafePosition(unsafePos); // false

boolean isDirSafe = ValidateUtil.isSafeDirection(safeDir); // true
boolean isDirUnsafe = ValidateUtil.isSafeDirection(unsafeDir); // false
```

## 3. Implication pour les Développeurs de Plugins
*   **Robustesse** : Utilisez ces méthodes pour valider les données que vous recevez (par exemple, des entrées utilisateur ou des données de calcul) avant de les utiliser dans des opérations critiques (téléportation, calcul de dégâts).
*   **Prévention des Bugs** : Évitez les comportements inattendus du jeu causés par des valeurs numériques invalides.

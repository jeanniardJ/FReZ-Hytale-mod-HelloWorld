# 🔍 NameMatching Utility

La classe `NameMatching` est un `enum` qui fournit des stratégies pour rechercher des entités (comme les joueurs) par leur nom, avec différentes options de correspondance.

## 1. Stratégies de Correspondance

| Stratégie | Description | Sensible à la casse ? |
| :--- | :--- | :--- |
| `EXACT` | Le nom doit correspondre exactement. | Oui |
| `EXACT_IGNORE_CASE` | Le nom doit correspondre exactement. | Non |
| `STARTS_WITH` | Le nom doit commencer par la chaîne de recherche. | Oui |
| `STARTS_WITH_IGNORE_CASE` | Le nom doit commencer par la chaîne de recherche. | Non |

`NameMatching.DEFAULT` est défini sur `STARTS_WITH_IGNORE_CASE`.

## 2. Utilisation
Ces stratégies sont utilisées avec les méthodes de recherche de joueurs dans la classe `Universe`.

```java
import com.hypixel.hytale.server.core.Universe;
import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.universe.PlayerRef;

// Recherche par défaut (commence par, insensible à la casse)
PlayerRef player1 = Universe.get().getPlayerByUsername("ste", NameMatching.DEFAULT); 
// Si "Steve" est connecté, player1 sera "Steve".

// Recherche exacte, sensible à la casse
PlayerRef player2 = Universe.get().getPlayerByUsername("Steve", NameMatching.EXACT);

// Recherche "commence par", sensible à la casse
PlayerRef player3 = Universe.get().getPlayerByUsername("ste", NameMatching.STARTS_WITH);
```

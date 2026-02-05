# NameMatching API

Utilitaire pour la recherche de joueurs ou d'entités par nom, avec différentes stratégies de correspondance.

## Stratégies (`NameMatching`)

* **`EXACT`** : Le nom doit correspondre exactement (sensible à la casse).
    * "Steve" == "Steve"
    * "steve" != "Steve"
* **`CASE_INSENSITIVE`** : Le nom doit correspondre, peu importe la casse.
    * "steve" == "Steve"
* **`START_WITH`** : Le nom doit commencer par la chaîne donnée.
    * "Ste" correspond à "Steve"
* **`DEFAULT`** : Généralement `CASE_INSENSITIVE` ou une combinaison intelligente.

## Utilisation

```java
import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.universe.Universe;

// Recherche d'un joueur
PlayerRef player = Universe.get().getPlayerByUsername("steve", NameMatching.CASE_INSENSITIVE);
```

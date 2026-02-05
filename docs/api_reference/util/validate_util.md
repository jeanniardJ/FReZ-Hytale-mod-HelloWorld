# ValidateUtil API

Classe utilitaire pour valider des arguments ou des états, similaire à `Preconditions` de Guava mais adaptée au contexte
Hytale.

## Méthodes Courantes

```java
import com.hypixel.hytale.server.core.util.ValidateUtil;

// Vérifier qu'une condition est vraie
ValidateUtil.checkState(isEnabled, "Le système doit être activé");

// Vérifier qu'un argument n'est pas null
ValidateUtil.checkNotNull(player, "Le joueur ne peut pas être null");

// Vérifier qu'une chaîne n'est pas vide
ValidateUtil.checkNotEmpty(name, "Le nom ne peut pas être vide");
```

Ces méthodes lancent généralement une `IllegalArgumentException` ou une `IllegalStateException` si la validation échoue.

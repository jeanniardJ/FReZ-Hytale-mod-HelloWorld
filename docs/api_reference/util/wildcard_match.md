# WildcardMatch API

Utilitaire pour effectuer des comparaisons de chaînes avec des caractères génériques (wildcards).

## Caractères Spéciaux

* `*` : Correspond à n'importe quelle séquence de caractères (0 ou plus).
* `?` : Correspond à exactement un caractère.

## Utilisation

```java
import com.hypixel.hytale.server.core.util.WildcardMatch;

// Créer un matcher
WildcardMatch matcher = new WildcardMatch();

// Tester
boolean match1 = matcher.match("Hello World", "Hello*"); // true
boolean match2 = matcher.match("Test", "T?st"); // true
boolean match3 = matcher.match("Bonjour", "*jour"); // true
```

Utile pour les systèmes de permissions, les filtres de chat ou les commandes.

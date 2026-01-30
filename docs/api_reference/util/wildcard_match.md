# 🧩 WildcardMatch Utility

La classe `WildcardMatch` fournit des méthodes statiques pour effectuer des correspondances de chaînes de caractères en utilisant des caractères génériques (wildcards).

## 1. Caractères Génériques
*   `*` : Correspond à zéro ou plusieurs caractères.
*   `?` : Correspond à exactement un caractère.

## 2. Utilisation
La méthode `test()` permet de vérifier si un texte correspond à un modèle donné.

```java
import com.hypixel.hytale.server.core.util.WildcardMatch;

// Correspondance simple
boolean match1 = WildcardMatch.test("hello", "hello"); // true

// Utilisation de '*'
boolean match2 = WildcardMatch.test("myplugin.admin.command", "myplugin.admin.*"); // true
boolean match3 = WildcardMatch.test("myplugin.user.command", "myplugin.admin.*"); // false

// Utilisation de '?'
boolean match4 = WildcardMatch.test("item1", "item?"); // true
boolean match5 = WildcardMatch.test("item10", "item?"); // false (car '?' est un seul caractère)

// Ignorer la casse
boolean match6 = WildcardMatch.test("Hello", "hello", true); // true
boolean match7 = WildcardMatch.test("Hello", "hello", false); // false
```

## 3. Implication pour les Développeurs de Plugins
*   **Permissions** : Utile pour vérifier si un joueur a une permission comme `myplugin.command.*`.
*   **Filtres** : Pour filtrer des noms de mondes, d'entités, ou d'autres chaînes de caractères basées sur des motifs.
*   **Configuration** : Pour définir des règles de configuration qui utilisent des motifs.

# Sneaky Throw

Technique utilisée en interne par Hytale (et Java en général) pour lancer des exceptions vérifiées (Checked Exceptions)
sans les déclarer dans la signature de la méthode.

## Utilité

Permet d'utiliser des lambdas et des streams avec des méthodes qui lancent des exceptions, sans avoir à envelopper
chaque appel dans un `try-catch`.

## Exemple

```java
import com.hypixel.hytale.sneakythrow.SneakyThrow;

public void myMethod() {
    // Lance une IOException sans avoir besoin de "throws IOException"
    SneakyThrow.sneakyThrow(new IOException("Erreur cachée"));
}
```

> **Attention** : À utiliser avec précaution. Cela peut rendre le débogage plus difficile si l'exception n'est pas
> attrapée plus haut.

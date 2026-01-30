# 👻 Le Pattern "Sneaky Throw"

Ce document explique le pattern "Sneaky Throw" utilisé en interne par Hytale, basé sur l'analyse de `ThrowableRunnable.java` et `SneakyThrow.java`.

**Note :** Il s'agit d'un pattern de bas niveau. Les développeurs de plugins ne sont pas censés l'utiliser.

## 1. Le Problème : Les Exceptions "Checked"
En Java, les méthodes des interfaces fonctionnelles comme `Runnable` ou `Consumer` ne peuvent pas déclarer d'exceptions "checked" (ex: `IOException`).

**Code Verbeux (Sans Sneaky Throw):**
```java
public void maMethode() {
    Runnable monRunnable = () -> {
        try {
            // Cette méthode lance une IOException
            methodeQuiLanceUneException(); 
        } catch (IOException e) {
            // On est obligé de catcher ici, même si on voudrait que l'erreur remonte plus haut.
            // Souvent, on finit par "wrapper" l'exception, ce qui est lourd.
            throw new RuntimeException(e);
        }
    };
}
```

## 2. La Solution : `SneakyThrow`
`SneakyThrow` est une astuce qui permet de lancer une exception "checked" sans que le compilateur ne s'en aperçoive. Elle la "déguise" en exception "unchecked".

**Le mécanisme interne :**
```java
// La méthode clé qui "trompe" le compilateur
private static <T extends Throwable> T sneakyThrow0(Throwable t) throws T {
    throw t;
}

// La méthode publique qui lance l'exception
public static RuntimeException sneakyThrow(@Nonnull Throwable t) {
    return (RuntimeException)sneakyThrow0(t);
}
```

**Wrappers pour les lambdas :**
La classe fournit aussi des méthodes "passerelles" comme `sneakyRunnable` qui permettent d'utiliser des lambdas qui lancent des exceptions dans des contextes qui ne le permettent pas nativement.

```java
// Permet d'utiliser une lambda qui lance une exception comme un simple Runnable.
Runnable r = SneakyThrow.sneakyRunnable(() -> {
    Files.readString(Paths.get("...")); // Lance une IOException
});
```

## 3. Implication pour les Développeurs de Plugins
*   **Ne pas utiliser** : C'est une technique avancée qui peut rendre le code plus difficile à déboguer si elle est mal utilisée.
*   **Comprendre le code décompilé** : Si vous voyez une méthode interne de Hytale qui semble "avaler" une exception, il est possible qu'elle utilise ce pattern.

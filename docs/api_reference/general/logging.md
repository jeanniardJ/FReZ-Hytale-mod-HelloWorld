# 📝 Logging API (HytaleLogger)

Le `HytaleLogger` est le système centralisé pour écrire des messages dans la console du serveur. Il est basé sur le logger Flogger de Google et utilise les niveaux de log standards de Java.

## 1. Obtenir une instance du Logger
La meilleure pratique est d'obtenir un logger spécifique à votre classe.

```java
import com.hypixel.hytale.logger.HytaleLogger;

public class MaClasse {
    // Crée un logger nommé d'après la classe "MaClasse"
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
    // ...
}
```

## 2. Utilisation de base
La syntaxe est fluide et se lit comme une phrase.

```java
import java.util.logging.Level;

// Log d'information simple
LOGGER.at(Level.INFO).log("Le plugin a démarré.");

// Log avec des paramètres (plus performant que la concaténation de String)
LOGGER.at(Level.INFO).log("Le joueur %s a rejoint le monde %s.", playerName, worldName);
```

## 3. Niveaux de Log
Utilisez le bon niveau pour chaque situation.

*   **`Level.INFO`** : Pour les informations générales (démarrage, actions normales).
    ```java
    LOGGER.at(Level.INFO).log("Démarrage du service d'annonces.");
    ```
*   **`Level.WARNING`** : Pour les problèmes non critiques qui n'empêchent pas le fonctionnement.
    ```java
    LOGGER.at(Level.WARNING).log("Le fichier de configuration est vide, utilisation des valeurs par défaut.");
    ```
*   **`Level.SEVERE`** : Pour les erreurs critiques qui empêchent une fonctionnalité de marcher.
    ```java
    LOGGER.at(Level.SEVERE).log("Impossible de se connecter à la base de données !");
    ```

## 4. Logger une Erreur (Exception)
Il est crucial d'inclure l'exception dans le log pour pouvoir la déboguer.

```java
try {
    // ... code qui peut échouer
} catch (Exception e) {
    // On utilise .withCause() pour attacher la stack trace au message.
    LOGGER.at(Level.SEVERE)
          .withCause(e)
          .log("Une erreur est survenue lors du chargement des données du joueur.");
}
```

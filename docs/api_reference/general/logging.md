# Logging API

Hytale utilise un système de logging standardisé pour écrire dans la console et les fichiers de logs.

## Utilisation de `HytaleLogger` (Recommandé)

C'est la méthode la plus simple et la plus propre.

```java
import com.hypixel.hytale.logger.HytaleLogger;

// Info (Niveau par défaut)
HytaleLogger.info("Ceci est un message d'information.");

// Warning (Avertissement)
HytaleLogger.warn("Attention, quelque chose ne va pas !");

// Error (Erreur grave)
HytaleLogger.error("Erreur critique : " + exception.getMessage());

// Debug (Visible seulement si le mode debug est activé)
HytaleLogger.debug("Données brutes : " + data);
```

## Utilisation de `Log` (Classe utilitaire du projet)

Dans ce projet, nous avons une classe utilitaire `Log` qui encapsule `HytaleLogger` ou le logger Java standard.

```java
import com.jjeanniard.plugins.Log;

Log.info("Message");
Log.warning("Attention");
Log.severe("Erreur");
```

## Bonnes Pratiques

1. **Pas de `System.out.println`** : Utilisez toujours le logger. `System.out` ne respecte pas le formatage des logs et
   peut être perdu.
2. **Contexte** : Incluez des informations contextuelles (nom du joueur, ID de l'entité) dans vos messages d'erreur.
3. **Niveaux** :
    * `INFO` : Démarrage, arrêt, actions administratives majeures.
    * `WARN` : Configuration invalide, erreur récupérable.
    * `ERROR` : Exception non gérée, échec critique d'une fonctionnalité.
    * `DEBUG` : Détails techniques pour le développement.

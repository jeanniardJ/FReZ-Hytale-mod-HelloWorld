# Système d'Arrêt (Shutdown System)

Procédure d'arrêt propre du serveur.

## Déclenchement

L'arrêt peut être déclenché par :

* La commande `/stop`.
* Un signal système (SIGINT/Ctrl+C).
* Une erreur critique.

## Cycle d'Arrêt

1. **Désactivation du réseau** : Le serveur arrête d'accepter de nouvelles connexions.
2. **Kick des joueurs** : Tous les joueurs sont déconnectés avec un message de fermeture.
3. **Arrêt des plugins** : La méthode `shutdown()` de chaque plugin est appelée.
4. **Sauvegarde des mondes** : Tous les chunks et entités sont sauvegardés sur le disque.
5. **Arrêt des threads** : Les threads de travail (workers) sont terminés.
6. **Fermeture de la JVM**.

## Bonnes Pratiques pour les Plugins

Dans votre méthode `shutdown()` :

* Arrêtez tous vos threads et timers (`ScheduledExecutorService`).
* Fermez les connexions aux bases de données.
* Sauvegardez vos données si ce n'est pas fait automatiquement.

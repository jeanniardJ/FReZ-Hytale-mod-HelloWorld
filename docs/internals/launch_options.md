# Options de Lancement (Launch Options)

Arguments de ligne de commande et configuration de démarrage du serveur Hytale.

## Arguments Communs

* `--port <port>` : Définit le port d'écoute du serveur (défaut: 25565).
* `--world-name <name>` : Nom du monde principal à charger.
* `--minigame <id>` : Lance le serveur en mode mini-jeu spécifique.
* `--debug` : Active le mode débogage (logs verbeux, fonctionnalités de test).
* `--no-watchdog` : Désactive le chien de garde (watchdog) qui détecte les blocages du serveur.

## Configuration `server.properties`

Le fichier `server.properties` à la racine du serveur contient les options persistantes.

```properties
server-port=25565
max-players=100
view-distance=10
online-mode=true
```

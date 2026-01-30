# 🚀 Options de Lancement du Serveur

Ce document liste les options de ligne de commande disponibles pour configurer et lancer le serveur Hytale, basé sur l'analyse de `Options.java`.

## 1. Utilisation
Les options sont passées lors de l'exécution du JAR du serveur.
```bash
java -jar HytaleServer.jar --bind 0.0.0.0:25565 --mods ./plugins
```

## 2. Options Principales

### Réseau
*   `--bind <ip:port>` : Définit l'adresse IP et le port sur lesquels le serveur écoute. (Alias: `-b`)
*   `--transport <type>` : Définit le type de transport réseau (`QUIC`, `TCP`, etc.). (Alias: `-t`)

### Dossiers
*   `--assets <path>` : Chemin vers le dossier des assets Hytale.
*   `--mods <path1>,<path2>` : Chemins vers les dossiers contenant les plugins/mods.
*   `--universe <path>` : Chemin vers le dossier de l'univers (qui contient les mondes).
*   `--world-gen <path>` : Chemin vers un dossier de génération de monde personnalisée.
*   `--backup-dir <path>` : Dossier où stocker les sauvegardes.

### Plugins & Mods
*   `--accept-early-plugins` : Requis pour charger des plugins "early" (non sécurisés).
*   `--early-plugins <path>` : Dossiers pour les plugins "early".

### Authentification
*   `--auth-mode <mode>` : Définit le mode d'authentification.
    *   `AUTHENTICATED` : Mode en ligne standard.
    *   `OFFLINE` : Mode hors ligne (les joueurs n'ont pas besoin d'être authentifiés).
    *   `INSECURE` : Mode hors ligne sans vérification des noms.
*   `--owner-name <name>` / `--owner-uuid <uuid>` : Définit le propriétaire du serveur (utile en mode solo).

### Débogage & Validation
*   `--log <package:level>` : Définit le niveau de log pour un package spécifique (ex: `--log com.myplugin:DEBUG`).
*   `--event-debug` : Active les logs détaillés pour le bus d'événements.
*   `--validate-assets` : Valide les assets au démarrage et s'arrête en cas d'erreur.
*   `--shutdown-after-validate` : Arrête le serveur après la validation.

### Commandes
*   `--boot-command <command1>,<command2>` : Exécute une ou plusieurs commandes au démarrage du serveur.
*   `--allow-op` : Permet aux joueurs de s'auto-opérer (probablement via une commande).

---

## 3. Implication pour les Développeurs de Plugins
*   **Structure des Dossiers** : Comprendre où le serveur s'attend à trouver les plugins (`--mods`).
*   **Débogage** : Utiliser `--log` et `--event-debug` pour obtenir plus d'informations lors du développement.
*   **Tests** : Utiliser `--auth-mode OFFLINE` pour tester sans avoir besoin d'une connexion internet.

# FReZ-Hytale-mod-HelloWorld

Un plugin d'exemple et de base pour Hytale, démontrant les fonctionnalités essentielles comme la gestion des annonces, les commandes, et l'intégration avec l'API Hytale.

## Fonctionnalités

*   **Système d'Annonces Automatiques** :
    *   Diffusion de messages périodiques aux joueurs.
    *   Support des annonces globales et spécifiques à un monde (univers).
    *   Placeholders dynamiques : `{online}` (joueurs en ligne), `{playersMax}` (capacité du serveur).
*   **Gestion des Annonces** :
    *   Commandes complètes pour créer, lister, modifier et supprimer des annonces en jeu ou via la console.
    *   Interface graphique (GUI) pour les joueurs permettant une gestion visuelle des annonces.
*   **Commandes Administratives** :
    *   Système de permissions intégré (`helloworld.announce.admin`, `helloworld.announce.reload`).
    *   Rechargement de la configuration à chaud.

## Installation

1.  Téléchargez le fichier `.jar` du plugin depuis la section [Releases](https://github.com/jeanniardJ/FReZ-Hytale-mod-HelloWorld/releases).
2.  Placez le fichier dans le dossier `mods` de votre serveur Hytale.
3.  Démarrez votre serveur.

## Configuration

Le fichier de configuration `config.json` est généré automatiquement au premier lancement.

```json
{
  "announceInterval": 300,
  "globalAnnouncements": [
    "Bienvenue sur notre serveur Hytale !",
    "N'oubliez pas de visiter notre site web."
  ],
  "universeAnnouncements": {
    "lobby": [
      "Bienvenue au lobby !"
    ],
    "minigame_world": [
      "Que le meilleur gagne !"
    ]
  }
}
```

*   `announceInterval` : Temps en secondes entre chaque annonce automatique.
*   `globalAnnouncements` : Liste des messages diffusés sur tout le serveur.
*   `universeAnnouncements` : Dictionnaire des messages spécifiques à chaque monde.

## Commandes et Permissions

| Commande | Description | Permission Requise |
| :--- | :--- | :--- |
| `/announce` | Ouvre le panneau de gestion (GUI) pour les joueurs, ou affiche l'aide pour la console. | `helloworld.announce.admin` |
| `/announce list` | Liste toutes les annonces configurées avec leur ID. | `helloworld.announce.admin` |
| `/announce create <message> [--world=<monde>]` | Crée une nouvelle annonce. Optionnel : spécifier un monde cible. | `helloworld.announce.admin` |
| `/announce delete <index>` | Supprime une annonce par son ID (index). | `helloworld.announce.admin` |
| `/announce edit <index> <nouveau_message>` | Modifie le texte d'une annonce existante. | `helloworld.announce.admin` |
| `/announce reload` | Recharge la configuration du plugin depuis le fichier. | `helloworld.announce.reload` |

## Auteur

Développé par [jeanniardJ](https://github.com/jeanniardJ) (JonasBadBoys).

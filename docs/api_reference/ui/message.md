# 💬 Message API

La classe `Message` est un constructeur (builder) puissant pour créer des messages texte riches et formatés, destinés à être envoyés dans le chat ou affichés dans l'interface utilisateur.

## 1. Créer un Message

### Message Simple (Texte Brut)
```java
import com.hypixel.hytale.server.core.Message;

Message msg = Message.raw("Bonjour le monde !");
```

### Message Traduisible (i18n)
Utilise une clé de traduction du jeu ou de votre plugin.
```java
Message welcomeMsg = Message.translation("gui.welcome_message");
```

## 2. Mettre en Forme un Message
Les méthodes peuvent être chaînées.

```java
import java.awt.Color;

Message styledMsg = Message.raw("Attention !")
    .color("#FF5555")       // Couleur en héxadécimal
    .bold(true)
    .italic(true);

Message styledMsg2 = Message.raw("Info")
    .color(Color.CYAN);     // Couleur via l'objet java.awt.Color
```

### Ajouter un Lien
```java
Message linkMsg = Message.raw("Cliquez ici")
    .link("https://hytale.com");
```

## 3. Combiner des Messages
Pour créer une ligne avec plusieurs formats.

```java
Message part1 = Message.raw("Le joueur ").color("gray");
Message part2 = Message.raw("Steve").color("gold").bold(true);
Message part3 = Message.raw(" a rejoint.").color("gray");

// Combine les parties en un seul message
Message fullMsg = Message.join(part1, part2, part3);

// Résultat visuel : Le joueur Steve a rejoint.
```

## 4. Utiliser des Paramètres (pour les traductions)
Si votre clé de traduction est `welcome.user=Bienvenue, {user} !`, vous pouvez passer la valeur de `{user}`.

```java
Message welcomeUser = Message.translation("welcome.user")
    .param("user", "Steve");
```

## 5. Envoyer le Message
Une fois le message construit, envoyez-le via `PlayerRef` ou `Universe`.

```java
// Envoyer à un joueur
player.sendMessage(fullMsg);

// Envoyer à tout le monde
Universe.get().sendMessage(fullMsg);
```

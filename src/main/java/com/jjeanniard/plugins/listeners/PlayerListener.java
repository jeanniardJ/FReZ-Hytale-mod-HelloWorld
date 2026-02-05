package com.jjeanniard.plugins.listeners;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import com.jjeanniard.plugins.Log;
import com.jjeanniard.plugins.services.WelcomeService;

/**
 * Cette classe "écoute" ce qui se passe sur le serveur.
 * Ici, elle s'intéresse spécifiquement aux connexions de joueurs.
 */
public final class PlayerListener {
    private final WelcomeService welcomeService;

    public PlayerListener(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }

    /**
     * Cette méthode est appelée automatiquement par Hytale à chaque fois qu'un joueur se connecte.
     *
     * @param event L'objet événement contenant toutes les infos (qui est le joueur, quand, etc.)
     */
    public void onPlayerJoin(PlayerConnectEvent event) {
        // On récupère la référence du joueur qui vient d'arriver
        PlayerRef player = event.getPlayerRef();

        // TODO: Vérifier si c'est vraiment sa première fois (API à venir)
        boolean isFirstJoin = false;

        // 1. On demande au service quel est le texte brut (ex : "Bienvenue {player}")
        String rawMessage = welcomeService.getWelcomeMessage(isFirstJoin);

        // 2. On remplace "{player}" par le vrai nom "Steve"
        String formattedMessage = rawMessage.replace("{player}", player.getUsername());

        // 3. On affiche un grand titre à l'écran du joueur
        EventTitleUtil.showEventTitleToPlayer(
                player,
                Message.raw(formattedMessage), // Titre principal
                Message.raw("Amuse-toi bien !"), // Sous-titre
                true,                          // isMajor (Vrai = Gros titre, Faux = Petit titre au dessus de la barre d'action)
                "ui/icons/announcement.png",   // Chemin vers l'icône (dans les assets du jeu)
                10.0f,                          // Durée d'affichage (secondes)
                1.5f,                          // Temps d'apparition (Fade in)
                1.5f                           // Temps de disparition (Fade out)
        );

        Log.info("Joueur connecté : %s", player.getUsername());
    }

    /**
     * Appelé lorsqu'un joueur est ajouté à un monde (connexion initiale ou téléportation).
     * Affiche un titre de bienvenue spécifique au monde.
     */
    public void onPlayerJoinUnivers(AddPlayerToWorldEvent event) {
        // Récupération du composant Player (ECS) depuis l'événement
        Player playerComponent = event.getHolder().getComponent(Player.getComponentType());
        String worldName = event.getWorld().getName();
        
        // Note: playerComponent.getName() n'existe pas.
        // Nous devons utiliser l'UUID du Holder (l'entité elle-même).
        // Correction : Holder n'a pas de méthode getUuid() directement accessible ici selon le compilateur.
        // Mais l'entité (EntityStore) a un UUID.
        // event.getHolder() retourne un Holder<EntityStore>.
        // Pour avoir l'UUID, il faut accéder à l'EntityStore.
        
        // Cependant, AddPlayerToWorldEvent est générique.
        // Essayons de récupérer le PlayerRef d'une manière plus robuste.
        // L'événement AddPlayerToWorldEvent est souvent déclenché avec un contexte qui permet de remonter au joueur.
        
        // Si nous ne pouvons pas obtenir l'UUID facilement, itérons sur les joueurs du monde.
        // C'est moins performant mais fonctionnel.
        
        // Mais attendez, PlayerConnectEvent nous donne le PlayerRef directement.
        // AddPlayerToWorldEvent est plus bas niveau (ECS).
        
        // Essayons de récupérer le composant PlayerRef depuis le Holder s'il existe.
        // Le composant PlayerRef est souvent attaché à l'entité joueur.
        
        // Correction : hasComponent n'existe pas sur Holder. Il faut utiliser getComponent et gérer l'exception ou vérifier autrement.
        // Mais Holder a une méthode getComponent qui lance une exception si le composant n'existe pas.
        // Il faut utiliser hasComponent si elle existe, ou try/catch.
        
        // Vérifions si PlayerRef est un composant valide pour cette entité.
        // Dans l'architecture Hytale, PlayerRef est souvent un composant de l'entité joueur.
        
        try {
            PlayerRef playerRef = event.getHolder().getComponent(PlayerRef.getComponentType());
             Log.debug("Le joueur " + playerRef.getUsername() + " a rejoint le monde : " + worldName);
             
             EventTitleUtil.showEventTitleToPlayer(
                playerRef,
                Message.raw("Bienvenue sur " + worldName), // Titre principal
                Message.raw("Exploration en cours..."), // Sous-titre
                false,                          // isMajor (Faux = Petit titre)
                "ui/icons/world.png",           // Chemin vers l'icône
                5.0f,                           // Durée
                1.0f,                           // Fade in
                1.0f                            // Fade out
            );
        } catch (Exception e) {
            // Fallback : Si le composant PlayerRef n'est pas directement sur l'entité (ce qui est rare pour un joueur connecté)
            // On peut essayer de matcher par le composant Player qui contient souvent le nom ou l'ID.
            // Mais Player (le composant) ne semble pas exposer getName() ou getUuid() publiquement facilement ici.
            
            Log.warning("Impossible de trouver le PlayerRef pour l'entité dans AddPlayerToWorldEvent");
        }
    }
}

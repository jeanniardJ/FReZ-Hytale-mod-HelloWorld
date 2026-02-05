# Système d'Interface Utilisateur (UI)

Hytale utilise un système d'UI déclaratif puissant qui sépare la mise en page (fichiers `.ui`) de la logique (code Java).

## Structure des Fichiers

Les fichiers UI doivent être placés dans une structure de dossiers spécifique dans `src/main/resources` :

```
src/main/resources/
└── Common/
    └── UI/
        └── Custom/
            └── Pages/
                └── VotrePage.ui
```

## Configuration

### 1. Manifest (`manifest.json`)
Il faut indiquer que le plugin contient des assets :
```json
{
  "IncludesAssetPack": true
}
```

### 2. Import Common.ui
Au début de chaque fichier `.ui`, il est recommandé d'importer les styles communs :
```
$C = "../Common.ui";
```

## Création d'une Page (Java)

Il existe deux types de pages principaux :
*   `BasicCustomUIPage` : Pour l'affichage statique.
*   `InteractiveCustomUIPage<T>` : Pour les formulaires interactifs avec boutons et champs de texte.

### Exemple : Page Interactive

```java
public class MaPage extends InteractiveCustomUIPage<MaPage.EventData> {
    
    // Classe de données pour lier les champs (Binding)
    public static class EventData {
        public String inputValue;
        
        public static final BuilderCodec<EventData> CODEC = BuilderCodec
            .builder(EventData.class, EventData::new)
            .append(new KeyedCodec<>("@inputValue", new StringCodec()),
                (obj, val) -> obj.inputValue = val,
                obj -> obj.inputValue)
            .add()
            .build();
    }

    public MaPage(PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, EventData.CODEC);
    }

    @Override
    public void build(Ref<EntityStore> ref, UICommandBuilder cmd, UIEventBuilder events, Store<EntityStore> store) {
        cmd.append("Pages/MaPage.ui");
        
        // Liaison des événements
        events.addEventBinding(CustomUIEventBindingType.Activating, "#monBouton",
            new EventData().append("@inputValue", "#monChamp.Value"));
    }
    
    @Override
    public void handleDataEvent(Ref<EntityStore> ref, Store<EntityStore> store, EventData data) {
        // Logique lors de la soumission
    }
}
```

## Styles et Couleurs

### Variables Utiles (Common.ui)
*   `$C.@DefaultTextButtonStyle` : Bouton bleu standard.
*   `$C.@SecondaryTextButtonStyle` : Bouton gris (Annuler).
*   `$C.@CancelTextButtonStyle` : Bouton rouge (Supprimer).
*   `$C.@InputBoxBackground` : Fond pour champ de texte.

### Palette de Couleurs (Hex)
*   `#1e2a3a` : Barre de titre
*   `#232d3f` : Fond de contenu
*   `#bfcdd5` : Texte de titre
*   `#e5e7eb` : Texte principal

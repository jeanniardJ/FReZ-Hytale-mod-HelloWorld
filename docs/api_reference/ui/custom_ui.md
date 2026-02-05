# 🖥️ Custom UI (Interfaces Utilisateur)

Hytale permet de créer des interfaces graphiques interactives (GUI) pour les joueurs.

## 1. Créer une Page (`InteractiveCustomUIPage`)

Une page UI est une classe Java qui hérite de `InteractiveCustomUIPage`. Elle définit la structure de l'interface et
gère les événements.

```java
public class MyPage extends InteractiveCustomUIPage<MyPage.MyEventData> {

    // Définition de la structure des données d'événement (Codec)
    public static class MyEventData {
        public String action;
        public String inputValue;

        public static final Codec<MyEventData> CODEC = BuilderCodec.builder(MyEventData.class, MyEventData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING), (o, v) -> o.action = v, o -> o.action)
                // Pour binder un input, la clé doit commencer par '@' dans le Codec ET dans l'EventData
                .append(new KeyedCodec<>("@InputValue", Codec.STRING), (o, v) -> o.inputValue = v, o -> o.inputValue)
                .build();
    }

    public MyPage(PlayerRef player) {
        super(player, CustomPageLifetime.CanDismiss, MyEventData.CODEC);
    }

    @Override
    public void build(Ref<EntityStore> ref, UICommandBuilder cmd, UIEventBuilder events, Store<EntityStore> store) {
        // Charger un fichier template .ui
        cmd.append("Pages/MyPage.ui");

        // Modifier un élément statique
        cmd.set("#Title.Text", "Mon Titre");

        // Binding d'un bouton simple
        EventData btnEvent = new EventData();
        btnEvent.append("Action", "CLICK_BUTTON");
        events.addEventBinding(CustomUIEventBindingType.Activating, "#MyButton", btnEvent);

        // Binding d'un bouton avec récupération de valeur (Input)
        EventData saveEvent = new EventData();
        saveEvent.append("Action", "SAVE");
        // '@' indique qu'on veut la valeur de l'élément UI, pas la chaîne littérale
        saveEvent.append("@InputValue", "#MyInput.Value");
        events.addEventBinding(CustomUIEventBindingType.Activating, "#SaveButton", saveEvent);
    }

    @Override
    public void handleDataEvent(Ref<EntityStore> ref, Store<EntityStore> store, MyEventData data) {
        if ("SAVE".equals(data.action)) {
            // data.inputValue contient le texte saisi par le joueur
            Log.info("Valeur reçue : " + data.inputValue);
        } else if ("CLOSE".equals(data.action)) {
            // Fermer la page
            this.close();
        }
    }
}
```

## 2. Listes Dynamiques

Pour afficher une liste d'éléments variable, il faut utiliser des sélecteurs indexés car `cmd.append` ne permet pas de
définir un ID unique à la volée.

```java
List<String> items = List.of("Item 1", "Item 2", "Item 3");
int index = 0;

for (String item : items) {
    // 1. Ajouter le template de l'item à la liste parente
    cmd.append("#MyListContainer", "Pages/ItemTemplate.ui");
    
    // 2. Cibler le N-ième enfant de la liste
    String selector = "#MyListContainer[" + index + "]";
    
    // 3. Modifier les propriétés via ce sélecteur relatif
    cmd.set(selector + " #ItemLabel.Text", item);
    
    // 4. Ajouter des événements spécifiques
    EventData event = new EventData();
    event.append("Action", "SELECT_ITEM");
    event.append("Index", String.valueOf(index));
    events.addEventBinding(CustomUIEventBindingType.Activating, selector + " #SelectBtn", event);
    
    index++;
}
```

## 3. Ouvrir l'Interface

L'ouverture doit se faire sur le thread du monde pour éviter les problèmes de concurrence.

```java
// Récupérer le monde depuis le store du joueur
World world = store.getExternalData().getWorld();

world.execute(() -> {
    PlayerRef playerRef = ...; // Récupérer le PlayerRef
    Player playerComponent = ...; // Récupérer le composant Player
    
    // Ouvrir la page
    playerComponent.getPageManager().openCustomPage(ref, store, new MyPage(playerRef));
});
```

## 4. Style des Boutons

Vous pouvez modifier la couleur des boutons directement via le code :

```java
// Couleur de fond
cmd.set("#MyButton.Color", "#22c55e"); // Vert

// Couleur au survol
cmd.set("#MyButton.HoverColor", "#4ade80"); // Vert clair
```

## 5. Structure des Fichiers UI

Les fichiers `.ui` utilisent un format proche du JSON/HJSON.

**Attention :** L'utilisation de variables globales comme `$C = "../Common.ui";` peut causer des erreurs si le fichier
référencé n'existe pas. Il est recommandé de commenter ces lignes si vous n'avez pas le fichier commun.

```hjson
Group {
  LayoutMode: Center;
  // ...
}
```

## 6. Dépannage (Troubleshooting)

### "Failed to load CustomUI documents"

**Cause** : Erreur de syntaxe dans votre fichier `.ui`.
**Solutions** :

1. Vérifiez que toutes les valeurs textuelles sont entre guillemets : `Text: "Hello";` (pas `Text: Hello;`).
2. Vérifiez que toutes les propriétés se terminent par un point-virgule `;`.
3. Vérifiez le format des couleurs : `#ffffff` ou `#fff`.
4. Assurez-vous que l'import de `Common.ui` est correct ou commenté si le fichier n'existe pas : `$C = "../Common.ui";`.

### "Failed to apply CustomUI event bindings"

**Cause** : L'ID de l'élément dans le code Java ne correspond pas à celui du fichier `.ui`.
**Solutions** :

1. Vérifiez que l'ID existe dans le fichier `.ui`.
2. Vérifiez l'orthographe : `#MyButton` en Java doit correspondre à `#MyButton` dans le `.ui`.

### "Selected element in CustomUI command was not found"

**Cause** : Sélecteur incorrect pour les templates ajoutés dynamiquement.
**Compréhension** : Quand vous ajoutez un template à un conteneur, le template lui-même **devient** l'élément à cet
index.
**Correct** : `cmd.set("#Container[0].Text", "Hello");`
**Incorrect** : `cmd.set("#Container[0] #Button.Text", "Hello");` (sauf si le template a des IDs imbriqués).

### Déconnexion du joueur à l'ouverture de la page

**Cause** : Le fichier `.ui` a une erreur de parsing ou n'existe pas.
**Solutions** :

1. Vérifiez que le chemin du fichier correspond : `"YourPlugin/MyPage.ui"` correspond à
   `Common/UI/Custom/YourPlugin/MyPage.ui`.
2. Revoyez la syntaxe du fichier UI.

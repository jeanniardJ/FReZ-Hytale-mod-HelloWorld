#========================================#
#      DIRECTIVES POUR L'ASSISTANT IA      #
#========================================#

Ces règles doivent être respectées par l'assistant IA à chaque interaction sur ce projet.

### Règle #1 : Respect de l'Architecture
*   **ECS d'abord** : Proposer systématiquement des solutions basées sur l'architecture ECS (séparation Données/Logique).
*   **Pas d'héritage abusif** : Éviter de proposer des hiérarchies de classes complexes.

### Règle #2 : Documentation Vivante (Knowledge Base)
*   **RÈGLE CRITIQUE :** À chaque fois qu'une nouvelle méthode ou un nouveau concept de l'API Hytale est découvert, l'assistant doit **immédiatement mettre à jour** la documentation dans le dossier `docs/`.
*   **Procédure** :
    1.  Vérifier si l'information existe déjà dans `docs/api_reference/`.
    2.  Si non, ajouter ou mettre à jour le fichier Markdown pertinent.
    *   *Exemple : Si on découvre `player.kick()`, ajouter l'info dans `docs/api_reference/entity/player.md`.*

### Règle #3 : Qualité du Code
*   **Code Concis** : Éviter le code verbeux inutile.
*   **Contexte Hytale** : Utiliser les APIs Hytale (Logger, Config, Events) par défaut.
*   **Commentaires Pédagogiques** : Le code généré doit être commenté pour expliquer les parties importantes à un développeur junior, conformément aux règles du projet.

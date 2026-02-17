# Changelog

Toutes les modifications notables de ce projet seront documentées dans ce fichier.

Le format est basé sur [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
et ce projet adhère à [Semantic Versioning](https://semver.org/spec/v2.0.html).

## [v2.0.1] - 2026-02-17

### Ajouté
- Configuration du CI/CD pour déclencher les builds sur les tags.
- Permissions ajoutées au job de release pour permettre la création de releases sur GitHub.
- Ajout de la documentation du projet (`FReZ-Hytale-mod-Docs`).
- Ajout de la configuration pour les annonces de l'univers et service de gestion.
- Ajout du système d'annonces et refactorisation de la structure du projet.
- Ajout du fichier de configuration `.whitesource`.

### Modifié
- Mise à jour de `build.gradle.kts` pour utiliser des dépendances conditionnelles (local vs CI).
- Mise à jour de `gradle.properties`, `manifest.json`, et `PlayerListener`.
- Refactorisation du système d'annonces et unification de la logique de service.
- Refactorisation de la structure du projet, du système de configuration.
- Message de commit "Reprise" (changement non spécifié).
- Message de commit "init projet" (changement non spécifié).

### Supprimé
- Suppression des tests obsolètes.

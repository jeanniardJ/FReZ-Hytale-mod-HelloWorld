# ⚙️ Moteur de Stockage Interne (IndexedStorageFile)

Ce document décrit le fonctionnement du moteur de stockage de bas niveau de Hytale, basé sur l'analyse de `IndexedStorageFile.java`.

**Note :** Il s'agit d'une API interne. Les développeurs de plugins ne sont pas censés l'utiliser directement.

## 1. Vue d'ensemble
Hytale utilise un format de fichier binaire personnalisé pour stocker les données du jeu (mondes, entités, etc.). Ce format est conçu pour la haute performance et l'accès concurrentiel.

## 2. Caractéristiques Techniques

*   **Format** : Fichier binaire personnalisé identifié par le header `HytaleIndexedStorage`.
*   **Structure** : Le fichier est divisé en "segments" de taille fixe. Les données sont stockées dans des "blobs" qui peuvent s'étendre sur plusieurs segments. Un index au début du fichier permet un accès rapide à n'importe quel blob.
*   **Compression** : Utilise la bibliothèque **Zstd** (`com.github.luben.zstd`), un algorithme de compression moderne et très rapide.
*   **Accès aux données** : Utilise le "memory mapping" (`MappedByteBuffer`) pour mapper l'index du fichier directement en mémoire, ce qui accélère considérablement les opérations de lecture.
*   **Concurrence** : Le système est entièrement "thread-safe" grâce à une utilisation intensive de `StampedLock`, permettant à plusieurs parties du serveur de lire et écrire des données simultanément sans corruption.
*   **Migration** : Le système est capable de migrer automatiquement les anciens formats de fichiers vers les nouvelles versions.

## 3. Implication pour les Développeurs de Plugins
*   **Ne pas utiliser directement** : L'API est complexe et non destinée à un usage public.
*   **Comprendre la performance** : Les opérations de sauvegarde/chargement du monde sont optimisées.
*   **Dépendances** : On peut voir que le serveur Hytale a une dépendance sur la librairie `com.github.luben.zstd`.

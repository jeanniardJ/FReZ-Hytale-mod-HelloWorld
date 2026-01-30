package com.jjeanniard.plugins.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class AnnounceUniverseConfig {
    @Nonnull
    private String title = "Voyage vers {world}";
    @Nonnull
    private String subtitle = "Préparez-vous !";

    // --- DONNÉES ---
    // C'est la Map que nous voulons remplir.
    // Clé = Nom du monde (String)
    // Valeur = Liste d'annonces (String[])
    public Map<String, String[]> worldAnnouncements = new HashMap<>();


    // --- OUTILS (CODECS) ---

    // Étape 2 : Le Codec pour le Tableau (String[])
    // On le stocke dans une variable pour que ce soit lisible.
    // Type : Codec<String[]>
    private static final Codec<String[]> STRING_ARRAY_CODEC = new ArrayCodec<>(Codec.STRING, String[]::new);

    // Étape 3 : Le Codec pour la Map (Map<String, String[]>)
    // Type : MapCodec<String[], Map<String, String[]>>
    // Note : Le premier type générique est la VALEUR de la map (String[]), le second est le type de la MAP elle-même.
    private static final MapCodec<String[], Map<String, String[]>> MAP_CODEC = new MapCodec<>(
            STRING_ARRAY_CODEC, // Le codec pour les valeurs (notre tableau)
            HashMap::new,       // Comment créer la map
            true               // Modifiable ? Oui (false = pas unmodifiable)
    );

    public static final Codec<AnnounceUniverseConfig> CODEC = BuilderCodec.builder(AnnounceUniverseConfig.class, AnnounceUniverseConfig::new)
            .append(new KeyedCodec<>("Title", Codec.STRING), (c, v, i) -> c.title = v, (c, i) -> c.title)
            .add()
            .append(new KeyedCodec<>("Subtitle", Codec.STRING), (c, v, i) -> c.subtitle = v, (c, i) -> c.subtitle)
            .add()
            .append(new KeyedCodec<>("Announcements", MAP_CODEC),
                    (c, v, i) -> c.worldAnnouncements = v,
                    (c, i) -> c.worldAnnouncements
            )
            .add()
            .build();

    public @Nonnull String getTitle() {
        return title;
    }

    public void setTitle(@Nonnull String title) {
        this.title = title;
    }

    public @Nonnull String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(@Nonnull String subtitle) {
        this.subtitle = subtitle;
    }

    public Map<String, String[]> getWorldAnnouncements() {
        return worldAnnouncements;
    }

    public void setWorldAnnouncements(Map<String, String[]> worldAnnouncements) {
        this.worldAnnouncements = worldAnnouncements;
    }
}
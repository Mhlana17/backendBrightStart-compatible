package za.ac.cput.factory;

import za.ac.cput.domain.Anime;

public class AnimeFactory {
    public static Anime createAnime(int animeId, String title, String genre) {
        return new Anime.Builder()
                .setAnimeId(animeId)
                .setTitle(title)
                .setGenre(genre)
                .build();
    }
}

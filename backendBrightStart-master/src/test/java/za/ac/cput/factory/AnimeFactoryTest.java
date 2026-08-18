package za.ac.cput.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Anime;

import static org.junit.jupiter.api.Assertions.*;

class AnimeFactoryTest {

    @Test
    void createAnime_shouldReturnValidAnime() {
        Anime anime = AnimeFactory.createAnime(1, "Attack on Titan", "Action");

        assertNotNull(anime);
        assertEquals(1, anime.getAnimeId());
        assertEquals("Attack on Titan", anime.getTitle());
        assertEquals("Action", anime.getGenre());
    }
}

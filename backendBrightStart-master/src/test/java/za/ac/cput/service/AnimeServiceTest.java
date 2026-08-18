package za.ac.cput.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import za.ac.cput.service.impl.AnimeServiceImpl;
import za.ac.cput.domain.Anime;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

@Service
class AnimeServiceTest {

    @Autowired
    private AnimeServiceImpl animeService;

//    @BeforeEach
//    void setUp() {
//        animeService = new AnimeServiceImpl();
//    }

    @Test
    @Order(1)
    void testSaveAnime() {
        Anime anime = new Anime.Builder()
                .setAnimeId(1)
                .setTitle("Attack on Titan")
                .setGenre("Action")
                .build();

        Anime saved = animeService.create(anime);
        assertNotNull(saved);
        assertEquals("Attack on Titan", saved.getTitle());
    }

    @Test
    @Order(2)
    void testGetAllAnime() {
        animeService.update(new Anime.Builder()
                .setAnimeId(2)
                .setTitle("Demon Slayer")
                .setGenre("Fantasy")
                .build());

        animeService.update(new Anime.Builder()
                .setAnimeId(3)
                .setTitle("One Piece")
                .setGenre("Adventure")
                .build());

        List<Anime> all = animeService.getAll();
        assertNotNull(all);
        System.out.println(all);
    }

    @Test
    @Order(3)
    void testDeleteAnime() {
        Anime anime = new Anime.Builder()
                .setAnimeId(4)
                .setTitle("Naruto")
                .setGenre("Shonen")
                .build();

        animeService.update(anime);

        boolean isDeleted = animeService.delete(4);
        assertTrue(isDeleted);
    }
}

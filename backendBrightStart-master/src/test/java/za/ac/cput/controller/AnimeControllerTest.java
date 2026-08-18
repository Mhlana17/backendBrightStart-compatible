package za.ac.cput.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.Payment;
import za.ac.cput.service.impl.AnimeServiceImpl;
import za.ac.cput.domain.Anime;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimeControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/anime";

    @Test
    void testCreateAnime() {
        Anime anime = new Anime.Builder()
                .setAnimeId(1)
                .setTitle("Bleach")
                .setGenre("Shonen")
                .build();

        String url = BASE_URL + "/save";
        ResponseEntity<Anime> postResponse = restTemplate.postForEntity(url, anime, Anime.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
        System.out.println("Created: " + postResponse.getBody());
    }

    @Test
    void testGetById() {
        Anime anime = new Anime.Builder()
                .setAnimeId(2)
                .setTitle("Fullmetal Alchemist")
                .setGenre("Adventure")
                .build();

        String url = BASE_URL + "/read/" + 1;
        ResponseEntity<Anime> response = restTemplate.getForEntity(url, Anime.class);
        assertNotNull(response.getBody());
        System.out.println("Read: " + response.getBody());
    }

    @Test
    void testDeleteAnime() {
        Anime anime = new Anime.Builder()
                .setAnimeId(3)
                .setTitle("Tokyo Ghoul")
                .setGenre("Horror")
                .build();

        String url = BASE_URL + "/delete/" + 1;
        restTemplate.delete(url);
    }
}

package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.service.AnimeService;
import za.ac.cput.domain.Anime;

import java.util.List;

@RestController
@RequestMapping("/anime")
//@CrossOrigin(origins = "http://localhost:3000") // React frontend
public class AnimeController {

    @Autowired
    private AnimeService animeService;

    @PostMapping("/save")
    public Anime create(@RequestBody Anime anime) {
        return animeService.create(anime);
    }

    @GetMapping
    public List<Anime> getAll() {
        return animeService.getAll();
    }

    @GetMapping("/read/{animeId}")
    public Anime getById(@PathVariable int animeId) {
        return animeService.read(animeId);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        animeService.delete(id);
    }
}

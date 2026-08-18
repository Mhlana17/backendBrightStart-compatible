package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Anime;
import za.ac.cput.repository.AnimeRepository;
import za.ac.cput.service.AnimeService;

import java.util.List;

@Service
public class AnimeServiceImpl implements AnimeService {

    @Autowired
    private AnimeRepository repository;

//    @Autowired
//    public AnimeServiceImpl() {
//        this.repository = repository;
//    }

    @Override
    public Anime create(Anime anime) {
        return repository.save(anime);
    }

    @Override
    public Anime read(int animeId) {
        return repository.findById(animeId).orElse(null);
    }

    @Override
    public Anime update(Anime anime) {
        if (repository.existsById(anime.getAnimeId())) {
            return repository.save(anime);
        }
        return null;
    }

    @Override
    public boolean delete(int animeId) {
//        if (repository.existsById(animeId)) {
            repository.deleteById(animeId);
//            return true;
//        }
        return !repository.existsById(animeId);
    }

    @Override
    public List<Anime> getAll() {
        return repository.findAll();
    }

    @Override
    public Anime getById(int id) {
        return null;
    }
}

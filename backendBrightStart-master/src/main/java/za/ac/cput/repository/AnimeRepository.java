package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Anime;
import java.util.List;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Integer> {
    List<Anime> findByGenre(String genre);
    List<Anime> findByTitleContainingIgnoreCase(String keyword);
    List<Anime> findByReleaseYearGreaterThan(int year);
}
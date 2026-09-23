package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.Progress;

import java.util.Optional;

public interface ProgressRepository
        extends JpaRepository<Progress, Long> {

    Optional<Progress> findByLearner_UserId(Long userId);
}
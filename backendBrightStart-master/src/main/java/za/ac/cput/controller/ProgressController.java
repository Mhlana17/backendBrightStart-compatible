package za.ac.cput.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Progress;
import za.ac.cput.domain.User;
import za.ac.cput.repository.ProgressRepository;
import za.ac.cput.repository.UserRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;

    public ProgressController(
            ProgressRepository progressRepository,
            UserRepository userRepository
    ) {
        this.progressRepository =
                progressRepository;

        this.userRepository =
                userRepository;
    }

    @GetMapping("/{learnerId}")
    public ResponseEntity<?> getProgress(
            @PathVariable Long learnerId
    ) {

        User learner =
                userRepository.findById(learnerId)
                        .orElse(null);

        if (learner == null) {
            return ResponseEntity.notFound().build();
        }

        Progress progress =
                progressRepository
                        .findByLearner_UserId(
                                learnerId
                        )
                        .orElseGet(
                                () -> new Progress(learner)
                        );

        return ResponseEntity.ok(
                toResponse(
                        learner,
                        progress
                )
        );
    }

    private Map<String, Object> toResponse(
            User learner,
            Progress progress
    ) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "learnerId",
                learner.getUserId()
        );

        response.put(
                "learnerName",
                learner.getFirstName()
                        + " "
                        + learner.getLastName()
        );

        response.put(
                "grade",
                "Registered Learner"
        );

        response.put(
                "overallProgress",
                progress.getOverallProgress()
        );

        response.put(
                "status",
                progress.getStatus() == null
                        ? "Not started"
                        : progress.getStatus()
        );

        response.put(
                "skills",
                List.of(
                        Map.of(
                                "name",
                                "Reading",
                                "color",
                                "green-bar",
                                "value",
                                progress.getReading()
                        ),

                        Map.of(
                                "name",
                                "Writing",
                                "color",
                                "blue-bar",
                                "value",
                                progress.getWriting()
                        ),

                        Map.of(
                                "name",
                                "Comprehension",
                                "color",
                                "yellow-bar",
                                "value",
                                progress.getComprehension()
                        ),

                        Map.of(
                                "name",
                                "Speaking",
                                "color",
                                "purple-bar",
                                "value",
                                progress.getSpeaking()
                        )
                )
        );

        response.put(
                "comment",
                progress.getComment() == null
                        ? "No tutor comment yet."
                        : progress.getComment()
        );

        response.put(
                "tutor",
                progress.getTutor() == null
                        ? "BrightStart"
                        : progress.getTutor()
        );

        response.put(
                "commentDate",
                progress.getCommentDate() == null
                        ? ""
                        : progress
                        .getCommentDate()
                        .toString()
        );

        return response;
    }
}
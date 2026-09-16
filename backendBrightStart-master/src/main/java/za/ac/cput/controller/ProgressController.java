package za.ac.cput.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {
    @GetMapping("/{learnerId}")
    public Map<String, Object> getProgress(@PathVariable Long learnerId) {
        return Map.of(
            "learnerId", learnerId,
            "learnerName", "Lerato Mongameli",
            "grade", "Grade 2",
            "overallProgress", 65,
            "status", "Improving",
            "skills", List.of(
                Map.of("name", "Reading", "color", "green-bar", "value", 70),
                Map.of("name", "Writing", "color", "blue-bar", "value", 60),
                Map.of("name", "Comprehension", "color", "yellow-bar", "value", 65),
                Map.of("name", "Speaking", "color", "purple-bar", "value", 75)
            ),
            "comment", "Lerato is showing great improvement in reading short stories. Keep practicing at home!",
            "tutor", "Teacher Amanda",
            "commentDate", "20 May 2026"
        );
    }
}

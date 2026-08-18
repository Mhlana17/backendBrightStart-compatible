package za.ac.cput.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/programs")
@CrossOrigin(origins = "http://localhost:5173")
public class ProgramController {
    @GetMapping
    public List<Map<String, Object>> getPrograms() {
        return List.of(
            Map.of("title", "Reading Lessons", "color", "green", "price", 150, "body", "Phonics, word recognition, reading fluency and comprehension."),
            Map.of("title", "Writing Skills", "color", "yellow", "price", 150, "body", "Sentence building, handwriting, spelling and creative writing."),
            Map.of("title", "Homework Help", "color", "purple", "price", 150, "body", "Assistance with school homework and class activities."),
            Map.of("title", "Speaking Practice", "color", "blue", "price", 150, "body", "Improve pronunciation, confidence and everyday communication.")
        );
    }
}

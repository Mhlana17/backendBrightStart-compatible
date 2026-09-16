package za.ac.cput.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "application", "BrightStart API",
                "status", "running",
                "message", "BrightStart backend is running successfully."
        );
    }
}
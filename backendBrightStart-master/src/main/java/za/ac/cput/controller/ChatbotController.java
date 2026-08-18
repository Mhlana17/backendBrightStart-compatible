package za.ac.cput.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import za.ac.cput.service.ChatbotService;
import za.ac.cput.service.ChatbotRateLimitService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;
    private final ChatbotRateLimitService rateLimitService;

    public ChatbotController(ChatbotService chatbotService, ChatbotRateLimitService rateLimitService) {
        this.chatbotService = chatbotService;
        this.rateLimitService = rateLimitService;
    }

    @PostMapping("/ask")
    public ResponseEntity<ChatbotService.ChatResponse> ask(@RequestBody ChatRequest request, HttpServletRequest httpRequest) {
        if (!rateLimitService.allowRequest(resolveClientKey(httpRequest))) {
            return ResponseEntity.status(429).build();
        }

        String message = request != null ? request.getMessage() : "";
        List<ChatbotService.ConversationTurn> history = request != null ? request.getHistory() : List.of();
        ChatbotService.ChatResponse response = chatbotService.generateResponse(message, history);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/stream", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> stream(@RequestBody ChatRequest request, HttpServletRequest httpRequest) {
        if (!rateLimitService.allowRequest(resolveClientKey(httpRequest))) {
            return ResponseEntity.status(429).build();
        }

        String message = request != null ? request.getMessage() : "";
        List<ChatbotService.ConversationTurn> history = request != null ? request.getHistory() : List.of();

        SseEmitter emitter = new SseEmitter(120_000L);
        CompletableFuture.runAsync(() -> {
            try {
                chatbotService.streamResponse(message, history, new ChatbotService.StreamListener() {
                    @Override
                    public void onMeta(String provider, List<ChatbotService.ProductSuggestion> suggestions) {
                        try {
                            Map<String, Object> meta = new HashMap<>();
                            meta.put("provider", provider);
                            meta.put("suggestions", suggestions);
                            emitter.send(SseEmitter.event().name("meta").data(meta));
                        } catch (Exception e) {
                            emitter.completeWithError(e);
                        }
                    }

                    @Override
                    public void onToken(String token) {
                        try {
                            emitter.send(SseEmitter.event().name("chunk").data(token));
                        } catch (Exception e) {
                            emitter.completeWithError(e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        try {
                            emitter.send(SseEmitter.event().name("done").data("[DONE]"));
                        } catch (Exception ignored) {
                            // ignore final send errors
                        }
                        emitter.complete();
                    }
                });
            } catch (Exception e) {
                try {
                    emitter.send(SseEmitter.event().name("error").data("Streaming failed"));
                } catch (Exception ignored) {
                    // ignore nested errors
                }
                emitter.completeWithError(e);
            }
        });

        return ResponseEntity.ok(emitter);
    }

    public static class ChatRequest {
        private String message;
        private List<ChatbotService.ConversationTurn> history = new ArrayList<>();

        public ChatRequest() {
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<ChatbotService.ConversationTurn> getHistory() {
            return history;
        }

        public void setHistory(List<ChatbotService.ConversationTurn> history) {
            this.history = history;
        }
    }

    private String resolveClientKey(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}

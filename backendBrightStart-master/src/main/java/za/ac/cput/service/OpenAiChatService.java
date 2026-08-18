package za.ac.cput.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class OpenAiChatService {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    @Value("${openai.enabled:true}")
    private boolean openAiEnabled;

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;

    public OpenAiChatService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String generateReply(String userMessage,
                                List<ChatbotService.ConversationTurn> history,
                                List<ChatbotService.ProductSuggestion> productContext) {
        if (!openAiEnabled || apiKey == null || apiKey.isBlank()) {
            return null;
        }

        try {
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of(
                    "role", "system",
                    "content", buildSystemPrompt()
            ));

            for (ChatbotService.ConversationTurn turn : sanitizeHistory(history)) {
                messages.add(Map.of(
                        "role", turn.getRole(),
                        "content", turn.getContent()
                ));
            }

            String userPrompt = buildUserPrompt(userMessage, productContext);
            messages.add(Map.of("role", "user", "content", userPrompt));

            Map<String, Object> payload = new HashMap<>();
            payload.put("model", model);
            payload.put("temperature", 0.5);
            payload.put("max_tokens", 350);
            payload.put("messages", messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .timeout(Duration.ofSeconds(40))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return null;
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode contentNode = root.path("choices").path(0).path("message").path("content");
            if (contentNode.isTextual() && !contentNode.asText().isBlank()) {
                return contentNode.asText().trim();
            }
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (IOException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean streamReply(String userMessage,
                               List<ChatbotService.ConversationTurn> history,
                               List<ChatbotService.ProductSuggestion> productContext,
                               Consumer<String> tokenConsumer) {
        if (!openAiEnabled || apiKey == null || apiKey.isBlank()) {
            return false;
        }

        try {
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of(
                    "role", "system",
                    "content", buildSystemPrompt()
            ));

            for (ChatbotService.ConversationTurn turn : sanitizeHistory(history)) {
                messages.add(Map.of(
                        "role", turn.getRole(),
                        "content", turn.getContent()
                ));
            }

            String userPrompt = buildUserPrompt(userMessage, productContext);
            messages.add(Map.of("role", "user", "content", userPrompt));

            Map<String, Object> payload = new HashMap<>();
            payload.put("model", model);
            payload.put("temperature", 0.5);
            payload.put("max_tokens", 350);
            payload.put("stream", true);
            payload.put("messages", messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .timeout(Duration.ofSeconds(60))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)))
                    .build();

            HttpResponse<java.io.InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return false;
            }

            boolean hasStreamedAnyToken = false;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (!trimmed.startsWith("data:")) {
                        continue;
                    }

                    String data = trimmed.substring(5).trim();
                    if ("[DONE]".equals(data)) {
                        break;
                    }

                    JsonNode root = objectMapper.readTree(data);
                    JsonNode deltaContent = root.path("choices").path(0).path("delta").path("content");
                    if (deltaContent.isTextual()) {
                        String token = deltaContent.asText();
                        if (!token.isEmpty()) {
                            tokenConsumer.accept(token);
                            hasStreamedAnyToken = true;
                        }
                    }
                }
            }
            return hasStreamedAnyToken;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private String buildSystemPrompt() {
        return "You are AniBot, a customer assistant for AnimeStore. " +
                "Be concise, practical, and friendly. " +
                "Use only store context provided. " +
                "If unsure, say you are unsure and suggest browsing catalog or contacting support. " +
                "Do not invent order status, stock, pricing, shipping rules, or policies.";
    }

    private String buildUserPrompt(String userMessage, List<ChatbotService.ProductSuggestion> productContext) {
        StringBuilder builder = new StringBuilder();
        builder.append("Customer message: ").append(userMessage).append("\n");
        builder.append("Available store product context:\n");
        if (productContext == null || productContext.isEmpty()) {
            builder.append("- No matching products found in current context.\n");
        } else {
            for (ChatbotService.ProductSuggestion product : productContext) {
                builder.append("- ")
                        .append(product.getName())
                        .append(" | Category: ").append(product.getCategory() == null ? "N/A" : product.getCategory())
                        .append(" | Price: R ").append(String.format("%.2f", product.getPrice()))
                        .append(" | Stock: ").append(product.getStock())
                        .append("\n");
            }
        }
        return builder.toString();
    }

    private List<ChatbotService.ConversationTurn> sanitizeHistory(List<ChatbotService.ConversationTurn> history) {
        if (history == null || history.isEmpty()) {
            return List.of();
        }

        List<ChatbotService.ConversationTurn> sanitized = new ArrayList<>();
        int start = Math.max(0, history.size() - 8);
        for (int i = start; i < history.size(); i++) {
            ChatbotService.ConversationTurn turn = history.get(i);
            if (turn == null || turn.getContent() == null || turn.getContent().isBlank()) {
                continue;
            }
            String role = "assistant".equalsIgnoreCase(turn.getRole()) ? "assistant" : "user";
            String content = turn.getContent().trim();
            if (content.length() > 500) {
                content = content.substring(0, 500);
            }
            sanitized.add(new ChatbotService.ConversationTurn(role, content));
        }
        return sanitized;
    }
}

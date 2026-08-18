package za.ac.cput.service;

import org.springframework.stereotype.Service;
import za.ac.cput.domain.Product;
import za.ac.cput.repository.ProductRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ChatbotService {

    private final ProductRepository productRepository;
    private final OpenAiChatService openAiChatService;

    private static final Set<String> STOP_WORDS = Set.of(
            "the", "a", "an", "and", "or", "i", "me", "my", "you", "your",
            "need", "want", "for", "with", "to", "of", "in", "on", "at",
            "is", "are", "do", "have", "show", "find", "please", "can"
    );

    public ChatbotService(ProductRepository productRepository, OpenAiChatService openAiChatService) {
        this.productRepository = productRepository;
        this.openAiChatService = openAiChatService;
    }

    public ChatResponse generateResponse(String message, List<ConversationTurn> history) {
        String normalized = message == null ? "" : message.trim();
        String lower = normalized.toLowerCase(Locale.ROOT);

        if (normalized.isEmpty()) {
            return new ChatResponse(
                    "Hi, I can help you find products, check stock, and answer common store questions.",
                    topProductSuggestions(3),
                    "fallback"
            );
        }

        List<ProductSuggestion> matchedProducts = findProductsByMessage(lower, 5);
        List<ProductSuggestion> contextProducts = matchedProducts.isEmpty()
                ? topProductSuggestions(5)
                : matchedProducts;

        String llmAnswer = openAiChatService.generateReply(normalized, history, contextProducts);
        if (llmAnswer != null && !llmAnswer.isBlank()) {
            return new ChatResponse(
                    llmAnswer,
                    contextProducts.stream().limit(5).collect(Collectors.toList()),
                    "openai"
            );
        }

        return generateFallbackResponse(normalized, lower, matchedProducts);
    }

    public void streamResponse(String message, List<ConversationTurn> history, StreamListener listener) {
        String normalized = message == null ? "" : message.trim();
        String lower = normalized.toLowerCase(Locale.ROOT);

        if (normalized.isEmpty()) {
            ChatResponse fallback = new ChatResponse(
                    "Hi, I can help you find products, check stock, and answer common store questions.",
                    topProductSuggestions(3),
                    "fallback"
            );
            listener.onMeta(fallback.getProvider(), fallback.getSuggestions());
            streamText(fallback.getAnswer(), listener);
            listener.onComplete();
            return;
        }

        List<ProductSuggestion> matchedProducts = findProductsByMessage(lower, 5);
        List<ProductSuggestion> contextProducts = matchedProducts.isEmpty()
                ? topProductSuggestions(5)
                : matchedProducts;

        listener.onMeta("openai", contextProducts.stream().limit(5).collect(Collectors.toList()));
        boolean streamed = openAiChatService.streamReply(normalized, history, contextProducts, listener::onToken);
        if (streamed) {
            listener.onComplete();
            return;
        }

        ChatResponse fallback = generateFallbackResponse(normalized, lower, matchedProducts);
        listener.onMeta(fallback.getProvider(), fallback.getSuggestions());
        streamText(fallback.getAnswer(), listener);
        listener.onComplete();
    }

    private ChatResponse generateFallbackResponse(String normalized, String lower, List<ProductSuggestion> matchedProducts) {
        if (containsAny(lower, "hello", "hi", "hey")) {
            return new ChatResponse(
                    "Hey! Ask me for recommendations, product stock, delivery info, or payment support.",
                    topProductSuggestions(3),
                    "fallback"
            );
        }

        if (containsAny(lower, "shipping", "delivery")) {
            return new ChatResponse(
                    "We deliver across South Africa. Final shipping details and charges are shown during checkout.",
                    List.of(),
                    "fallback"
            );
        }

        if (containsAny(lower, "payment", "pay", "card", "checkout")) {
            return new ChatResponse(
                    "You can complete payment during checkout after signing in. If checkout fails, retry or contact support.",
                    List.of(),
                    "fallback"
            );
        }

        if (containsAny(lower, "refund", "return", "exchange")) {
            return new ChatResponse(
                    "For returns or refunds, please contact support with your order number so the team can assist quickly.",
                    List.of(),
                    "fallback"
            );
        }

        if (containsAny(lower, "recommend", "suggest", "best", "popular")) {
            List<ProductSuggestion> suggestions = topProductSuggestions(5);
            String reply = suggestions.isEmpty()
                    ? "I could not find available products right now."
                    : "Here are some available picks you can browse right now.";
            return new ChatResponse(reply, suggestions, "fallback");
        }

        if (!matchedProducts.isEmpty()) {
            return new ChatResponse("I found these matching products for you.", matchedProducts, "fallback");
        }

        return new ChatResponse(
                "I could not find an exact match. Try asking for hoodies, tees, caps, beanies, or say 'recommend products'.",
                topProductSuggestions(3),
                "fallback"
        );
    }

    private void streamText(String text, StreamListener listener) {
        if (text == null || text.isBlank()) {
            return;
        }
        Matcher matcher = Pattern.compile("\\S+\\s*").matcher(text);
        while (matcher.find()) {
            listener.onToken(matcher.group());
        }
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private List<ProductSuggestion> findProductsByMessage(String message, int limit) {
        Set<String> tokens = tokenize(message);
        if (tokens.isEmpty()) {
            return List.of();
        }

        return productRepository.findByStockGreaterThan(0).stream()
                .filter(product -> {
                    String name = product.getName() == null
                            ? ""
                            : product.getName().toLowerCase(Locale.ROOT);
                    String category = product.getCategory() != null && product.getCategory().getCategoryName() != null
                            ? product.getCategory().getCategoryName().toLowerCase(Locale.ROOT)
                            : "";
                    return tokens.stream().anyMatch(token -> name.contains(token) || category.contains(token));
                })
                .sorted(Comparator.comparingInt(Product::getStock).reversed())
                .limit(limit)
                .map(this::toSuggestion)
                .collect(Collectors.toList());
    }

    private Set<String> tokenize(String message) {
        String[] parts = message.split("[^a-z0-9]+");
        Set<String> tokens = new HashSet<>();
        for (String part : parts) {
            if (part.length() >= 3 && !STOP_WORDS.contains(part)) {
                tokens.add(part);
            }
        }
        return tokens;
    }

    private List<ProductSuggestion> topProductSuggestions(int limit) {
        return productRepository.findByStockGreaterThan(0).stream()
                .sorted(Comparator.comparingInt(Product::getStock).reversed())
                .limit(limit)
                .map(this::toSuggestion)
                .collect(Collectors.toList());
    }

    private ProductSuggestion toSuggestion(Product product) {
        String category = product.getCategory() != null ? product.getCategory().getCategoryName() : null;
        return new ProductSuggestion(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                category
        );
    }

    public static class ChatResponse {
        private String answer;
        private List<ProductSuggestion> suggestions = new ArrayList<>();
        private String provider;

        public ChatResponse() {
        }

        public ChatResponse(String answer, List<ProductSuggestion> suggestions, String provider) {
            this.answer = answer;
            this.suggestions = suggestions;
            this.provider = provider;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public List<ProductSuggestion> getSuggestions() {
            return suggestions;
        }

        public void setSuggestions(List<ProductSuggestion> suggestions) {
            this.suggestions = suggestions;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }
    }

    public static class ProductSuggestion {
        private Long productId;
        private String name;
        private double price;
        private int stock;
        private String category;

        public ProductSuggestion() {
        }

        public ProductSuggestion(Long productId, String name, double price, int stock, String category) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.category = category;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    public static class ConversationTurn {
        private String role;
        private String content;

        public ConversationTurn() {
        }

        public ConversationTurn(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public interface StreamListener {
        void onMeta(String provider, List<ProductSuggestion> suggestions);

        void onToken(String token);

        void onComplete();
    }
}

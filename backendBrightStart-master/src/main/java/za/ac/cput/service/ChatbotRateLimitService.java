package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatbotRateLimitService {

    @Value("${chatbot.rate-limit.requests:20}")
    private int maxRequests;

    @Value("${chatbot.rate-limit.window-seconds:60}")
    private int windowSeconds;

    private final Map<String, Deque<Long>> requestsByClient = new ConcurrentHashMap<>();

    public boolean allowRequest(String clientKey) {
        long now = System.currentTimeMillis();
        long cutoff = now - (windowSeconds * 1000L);

        Deque<Long> timestamps = requestsByClient.computeIfAbsent(clientKey, key -> new ArrayDeque<>());
        synchronized (timestamps) {
            while (!timestamps.isEmpty() && timestamps.peekFirst() < cutoff) {
                timestamps.pollFirst();
            }
            if (timestamps.size() >= maxRequests) {
                return false;
            }
            timestamps.addLast(now);
            return true;
        }
    }
}

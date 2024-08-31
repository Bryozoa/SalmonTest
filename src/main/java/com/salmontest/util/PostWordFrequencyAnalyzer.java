package com.salmontest.util;

import com.salmontest.model.Post;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostWordFrequencyAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(PostWordFrequencyAnalyzer.class);

    public void analyze() {
        try {
            Response response = RestAssured.get("https://jsonplaceholder.typicode.com/posts");
            if (response.getStatusCode() != 200) {
                throw new RuntimeException("Failed to fetch posts: HTTP error code " + response.getStatusCode());
            }

            List<Post> posts = response.jsonPath().getList("", Post.class);
            if (posts == null || posts.isEmpty()) {
                throw new RuntimeException("No posts available for analysis");
            }

            analyzeWordFrequency(posts);
        } catch (Exception e) {
            logger.error("Error occurred while analyzing post word frequencies: {}", e.getMessage(), e);
            System.out.println("An error occurred. Please try again later.");
        }
    }

    private void analyzeWordFrequency(List<Post> posts) {
        Map<String, Integer> wordFrequencyMap = new HashMap<>();

        for (Post post : posts) {
            if (post.getBody() == null || post.getBody().trim().isEmpty()) {
                logger.warn("Skipping post with empty body: {}", post.getId());
                continue;
            }

            String[] words = post.getBody().toLowerCase().split("\\W+");
            for (String word : words) {
                wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> sortedWords = wordFrequencyMap.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(10)
                .toList();

        for (int i = 0; i < sortedWords.size(); i++) {
            Map.Entry<String, Integer> entry = sortedWords.get(i);
            System.out.println((i + 1) + ". " + entry.getKey() + " - " + entry.getValue());
        }
    }
}
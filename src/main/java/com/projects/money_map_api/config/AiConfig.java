package com.projects.money_map_api.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AiConfig {

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.base.url}")
    private String baseUrl;

    @Value("${openrouter.model.name}")
    private String modelName;

    @Bean
    public ChatModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .timeout(Duration.ofSeconds(60))
                .logRequests(true) // Set to true to see the OpenRouter communication in logs
                .logResponses(true)
                // OpenRouter specific headers (Optional but recommended)
                .customHeaders(java.util.Map.of(
                        "HTTP-Referer", "http://localhost:8080",
                        "X-Title", "MoneyMap API"
                ))
                .build();
    }
}
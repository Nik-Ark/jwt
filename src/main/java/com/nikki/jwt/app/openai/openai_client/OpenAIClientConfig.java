package com.nikki.jwt.app.openai.openai_client;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.Retryer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Indexed;

@Configuration
@ConfigurationProperties
@Indexed
@Data
@Slf4j
public class OpenAIClientConfig {

    @Value("${openai.config.http-client.read-timeout}")
    private long readTimeout;

    @Value("${openai.config.http-client.connect-timeout}")
    private long connectTimeout;

    @Value("${openai.config.api-key}")
    private String apiKey;

    @Value("${openai.config.gpt-model}")
    private String model;

    @Value("${openai.config.audio-model}")
    private String audioModel;

    @Bean
    public Request.Options options() {
        return new Request.Options();
    }

    @Bean
    public Logger.Level feignLogger() {
        return Logger.Level.FULL;
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default();
    }

    @Bean
    public RequestInterceptor apiKeyInterceptor() {
        return request -> request.header("Authorization", "Bearer " + apiKey);
    }
}

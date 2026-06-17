package com.example.springailearning.chatclient2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.ai")
public record AiProviderProperties(

    String provider,

    String model,

    String profile

) {

}

package com.example.springailearning.chatclient2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticPageConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/chat")
            .setViewName("forward:/chat/index.html");
        registry.addViewController("/chat/")
            .setViewName("forward:/chat/index.html");
    }
}

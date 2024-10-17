package com.cms.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebClientConfig 类用于配置 WebClient Bean
 *
 * @author Seamher
 */
@Configuration
public class WebClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebClientConfig.class);

    /**
     * 创建并配置一个 WebClient Bean
     *
     * @return 配置完成的 WebClient 实例
     */
    @Bean
    public WebClient webClient() {
        logger.info("Creating WebClient bean");
        WebClient webClient = WebClient.builder().build();
        logger.info("WebClient bean created successfully");
        return webClient;
    }

}

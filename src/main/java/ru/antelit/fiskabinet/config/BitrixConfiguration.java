package ru.antelit.fiskabinet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import ru.antelit.fiskabinet.api.bitrix.Bitrix24;

@Configuration
@PropertySource("classpath:application-dev.yaml")
public class BitrixConfiguration {

    @Value("${bitrix24.domain}")
    private String domain;

    @Value("${bitrix24.country}")
    private String country = "ru";

    @Value("${bitrix24.user-id}")
    private String userId;

    @Value("${bitrix24.token}")
    private String apiKey;

    @Bean
    @Primary
    public Bitrix24 bitrix24() {
        return Bitrix24.builder()
                .country(country)
                .domain(domain)
                .userId(userId)
                .apiKey(apiKey)
                .build();
    }
}

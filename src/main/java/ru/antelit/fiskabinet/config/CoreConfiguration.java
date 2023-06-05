package ru.antelit.fiskabinet.config;

import org.apache.tomcat.util.threads.ScheduledThreadPoolExecutor;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import ru.antelit.fiskabinet.api.Bitrix24;

import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.ExecutorService;

@Configuration
@PropertySource("classpath:application-dev.yaml")
@Profile("dev")
public class CoreConfiguration {

    @Value("${bitrix24.domain}")
    private String domain;

    @Value("${bitrix24.country}")
    private String country = "ru";

    @Value("${bitrix24.user-id}")
    private String userId;

    @Value("${bitrix24.token}")
    private String apiKey;

    @Bean
    public JerseyClient jerseyClient() {
        JerseyClient client;
        ClientBuilder builder = JerseyClientBuilder.newBuilder();


        return null;
    }
    @Bean
    public Bitrix24 bitrix24() {

        return Bitrix24.builder()
                .country(country)
                .domain(domain)
                .userId(userId)
                .apiKey(apiKey)
                .build();
    }

}

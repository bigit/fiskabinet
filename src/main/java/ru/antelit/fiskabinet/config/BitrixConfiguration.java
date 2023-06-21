package ru.antelit.fiskabinet.config;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import ru.antelit.fiskabinet.api.bitrix.Bitrix24;


import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@PropertySource("classpath:application-dev.yaml")
@Profile("dev")
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
    public JerseyClient jerseyClient() {
        ClientBuilder builder = JerseyClientBuilder.newBuilder();

        ClientConfig config = new ClientConfig();
        config.register(ObjectMapperProvider.class);
        builder.withConfig(config);

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        builder.scheduledExecutorService(service);

        return (JerseyClient) builder.build();
    }

    @Bean
    public Bitrix24 bitrix24() {
//        return new Bitrix24("https://65df5531-c8e0-4ffd-981e-6a0246750b63.mock.pstmn.io", "21");
        return Bitrix24.builder()
                .country(country)
                .domain(domain)
                .userId(userId)
                .apiKey(apiKey)
                .build();
    }
}

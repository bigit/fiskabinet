package ru.antelit.fiskabinet;

import jakarta.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import ru.antelit.fiskabinet.api.bitrix.Bitrix24;
import ru.antelit.fiskabinet.api.bitrix.json.ObjectMapperProvider;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@TestConfiguration
@Profile({"test"})
@TestPropertySource("classpath:application.yaml")
public class FiskabinetTestConfiguration {

    @Value("${bitrix.domain}")
    private String domain;

    @Value("${bitrix.country}")
    private String country;

    @Value("${bitrix.user-id}")
    private String userId;

    @Value("${bitrix.apiKey}")
    private String apiKey;

    @Bean(name = "bitrix.post")
    public Bitrix24 bitrix24() {
        return new Bitrix24("https://65df5531-c8e0-4ffd-981e-6a0246750b63.mock.pstmn.io", "21", null, null);
    }

    @Bean(name = "bitrix.test")
    @Primary
    public Bitrix24 bitrix24Prod() {
        return  Bitrix24.builder()
                .domain(domain)
                .country(country)
                .userId(userId)
                .apiKey(apiKey)
                .build();
    }

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


}

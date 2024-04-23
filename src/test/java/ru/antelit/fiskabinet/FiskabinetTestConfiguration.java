package ru.antelit.fiskabinet;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import ru.antelit.fiskabinet.api.bitrix.Bitrix24;
import ru.antelit.fiskabinet.api.bitrix.json.ObjectMapperProvider;

import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@TestConfiguration
//@Import({BitrixConfiguration.class})
@Profile({"test"})
@TestPropertySource("classpath:application.yaml")
public class FiskabinetTestConfiguration {

    @Bean(name = "bitrix.post")
    public Bitrix24 bitrix24() {
        return new Bitrix24("https://65df5531-c8e0-4ffd-981e-6a0246750b63.mock.pstmn.io", "21", null, null);
    }

    @Bean(name = "bitrix.test")
    @Primary
    public Bitrix24 bitrix24Prod() {
        return  Bitrix24.builder()
                .country("ru")
                .domain("antelplus")
                .apiKey("b38vyanz2vyjj67m")
                .userId("21")
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

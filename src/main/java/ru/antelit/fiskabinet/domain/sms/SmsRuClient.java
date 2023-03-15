package ru.antelit.fiskabinet.domain.sms;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.springframework.stereotype.Component;
import ru.antelit.fiskabinet.domain.sms.smsru.SmsRuResponse;

@Component
public class SmsRuClient implements SmsApiClient {

    public static final String BASE_URL = "https://sms.ru/sms/send";
    public static final String API_ID = "833E7A39-BEB9-8869-8F5A-09BAF8E22851 ";

    @Override
    public void send(String recipient, String message) {
        JerseyClient client = JerseyClientBuilder.createClient();

        SmsRuResponse response = client.target(BASE_URL)
                .queryParam("api_id", API_ID)
                .queryParam("to", recipient)
                .queryParam("msg", message)
                .queryParam("json", 1)
                .request()
                .buildGet()
                .invoke(SmsRuResponse.class);

    }
}

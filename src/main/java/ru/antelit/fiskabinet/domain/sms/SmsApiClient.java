package ru.antelit.fiskabinet.domain.sms;

public interface SmsApiClient {
    public void send(String recipient, String message);
}

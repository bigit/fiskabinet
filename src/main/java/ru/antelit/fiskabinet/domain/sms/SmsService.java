package ru.antelit.fiskabinet.domain.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private final SmsApiClient smsApiClient;

    public SmsService(SmsApiClient smsApiClient) {
        this.smsApiClient = smsApiClient;
    }

    public void sendSms(String message, String recipient) {

    }
}

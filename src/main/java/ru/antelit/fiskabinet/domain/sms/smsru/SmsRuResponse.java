package ru.antelit.fiskabinet.domain.sms.smsru;

import lombok.Data;

import java.util.Map;

@Data
public class SmsRuResponse {
    private String status;
    private String statusCode;
    private Map<String, SmsResult> sms;

}

class SmsResult {
    String status;
    Integer statusCode;
    String smsId;
}

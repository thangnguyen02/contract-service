package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.config.TwilioConfig;
import com.fpt.servicecontract.contract.service.SmsService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {
    private final TwilioConfig twilioConfig;
//    private static final String API_URL = "https://y3n2v9.api.infobip.com/sms/2/text/advanced";
//    private static final String AUTH_TOKEN = "2a75342510d4104086d2f665475327f9-dfe40162-1436-43fa-9218-3c6d9aa56aa0";
//    private final RestTemplate restTemplate;
//
//    @Override
//    public void sendSms(String to, String text) {
//        to = "84" + to.substring(0);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "App " + AUTH_TOKEN);
//        headers.set("Content-Type", "application/json");
//        headers.set("Accept", "application/json");
//
//        Map<String, Object> message = new HashMap<>();
//        message.put("from", "ServiceSMS");
//        message.put("text", text);
//        Map<String, Object> destination = new HashMap<>();
//        destination.put("to", to);
//        message.put("destinations", new Map[]{destination});
//        Map<String, Object> body = new HashMap<>();
//        body.put("messages", new Map[]{message});
//
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//        restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, String.class);
//
//    }

    @Override
    public void sendSms(String to, String messageBody) {
        Message message = Message.creator(
                        new PhoneNumber(to),
                        new PhoneNumber(twilioConfig.getPhoneNumber()),
                        messageBody)
                .create();

        System.out.println("SMS sent with SID: " + message.getSid());
    }
}

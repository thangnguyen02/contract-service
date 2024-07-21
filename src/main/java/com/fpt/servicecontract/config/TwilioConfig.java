package com.fpt.servicecontract.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Getter
    @Value("${twilio.phone-number}")
    private String phoneNumber;

    @PostConstruct
    public void initializeTwilio() {
        Twilio.init(accountSid, authToken);
    }

}

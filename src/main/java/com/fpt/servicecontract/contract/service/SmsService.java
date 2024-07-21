package com.fpt.servicecontract.contract.service;

public interface SmsService {
    void sendSms(String to, String messageBody);
}

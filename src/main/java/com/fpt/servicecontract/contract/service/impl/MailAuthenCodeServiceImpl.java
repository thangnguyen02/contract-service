package com.fpt.servicecontract.contract.service.impl;


import com.fpt.servicecontract.auth.dto.UserDto;
import com.fpt.servicecontract.config.MailService;
import com.fpt.servicecontract.contract.model.Party;
import com.fpt.servicecontract.contract.model.AuthenticationCode;
import com.fpt.servicecontract.contract.repository.ContractAppendicesRepository;
import com.fpt.servicecontract.contract.repository.PartyRepository;
import com.fpt.servicecontract.contract.repository.MailAuthenCodeRepository;
import com.fpt.servicecontract.contract.service.MailAuthenCodeService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailAuthenCodeServiceImpl implements MailAuthenCodeService {

    private final MailAuthenCodeRepository mailAuthenCodeRepository;
    private final PartyRepository partyRepository;
    private final MailService mailService;
    private final ContractAppendicesRepository contractAppendicesRepository;

    public BaseResponse GetAuthenMailCode(String email, String contractId, String type) {
        if ("appendices".equals(type)) {
            var appendices = contractAppendicesRepository.findById(contractId);
            if (appendices.isEmpty()) {
                return new BaseResponse(Constants.ResponseCode.FAILURE, "Appendices not existed", true, null);
            }

            contractId = appendices.get().getContractId();
        }
        int user = partyRepository.checkMailContractParty(contractId, email);
        String[] emailList = new String[]{email};
        if (user == 0) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "user not found", true, null);
        }
        int code = new Random().nextInt(999999);
        var mailCode = mailAuthenCodeRepository.findByEmail(email);
        if (mailCode.isEmpty()) {
            LocalDateTime startTime = LocalDateTime.now();
            AuthenticationCode mailAuthedCode = AuthenticationCode.builder()
                    .email(email)
                    .code(code)
                    .expiryTime(startTime.plusMinutes(5))
                    .startTime(startTime)
                    .build();
            mailAuthenCodeRepository.save(mailAuthedCode);
        } else {
            mailCode.get().setCode(code);
            LocalDateTime startTime = LocalDateTime.now();
            LocalDateTime expiryTime = startTime.plusMinutes(5);
            mailCode.get().setStartTime(startTime);
            mailCode.get().setExpiryTime(expiryTime);
            mailAuthenCodeRepository.save(mailCode.get());
        }

        try {
            mailService.sendNewMail(emailList, null, "OTP CODE", "<h1>" + code + "</h1>", null);
        } catch (MessagingException e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, e.getMessage(), false, null);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "found user", true, null);
    }

    @Override
    public BaseResponse GetSmsCode(String sms) {
        int code = new Random().nextInt(999999);
        var mailCode = mailAuthenCodeRepository.findByEmail(sms);
        if (mailCode.isEmpty()) {
            LocalDateTime startTime = LocalDateTime.now();
            AuthenticationCode mailAuthedCode = AuthenticationCode.builder()
                    .email(sms)
                    .code(code)
                    .expiryTime(startTime.plusMinutes(5))
                    .startTime(startTime)
                    .build();
            mailAuthenCodeRepository.save(mailAuthedCode);
        } else {
            mailCode.get().setCode(code);
            LocalDateTime startTime = LocalDateTime.now();
            LocalDateTime expiryTime = startTime.plusMinutes(5);
            mailCode.get().setStartTime(startTime);
            mailCode.get().setExpiryTime(expiryTime);
            mailAuthenCodeRepository.save(mailCode.get());
        }
        sendSms(sms, String.valueOf(code));
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, null);
    }

    @Override
    public BaseResponse AuthenticationMailWithCode(String email, Integer AuthenCode) {
        var contractPartyObject = partyRepository.findByEmail(email);
        var mailAuthedCode = mailAuthenCodeRepository.findByEmail(email);

        if (mailAuthedCode.isEmpty() || contractPartyObject.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "User not exist", true, null);
        }

        if (!Objects.equals(mailAuthedCode.get().getCode(), AuthenCode)) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Your code is invalid", true, null);
        }
        if ((mailAuthedCode.get().getExpiryTime().isBefore(LocalDateTime.now()))) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Your code is expired", true, null);
        }
        Party party = contractPartyObject.get();
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "found user", true, UserDto.builder()
                .id(party.getId())
                .email(party.getEmail())
                .build());
    }
    @Override
    public BaseResponse AuthenticationSmsWithCode(String phone, Integer AuthenCode) {
        var contractPartyObject = partyRepository.findByPhone(phone);
        var mailAuthedCode = mailAuthenCodeRepository.findByEmail(phone);

        if (mailAuthedCode.isEmpty() || contractPartyObject.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "User not exist", true, null);
        }

        if (!Objects.equals(mailAuthedCode.get().getCode(), AuthenCode)) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Your code is invalid", true, null);
        }
        if ((mailAuthedCode.get().getExpiryTime().isBefore(LocalDateTime.now()))) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Your code is expired", true, null);
        }
        Party party = contractPartyObject.get();
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "sms is verified", true, UserDto.builder()
                .id(party.getId())
                .phone(party.getPhone())
                .build());
    }
    private static final String API_URL = "https://n8ne9y.api.infobip.com/sms/2/text/advanced";
    private static final String AUTH_TOKEN = "f2bbbc00f61e3321cc002619c0a39745-2d42b647-c8d0-499f-9da0-a3a5eb411670";
    private final RestTemplate restTemplate;

    public void sendSms(String to, String text) {
        to = "84" + to.substring(0);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "App " + AUTH_TOKEN);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        Map<String, Object> message = new HashMap<>();
        message.put("from", "ServiceSMS");
        message.put("text", text + " là mã xác minh TdocMan của bạn");
        Map<String, Object> destination = new HashMap<>();
        destination.put("to", to);
        message.put("destinations", new Map[]{destination});
        Map<String, Object> body = new HashMap<>();
        body.put("messages", new Map[]{message});

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, String.class);

    }
}

package com.fpt.servicecontract.contract.service.impl;


import com.fpt.servicecontract.auth.dto.UserDto;
import com.fpt.servicecontract.auth.model.User;
import com.fpt.servicecontract.auth.repository.UserRepository;
import com.fpt.servicecontract.config.MailService;
import com.fpt.servicecontract.contract.model.MailAuthedCode;
import com.fpt.servicecontract.contract.repository.MailAuthenCodeRepository;
import com.fpt.servicecontract.contract.service.MailAuthenCodeService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
@Service
public class MailAuthenCodeServiceImpl implements MailAuthenCodeService {
    private MailAuthenCodeRepository mailAuthenCodeRepository;
    private UserRepository userRepository;
    private MailService mailService;

    public BaseResponse GetAuthenMailCode(String email) {
        var user = userRepository.findByEmail(email);
        String[] emailList = new String[]{email};
        if (user.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "user not found", true, null);
        }
        int code = new Random().nextInt(999999);
        var mailCode = mailAuthenCodeRepository.findByEmail(email);
        if (mailCode.isEmpty()) {
            LocalDateTime startTime = LocalDateTime.now();
            MailAuthedCode mailAuthedCode = MailAuthedCode.builder()
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
    public BaseResponse AuthenticationMailWithCode(String email, Integer AuthenCode) {
        var company = userRepository.findByEmail(email);
        var mailAuthedCode = mailAuthenCodeRepository.findByEmail(email);

        if(mailAuthedCode.isEmpty() || company.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "User not exist", true, null);
        }

        if(!Objects.equals(mailAuthedCode.get().getCode(), AuthenCode)) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Your code is invalid", true, null);
        }
        if((mailAuthedCode.get().getExpiryTime().isBefore(LocalDateTime.now()))) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Your code is expired", true, null);
        }
        User user = company.get();
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "found user", true, UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build());
    }
}

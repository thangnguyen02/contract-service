package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.utils.BaseResponse;

public interface MailAuthenCodeService {
    BaseResponse GetAuthenMailCode(String email,String contractId, String type);

    BaseResponse GetSmsCode(String sms);

    BaseResponse AuthenticationMailWithCode(String email, Integer code);

    BaseResponse AuthenticationSmsWithCode(String phone, Integer AuthenCode);
}

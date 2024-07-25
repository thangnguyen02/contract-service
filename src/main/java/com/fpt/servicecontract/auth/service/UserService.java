package com.fpt.servicecontract.auth.service;

import com.fpt.servicecontract.auth.dto.RegisterRequest;
import com.fpt.servicecontract.auth.dto.SearchUserRequest;
import com.fpt.servicecontract.auth.dto.UpdateUserRequest;
import com.fpt.servicecontract.utils.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


public interface UserService {
    BaseResponse delete(String id);

    BaseResponse register(RegisterRequest request);

    BaseResponse update(String id, UpdateUserRequest userRequest, MultipartFile file);

    BaseResponse search(SearchUserRequest searchUserRequest, Pageable pageable);

    BaseResponse getUserById(String id);

    BaseResponse getUserByPermission(String permission, Pageable pageable);

    BaseResponse resetPass(String email);

}

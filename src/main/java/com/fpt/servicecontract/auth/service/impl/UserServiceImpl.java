package com.fpt.servicecontract.auth.service.impl;

import com.fpt.servicecontract.auth.dto.*;
import com.fpt.servicecontract.auth.model.Role;
import com.fpt.servicecontract.auth.model.User;
import com.fpt.servicecontract.auth.model.UserStatus;
import com.fpt.servicecontract.auth.repository.UserRepository;
import com.fpt.servicecontract.auth.service.UserService;
import com.fpt.servicecontract.config.MailService;
import com.fpt.servicecontract.contract.service.CloudinaryService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import com.fpt.servicecontract.utils.DateUltil;
import com.fpt.servicecontract.utils.QueryUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;
    private final MailService mailService;

    @Transactional(rollbackOn = Exception.class)
    public BaseResponse delete(String id) {
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setStatus(UserStatus.INACTIVE);
            userRepository.save(user.get());
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Delete Successful", true, user);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Delete Unsuccessful", true, user);
    }

    public BaseResponse register(RegisterRequest request)   {
        var user = new User();
        user.setStatus(UserStatus.ACTIVE);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setDepartment(request.getDepartment());
        user.setPosition(request.getPosition());
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole(request.getRole());
        user.setPermissions(request.getPermissions());
        user.setIdentificationNumber(request.getIdentificationNumber());
        user.setDob(DateUltil.stringToDate(request.getDob(), "yyyy-MM-dd"));
        user.setCreatedDate(new Date());

        try {
            userRepository.save(user);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Create Successful", true, user);
        } catch (DataIntegrityViolationException e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Create failed due to an unexpected error", false, e.getMessage());
        }
    }

    public BaseResponse update(String id, UpdateUserRequest userRequest, MultipartFile file) {
        var userOptional = userRepository.findById(id);

        if(userOptional.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "User does not exist", true, null);
        }
        var user = userOptional.get();
        user.setName(userRequest.getName() == null ? user.getName() : userRequest.getName());
        user.setStatus(userRequest.getStatus() == null ? user.getStatus() : userRequest.getStatus());
        user.setDepartment(userRequest.getDepartment() == null ? user.getDepartment() : userRequest.getDepartment());
        user.setRole(userRequest.getRole() == null ? user.getRole() : userRequest.getRole());
        user.setPosition(userRequest.getPosition() == null ? user.getPosition() : userRequest.getPosition());
        user.setAddress(userRequest.getAddress() == null ? user.getAddress() : userRequest.getAddress());
        user.setGender(userRequest.getGender() == null ? user.isGender() : userRequest.getGender());
        user.setPhone(userRequest.getPhone() == null ? user.getPhone() : userRequest.getPhone());
        user.setDob(userRequest.getDob() == null ? user.getDob() : DateUltil.stringToDate(userRequest.getDob(), DateUltil.DATE_FORMAT_dd_MM_yyyy));
        user.setIdentificationNumber(userRequest.getIdentificationNumber() == null ? user.getIdentificationNumber() : userRequest.getIdentificationNumber());
        if (file != null) {
            try {
                user.setAvatar(cloudinaryService.uploadImage(file));
            } catch (IOException e) {
                return new BaseResponse(Constants.ResponseCode.FAILURE, "Upload file false", true, null);
            }
        }
        user.setPermissions(userRequest.getPermissions());


        try {
            userRepository.save(user);
            UserDto userDto = UserDto.builder()
                    .name(user.getName())
                    .id(user.getId())
                    .email(user.getEmail())
                    .status(user.getStatus())
                    .department(user.getDepartment())
                    .identificationNumber(user.getIdentificationNumber())
                    .position(user.getPosition())
                    .address(user.getAddress())
                    .role(user.getRole())
                    .gender(user.isGender())
                    .permissions(user.getPermissions())
                    .createdDate(String.valueOf(user.getCreatedDate()))
                    .updatedDate(String.valueOf(user.getUpdatedDate()))
                    .dob(String.valueOf(user.getDob()))
                    .avatar(user.getAvatar())
                    .phone(user.getPhone())
                    .userCode(user.getUserCode())
                    .build();
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Update Successful", true, userDto);
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Update failed", true, null);
        }

    }

    public BaseResponse search(SearchUserRequest searchUserRequest, Pageable pageable) {
        Page<UserInterface> result = userRepository.search(
                QueryUtils.appendPercent(searchUserRequest.getName()),
                QueryUtils.appendPercent(searchUserRequest.getEmail()),
                QueryUtils.appendPercent(searchUserRequest.getAddress()),
                QueryUtils.appendPercent(searchUserRequest.getIdentificationNumber()),
                searchUserRequest.getStatus(),
                QueryUtils.appendPercent(searchUserRequest.getDepartment()),
                QueryUtils.appendPercent(searchUserRequest.getPhoneNumber()),
                QueryUtils.appendPercent(searchUserRequest.getPosition()),
                Role.USER.getRole(),
                pageable);
        if (result.getTotalElements() > 0) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Search Successful", true, result);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Not Have any User", true, null);
    }

    public BaseResponse getUserById(String id) {
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.NOT_FOUND, "User not found", true, null);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Get Successful", true, user);
    }

    public BaseResponse getUserByPermission(String permission, Pageable pageable) {
        Page<UserInterface> result = userRepository.getUserWithPermission(
                QueryUtils.appendPercent(permission),
                pageable);
        if (result.getTotalElements() > 0) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Search Successful", true, result);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Search Successful", true, null);
    }

    public BaseResponse resetPass(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.NOT_FOUND, "user not found", true, null);
        }
        int passReset = new Random().nextInt(999999);
        user.get().setPassword(passwordEncoder.encode(String.valueOf(passReset)));
        userRepository.save(user.get());
        String[] to = new String[]{email};
        try {
            mailService.sendNewMail(to, null, "Password Reset", "<h1>Your pass after reset: <h1>" + passReset, null);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Found user", true, null);
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, e.getMessage(), false, null);
        }
    }

    @Override
    public BaseResponse retriverUser(String email) {
        var user = userRepository.getUserByEmail(email);
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Retriver Successful", true, user);
    }


}

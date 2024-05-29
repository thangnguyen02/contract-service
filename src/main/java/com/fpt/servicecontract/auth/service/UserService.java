package com.fpt.servicecontract.auth.service;

import com.fpt.servicecontract.auth.dto.RegisterRequest;
import com.fpt.servicecontract.auth.dto.SearchUserRequest;
import com.fpt.servicecontract.auth.dto.UpdateUserRequest;
import com.fpt.servicecontract.auth.dto.UserDto;
import com.fpt.servicecontract.auth.dto.UserInterface;
import com.fpt.servicecontract.auth.model.Role;
import com.fpt.servicecontract.auth.model.User;
import com.fpt.servicecontract.auth.model.UserStatus;
import com.fpt.servicecontract.auth.repository.UserRepository;
import com.fpt.servicecontract.contract.service.CloudinaryService;
import com.fpt.servicecontract.utils.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;

    @Transactional(rollbackOn = Exception.class)
    public BaseResponse delete(String id) {
        var user = userRepository.findById(id);
        if(user.isPresent()) {
            user.get().setStatus(UserStatus.INACTIVE);
            userRepository.save(user.get());
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Delete Successful", true, user);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Delete Unsuccessful", true, user);
    }

    @Transactional(rollbackOn = Exception.class)
    public BaseResponse register(RegisterRequest request) {

        var user = new User();
        user.setStatus(UserStatus.ACTIVE);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDepartment(request.getDepartment());
        user.setPosition(request.getPosition());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setPermissions(request.getPermissions());
        try{
            User user2 = userRepository.save(user);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Create Successful", true, user2);
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, e.getMessage(), true, null);
        }

    }
    @Transactional(rollbackOn = Exception.class)
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
        if(file != null) {
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
                    .status(user.getStatus())
                    .department(user.getDepartment())
                    .identificationNumber(user.getIdentificationNumber())
                    .position(user.getPosition())
                    .address(user.getAddress())
                    .role(user.getRole())
                    .gender(user.isGender())
                    .permissions(user.getPermissions())
                    .createdDate(user.getCreatedDate())
                    .updatedDate(user.getUpdatedDate())
                    .dob(user.getDob())
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
        Page<UserInterface> result =  userRepository.search(
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
        if(result.getTotalElements() > 0) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Search Successful", true, result);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Search Successful", true, null);
    }

    public BaseResponse getUserById(String id) {
        var user = userRepository.findById(id);
        if(user.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.NOT_FOUND, "User not found", true, null);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Get Successful", true, user);
    }
}

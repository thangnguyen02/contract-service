package com.fpt.servicecontract.auth.service;

import com.fpt.servicecontract.auth.dto.UpdateUserRequest;
import com.fpt.servicecontract.auth.dto.UserDto;
import com.fpt.servicecontract.auth.model.UserStatus;
import com.fpt.servicecontract.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String delete(String id) {
        var user = userRepository.findById(id).orElseThrow();

        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        return "Successfully";
    }

    public UserDto update(String id, UpdateUserRequest userRequest) {
        var user = userRepository.findById(id).orElseThrow();

        user.setName(userRequest.getName());
        user.setPassword(userRequest.getPassword());
        user.setStatus(userRequest.getStatus());
        user.setDepartment(user.getDepartment());
        user.setRole(userRequest.getRole());
        user.setPosition(userRequest.getPosition());
        user.setPermissions(userRequest.getPermissions());
        userRepository.save(user);
        return UserDto.builder()
                .name(user.getName())
                .phone(user.getPhone())
                .build();
    }
}

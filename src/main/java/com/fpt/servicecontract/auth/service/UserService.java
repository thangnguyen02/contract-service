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
import com.fpt.servicecontract.utils.DataUtil;
import com.fpt.servicecontract.utils.QueryUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackOn = Exception.class)
    public String delete(String id) {
        var user = userRepository.findById(id).orElseThrow();

        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        return "Successfully";
    }
    public String register(RegisterRequest request) {
        var user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDepartment(request.getDepartment());
        user.setPosition(request.getPosition());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setPermissions(request.getPermissions());
        userRepository.save(user);
        return "Successfully";
    }
    @Transactional(rollbackOn = Exception.class)
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

    public Page<UserInterface> search(SearchUserRequest searchUserRequest, Pageable pageable) {
        return userRepository.search(
                QueryUtils.appendPercent(searchUserRequest.getName()),
                QueryUtils.appendPercent(searchUserRequest.getEmail()),
                QueryUtils.appendPercent(searchUserRequest.getAddress()),
                QueryUtils.appendPercent(searchUserRequest.getIdentificationNumber()),
                searchUserRequest.getStatus(),
                QueryUtils.appendPercent(searchUserRequest.getDepartment()),
                QueryUtils.appendPercent(searchUserRequest.getPhoneNumber()),
                QueryUtils.appendPercent(searchUserRequest.getPosition()),
                Role.USER,
                pageable);
    }

}

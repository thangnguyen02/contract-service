package com.fpt.servicecontract;

import com.fpt.servicecontract.auth.model.Permission;
import com.fpt.servicecontract.auth.model.Role;
import com.fpt.servicecontract.auth.model.User;
import com.fpt.servicecontract.auth.model.UserStatus;
import com.fpt.servicecontract.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
@RequiredArgsConstructor
public class ServiceContractApplication {
	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(ServiceContractApplication.class, args);
	}

	@EventListener
	public void onApplicationReady(ApplicationReadyEvent event) {
		if (userRepository.count() == 0) {
			User adminUser = new User();
			adminUser.setName("admin");
			adminUser.setEmail("admin@gmail.com");
			adminUser.setDepartment("IT");
			adminUser.setPosition("Dev");
			adminUser.setPhone("0352334588");
			adminUser.setPermissions(Permission.getAllPermissions());
			adminUser.setPassword(passwordEncoder.encode("admin"));
			adminUser.setStatus(UserStatus.ACTIVE);
			adminUser.setRole(Role.ADMIN);
			userRepository.save(adminUser);
			User user = new User();
			user.setName("user");
			user.setEmail("user@gmail.com");
			user.setDepartment("IT");
			user.setPosition("Dev");
			user.setPhone("123456789");
			user.setPermissions(Set.of(Permission.MANAGE_CONTRACT, Permission.CREATE_CONTRACT));
			user.setPassword(passwordEncoder.encode("user"));
			user.setStatus(UserStatus.ACTIVE);
			user.setRole(Role.USER);
			userRepository.save(user);
		}
	}
}

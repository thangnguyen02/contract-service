package com.fpt.servicecontract;

import com.fpt.servicecontract.auth.model.Permission;
import com.fpt.servicecontract.auth.model.Role;
import com.fpt.servicecontract.auth.model.User;
import com.fpt.servicecontract.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

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
			adminUser.setRole(Role.ADMIN);
			userRepository.save(adminUser);
		}
	}
}

package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    Optional<Notification> findByIdAndMarkedDeletedFalse(String id);

    Page<Notification> findAllByMarkedDeletedFalse(Pageable pageable);
}

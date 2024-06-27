package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    Optional<Notification> findByIdAndMarkedDeletedFalse(String id);

    List<Notification> findAllByMarkedDeletedFalse();
}

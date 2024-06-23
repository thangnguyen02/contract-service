package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationService {
    Optional<Notification> findNotificationById(String id);

    boolean deleteNotificationById(String id);

    Page<Notification> findAllNotifications(Pageable pageable);

    String create(Notification message);

    void readNotificationById(String id, boolean isRead);
}

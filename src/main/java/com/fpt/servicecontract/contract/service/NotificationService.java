package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.model.Notification;
import com.fpt.servicecontract.utils.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationService {
    Optional<Notification> findNotificationById(String id);

    boolean deleteNotificationById(String id);

    BaseResponse findAllNotifications(Pageable pageable, String email);

    String create(Notification message);

    void readNotificationById(String id, boolean isRead);

    Integer countNotRead(String email);
}

package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.dto.CreateNotificationRequest;
import com.fpt.servicecontract.contract.dto.NotificationDto;
import com.fpt.servicecontract.contract.model.EntityId;
import com.fpt.servicecontract.utils.BaseResponse;

public interface NotificationService {
    BaseResponse findAllNotifications(String recipientId);
    BaseResponse deleteNotification(String id);
    String notifyFrontend(NotificationDto message);
}

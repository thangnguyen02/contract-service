package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.dto.CreateNotificationRequest;
import com.fpt.servicecontract.contract.model.EntityId;
import com.fpt.servicecontract.utils.BaseResponse;

public interface NotificationService {
    BaseResponse findAllNotifications(String recipientId);
    BaseResponse createNotification(CreateNotificationRequest request, EntityId entityIdCreate);
    BaseResponse updateNotification(String id, CreateNotificationRequest request);
    BaseResponse deleteNotification(String id);
}

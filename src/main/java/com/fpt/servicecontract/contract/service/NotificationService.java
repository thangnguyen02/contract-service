package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.dto.CreateNotificationRequest;
import com.fpt.servicecontract.utils.BaseResponse;

public interface NotificationService {
    BaseResponse findAllNotifications();
    BaseResponse createNotification(CreateNotificationRequest request);
    BaseResponse updateNotification(String id, CreateNotificationRequest request);
    BaseResponse deleteNotification(String id);
}

package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.dto.CreateNotificationRequest;
import com.fpt.servicecontract.contract.dto.NotificationDto;
import com.fpt.servicecontract.contract.model.Notification;
import com.fpt.servicecontract.contract.repository.NotificationRepository;
import com.fpt.servicecontract.contract.service.NotificationService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import com.fpt.servicecontract.utils.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public BaseResponse findAllNotifications() {
        List<Object[]> notifications = notificationRepository.getAllNotificationBy();
        if (notifications.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Not have any notification", true, null);
        }
        List<NotificationDto> notificationDtos = new ArrayList<>();

        for(Object[] obj : notifications) {
            notificationDtos.add(NotificationDto.builder()
                    .id(Objects.nonNull(obj[0]) ? obj[0].toString() : null)
                    .title(Objects.nonNull(obj[1]) ? obj[1].toString() : null)
                    .message(Objects.nonNull(obj[2]) ? obj[2].toString() : null)
                    .createdBy(Objects.nonNull(obj[3]) ? obj[3].toString() : null)
                    .createdDate(Objects.nonNull(obj[4]) ? obj[4].toString() : null)
                    .markedDeleted((boolean) obj[5])
                    .markRead((boolean) obj[6])
                    .build());
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "List Notifications", true, notificationDtos);
    }

    @Override
    public BaseResponse createNotification(CreateNotificationRequest request) {
        Notification notification = Notification.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .createdBy(request.getCreatedBy())
                .createdDate(LocalDateTime.now())
                .markedDeleted(false)
                .markRead(false)
                .build();
        try {
            notificationRepository.save(notification);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Create successfully", true, NotificationDto.builder()
                    .id(notification.getId())
                    .title(notification.getTitle())
                    .message(notification.getMessage())
                    .createdBy(notification.getCreatedBy())
                    .build());
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Create failed", true, null);
        }
    }

    @Override
    public BaseResponse updateNotification(String id, CreateNotificationRequest request) {
        var noti = notificationRepository.findById(id);
        if(noti.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Notification not found", true, null);
        }

        Notification notification = noti.get();
        notification.setTitle(DataUtil.isNullObject(request.getTitle()) ? notification.getTitle() : request.getTitle());
        notification.setMessage(DataUtil.isNullObject(request.getMessage()) ? notification.getMessage() : request.getMessage());
        notification.setCreatedBy(DataUtil.isNullObject(request.getCreatedBy()) ? notification.getCreatedBy() : request.getCreatedBy());
        notification.setMarkRead(DataUtil.isNullObject(request.getMarkRead()) ? notification.getMarkRead() : request.getMarkRead());
        try {
            notificationRepository.save(notification);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Notification update successfully", true, NotificationDto.builder()
                    .id(notification.getId())
                    .title(notification.getTitle())
                    .markRead(notification.getMarkRead())
                    .createdBy(notification.getCreatedBy())
                    .message(notification.getMessage())
                    .markedDeleted(notification.getMarkedDeleted())
                    .build());
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Update failed", true, null);
        }

    }

    @Override
    public BaseResponse deleteNotification(String id) {
        var noti = notificationRepository.findById(id);
        if(noti.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Notification not found", true, null);
        }

        noti.get().setMarkedDeleted(true);
        notificationRepository.save(noti.get());
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Notification delete successfully", true, null);
    }
}

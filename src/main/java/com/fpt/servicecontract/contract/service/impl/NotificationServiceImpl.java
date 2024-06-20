package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.dto.CreateNotificationRequest;
import com.fpt.servicecontract.contract.dto.NotificationDto;
import com.fpt.servicecontract.contract.model.Notification;
import com.fpt.servicecontract.contract.repository.NotificationRepository;
import com.fpt.servicecontract.contract.service.NotificationService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public String notifyFrontend(NotificationDto message) {
        messagingTemplate.convertAndSend("/topic/notifications", message.toString());
        return message.getMessage();
    }

    @Override
    public BaseResponse findAllNotifications(String recipientId) {
        List<Object[]> notifications = notificationRepository.getAllNotificationBy(recipientId);
        if (notifications.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Not have any notification", true, null);
        }
        List<NotificationDto> notificationDtos = new ArrayList<>();

        for (Object[] obj : notifications) {
            notificationDtos.add(NotificationDto.builder()
                    .id(Objects.nonNull(obj[0]) ? obj[0].toString() : null)
                    .title(Objects.nonNull(obj[1]) ? obj[1].toString() : null)
                    .message(Objects.nonNull(obj[2]) ? obj[2].toString() : null)
                    .senderId(Objects.nonNull(obj[3]) ? obj[3].toString() : null)
                    .createdDate(Objects.nonNull(obj[4]) ? obj[4].toString() : null)
                    .markedDeleted((boolean) obj[5])
                    .markRead((boolean) obj[6])
                    .recipientId(Objects.nonNull(obj[7]) ? obj[7].toString() : null)
                    .objectId(Objects.nonNull(obj[8]) ? obj[8].toString() : null)
                    .entityType(Objects.nonNull(obj[9]) ? obj[9].toString() : null)
                    .description(Objects.nonNull(obj[9]) ? obj[9].toString() : null)
                    .senderName(Objects.nonNull(obj[10]) ? obj[10].toString() : null)
                    .recipientName(Objects.nonNull(obj[11]) ? obj[11].toString() : null)
                    .build());
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "List Notifications", true, notificationDtos);
    }

    public String createNotification(CreateNotificationRequest request ) {

        Notification notification = Notification.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .senderId(request.getSenderId())
                .createdDate(LocalDateTime.now())
                .recipientId(request.getRecipientId())
                .markedDeleted(false)
                .markRead(false)
                .build();
        notifyFrontend(NotificationDto.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .senderId(request.getSenderId())
                .recipientId(request.getRecipientId())
                .createdDate(String.valueOf(LocalDateTime.now()))
                .markedDeleted(false)
                .markRead(false)
                .build());
        try {
            notificationRepository.save(notification);
            return "Create successfully";
        } catch (Exception e) {
            return "Create failed";
        }
    }

    @Override
    public BaseResponse deleteNotification(String id) {
        var noti = notificationRepository.findById(id);
        if (noti.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Notification not found", true, null);
        }

        noti.get().setMarkedDeleted(true);
        notificationRepository.save(noti.get());
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Notification delete successfully", true, null);
    }
}

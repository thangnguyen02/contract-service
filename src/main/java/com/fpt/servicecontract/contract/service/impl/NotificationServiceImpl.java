package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.auth.model.User;
import com.fpt.servicecontract.auth.repository.UserRepository;
import com.fpt.servicecontract.contract.model.Notification;
import com.fpt.servicecontract.contract.repository.NotificationRepository;
import com.fpt.servicecontract.contract.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Override
    public Optional<Notification> findNotificationById(String id) {
        return notificationRepository.findByIdAndMarkedDeletedFalse(id);
    }

    @Override
    public boolean deleteNotificationById(String id) {
        Optional<Notification> notificationOptional = notificationRepository.findById(id);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            notification.setMarkedDeleted(true);
            notificationRepository.save(notification);
            return true;
        }
        return false;
    }

    @Override
    public Page<Notification> findAllNotifications(Pageable pageable, String email) {
        List<Notification> notifications = notificationRepository.findAllByMarkedDeletedFalse();
        List<Notification> filteredNotifications = notifications.stream()
                .filter(notification -> email.trim().equalsIgnoreCase(notification.getReceiver()))
                .sorted(Comparator.comparing(Notification::getCreatedDate).reversed())
                .toList();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        int endItem = Math.min(startItem + pageSize, filteredNotifications.size());
        List<Notification> pagedNotifications = filteredNotifications.subList(startItem, endItem);
        return new PageImpl<>(pagedNotifications, pageable, filteredNotifications.size());
    }

    @Override
    public String create(Notification notification) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        notification.setCreatedDate(currentDateTime.plusHours(7));
        notification.setMarkedDeleted(false);
        notification.setMarkRead(false);
        notification.getReceivers().forEach(receiver -> {
            Notification noti = new Notification(notification);
            noti.setReceiver(receiver);
            notificationRepository.save(noti);
        });

        notification.getReceivers().forEach(f -> {
            messagingTemplate.convertAndSend("/topic/notifications/" + f, notification);
            Optional<User> user = userRepository.findByEmail(f);
            user.ifPresent(value -> sendPushNotification(value.getTokenDevice(), notification.getTitle(), notification.getMessage(), notification.getContractId()));
        });
        return "Notification ok! ";
    }

    public void sendPushNotification(String tokenDevice, String title, String content, String contractId) {
        if (!StringUtils.isBlank(tokenDevice)) {
            String url = "https://exp.host/--/api/v2/push/send";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("to", tokenDevice);
            body.put("sound", "default");
            body.put("title", title);
            body.put("body", content);
            Map<String, String> data = new HashMap<>();
            data.put("screen", "(drawer)/(tabs)/new-contract/details/" + contractId);
            body.put("data", data);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            restTemplate.postForObject(url, request, String.class);
        }
    }

    @Override
    public void readNotificationById(String id, boolean isRead) {
        Optional<Notification> notificationOptional = notificationRepository.findById(id);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            notification.setMarkRead(isRead);
            notificationRepository.save(notification);
        } else {
            throw new NotFoundException("Not found!!");
        }
    }

    @Override
    public Integer countNotRead(String email) {
        List<Notification> notificationList = notificationRepository.findAllByMarkedDeletedFalse();
        List<Notification> list = notificationList.stream().filter(f -> email.trim().equalsIgnoreCase(f.getReceiver()) && !f.getMarkRead()).toList();
        return list.size();
    }
}

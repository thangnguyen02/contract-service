package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.model.Notification;
import com.fpt.servicecontract.contract.repository.NotificationRepository;
import com.fpt.servicecontract.contract.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

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
    public Page<Notification> findAllNotifications(Pageable pageable) {
        return notificationRepository.findAllByMarkedDeletedFalse(pageable);
    }

    @Override
    public String create(Notification notification) {
        notification.setCreatedDate(LocalDateTime.now());
        notification.setMarkedDeleted(false);
        notification.setMarkRead(false);
        notification.getReceivers().forEach(f -> {
            messagingTemplate.convertAndSend("/topic/notifications/" + f, notification);
        });
        notificationRepository.save(notification);
        return "Notification ok! ";
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
}

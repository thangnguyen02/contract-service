package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.model.Notification;
import com.fpt.servicecontract.contract.repository.NotificationRepository;
import com.fpt.servicecontract.contract.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Page<Notification> findAllNotifications(Pageable pageable, String email) {
        List<Notification> notifications = notificationRepository.findAllByMarkedDeletedFalse();
        List<Notification> filteredNotifications = notifications.stream()
                .filter(notification -> notification.getReceivers().contains(email))
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
        notification.setCreatedDate(LocalDateTime.now());
        notification.setMarkedDeleted(false);
        notification.setMarkRead(false);
        notification.getReceivers().forEach(f -> {
            messagingTemplate.convertAndSend("/topic/notifications/" + f, notificationRepository.save(notification));
        });
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

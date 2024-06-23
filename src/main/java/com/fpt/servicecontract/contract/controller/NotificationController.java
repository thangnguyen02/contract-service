package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.contract.model.Notification;
import com.fpt.servicecontract.contract.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping()
    public String testNotifyAll(@RequestBody Notification message) {
        message.setSenderId("7bfb03f0-8fbf-462d-9f73-99bfb3e5c3a3");
        message.setReceivers(List.of("45130aca-2196-4bbc-87ac-3ad3eafe5d8d"));
        return notificationService.create(message);
    }

    @GetMapping
    public ResponseEntity<Page<Notification>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationService.findAllNotifications(pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable String id) {
        Optional<Notification> notificationOptional = notificationService.findNotificationById(id);
        return notificationOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificationById(@PathVariable String id) {
        boolean isDeleted = notificationService.deleteNotificationById(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/{isRead}")
    public ResponseEntity<Void> readNotificationById(@PathVariable String id, @PathVariable boolean isRead) {
        notificationService.readNotificationById(id, isRead);
        return ResponseEntity.noContent().build();
    }
}

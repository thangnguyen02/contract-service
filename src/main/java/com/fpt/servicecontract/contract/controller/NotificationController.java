package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.config.JwtService;
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
    private final JwtService jwtService;
    @PostMapping()
    public String testNotifyAll(@RequestBody Notification message) {
        message.setSender("salesep490@gmail.com");
        message.setReceivers(List.of("officeadminsep490@gmail.com"));
        return notificationService.create(message);
    }

    @GetMapping
    public ResponseEntity<Page<Notification>> getAllNotifications(@RequestHeader("Authorization") String bearerToken,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String email = jwtService.extractUsername(bearerToken.substring(7));
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationService.findAllNotifications(pageable,email);
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

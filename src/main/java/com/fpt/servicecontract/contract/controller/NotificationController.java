package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.contract.dto.CreateNotificationRequest;
import com.fpt.servicecontract.contract.service.NotificationService;
import com.fpt.servicecontract.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping()
    public ResponseEntity<BaseResponse> getAll() {

        return ResponseEntity.ok(notificationService.findAllNotifications());
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> create(
            @RequestBody CreateNotificationRequest createNotificationRequest
            )
    {
        return ResponseEntity.ok(notificationService.createNotification(createNotificationRequest));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(notificationService.deleteNotification(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> update(
            @PathVariable("id") String id,
            @RequestBody CreateNotificationRequest createNotificationRequest
    ) {
        return ResponseEntity.ok(notificationService.updateNotification(id, createNotificationRequest));
    }
}

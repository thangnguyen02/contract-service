package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    @Autowired
    private NotificationService notificationService;
}
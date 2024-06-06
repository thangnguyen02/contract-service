package com.fpt.servicecontract.contract.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class CreateNotificationRequest {
    private String title;
    private String message;
    private String senderId;
    private Boolean markRead;
    private String recipientId;
}

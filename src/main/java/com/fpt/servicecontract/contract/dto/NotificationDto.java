package com.fpt.servicecontract.contract.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class NotificationDto {
    private String id;
    private String title;
    private String message;
    private String createdBy;
    private String createdDate;
    private boolean markedDeleted;
    private boolean markRead;
}

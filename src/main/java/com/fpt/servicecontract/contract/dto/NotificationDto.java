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
    private String senderId;
    private String recipientId;
    private String objectId;
    private String entityType;
    private String createdDate;
    private boolean markedDeleted;
    private boolean markRead;
    private String description;
    private String senderName;
    private String recipientName;

    @Override
    public String toString() {
        return "NotificationDto{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", senderId='" + senderId + '\'' +
                ", recipientId='" + recipientId + '\'' +
                ", objectId='" + objectId + '\'' +
                ", entityType='" + entityType + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", markedDeleted=" + markedDeleted +
                ", markRead=" + markRead +
                ", description='" + description + '\'' +
                ", senderName='" + senderName + '\'' +
                ", recipientName='" + recipientName + '\'' +
                '}';
    }
}

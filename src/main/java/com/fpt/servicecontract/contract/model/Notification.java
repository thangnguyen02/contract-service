package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @UuidGenerator
    private String id;
    private String title;
    private String message;
    private String sender;
    private List<String> receivers;
    private String typeNotification;
    private String idType;
    private LocalDateTime createdDate;
    private Boolean markedDeleted;
    private Boolean markRead;
    private String contractId;
    private String receiver;

    public Notification(Notification notification) {
        this.id = notification.id;
        this.title = notification.title;
        this.message = notification.message;
        this.sender = notification.sender;
        this.typeNotification = notification.typeNotification;
        this.idType = notification.idType;
        this.createdDate = notification.createdDate;
        this.markedDeleted = notification.markedDeleted;
        this.markRead = notification.markRead;
        this.contractId = notification.contractId;
    }

}

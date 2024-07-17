package com.fpt.servicecontract.contract.dto;

import com.fpt.servicecontract.contract.model.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;


@Getter
@Setter
@Builder
public class NotificationDto {
    private Page<Notification> notification;
    private String tokenDevice;
}

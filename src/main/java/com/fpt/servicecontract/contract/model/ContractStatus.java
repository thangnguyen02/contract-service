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
public class ContractStatus {
    @Id
    @UuidGenerator
    private String id;
    private String sender;//id email
    private List<String> receiver;// id email
    private String contractId;
    private LocalDateTime sendDate;

}

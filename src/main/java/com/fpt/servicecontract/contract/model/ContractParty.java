package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractParty {
    @Id
    @UuidGenerator
    private String id;
    private String contract_id;
    private String party_id;
}

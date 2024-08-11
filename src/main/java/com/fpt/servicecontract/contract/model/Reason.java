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
public class Reason {
    @Id
    @UuidGenerator
    private String id;

    private String title;

    private String description;

    private Boolean markDeleted;

}

package com.fpt.servicecontract.contract.model;

import com.fpt.servicecontract.contract.enums.EntityType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "entity_id")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityId {
    @Id
    @UuidGenerator
    private String id;
    //id của bản ghi entity
    private String objectId;
    //id của loại entity
    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    private String description;
}

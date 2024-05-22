package com.fpt.servicecontract.contract.model;

import com.fpt.servicecontract.auth.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class OldContract {
    @Id
    @UuidGenerator
    private String id;
    private String contractName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
    private String partyA;
    private String partyB;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private User user;
}

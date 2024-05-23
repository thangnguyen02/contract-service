package com.fpt.servicecontract.contract.model;

import com.fpt.servicecontract.auth.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Getter
@Setter
public class OldContract {
  @Id
  @UuidGenerator
  private String id;
  private String contractName; // required

  private String createdBy;

  @Column(columnDefinition = "NVARCHAR(MAX)")
  private String content;

  private Boolean isDeleted = false;

  private String file;
  private LocalDateTime createdDate;
  private LocalDateTime contractSignDate;
  private LocalDateTime contractStartDate;
  private LocalDateTime contractEndDate;
}

package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
  private String content;
  private String file;
  private LocalDateTime createdDate;
  private LocalDateTime contractSignDate;
  private LocalDateTime contractStartDate;
  private LocalDateTime contractEndDate;
}

package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;


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
  @Column(name="content", columnDefinition="LONGTEXT")
  private String content;
  private Boolean isDeleted = false;
  private String file;
  private Date createdDate;
  private Date contractSignDate;
  private Date contractStartDate;
  private Date contractEndDate;
  private String updatedBy;
  private String contractTypeId;
}

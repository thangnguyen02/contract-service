package com.fpt.servicecontract.contract.model;

import jakarta.persistence.Column;
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
public class ContractAppendices {
    @Id
    @UuidGenerator
    private String id;
    private String contract_id;
    private String appendix_number;
    private String appendix_date;// Ngày ký phụ lục
    @Column(columnDefinition="LONGTEXT")
    private String appendix_details;// Chi tiết phụ lục
    private String effective_date;//Ngày bắt đầu hiệu lực
    private String expiry_date;// Ngày hết hạn (nếu có)
}

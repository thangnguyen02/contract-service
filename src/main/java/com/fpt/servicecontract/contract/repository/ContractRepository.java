package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.Contract;
import com.fpt.servicecontract.utils.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {

    @Query(value = """
            SELECT\s
                name,
                created_by,
                file,
                created_date,
                id,
                status,
                is_urgent
            FROM
                contract
            WHERE
                mark_deleted = 0
                 AND (created_by = :email OR id IN (:ids))  order by is_urgent desc, created_date desc
                 """, nativeQuery = true)
    Page<Object[]>  findAllContract(Pageable p, String email , List<String> ids);

    @Query(value = """
            SELECT\s
                     c.id,
                     c.name,
                     c.number,
                     c.rule,
                     c.term,
                     pa.id,
                     pa.name,
                     pa.address,
                     pa.tax_number,
                     pa.presenter,
                     pa.position,
                     pa.business_number,
                     pa.bank_id,
                     pa.email,
                     pa.bank_name,
                     pa.bank_acc_ower,
                     pb.id,
                     pb.name,
                     pb.address,
                     pb.tax_number,
                     pb.presenter,
                     pb.position,
                     pb.business_number,
                     pb.bank_id,
                     pb.email,
                     pb.bank_name,
                     pb.bank_acc_ower,
                     c.file
                 FROM
                     fpt_company.contract c
                         LEFT JOIN
                     contract_party pa ON c.partyaid = pa.id
                         LEFT JOIN
                     contract_party pb ON c.partybid = pb.id
                 WHERE
                    c.mark_deleted = 0 and c.id = :id
            """, nativeQuery = true)
    List<Object[]> findByIdContract(String id);

    @Query(value = """
            SELECT\s
                 count(id)
                 from contract  where 
                 created_date between :fromDate and :toDate
                 and mark_deleted = 0
                 """, nativeQuery = true)
    Integer  staticalNewContract(
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate
    );

    @Query(value = """
            SELECT
                 count(id)
                 from contract  where 
                 mark_deleted = 0
                 and (status = :status or :status is null)
                 """, nativeQuery = true)
    Integer  statisticSignStatus(
            @Param("status") String status
    );
}

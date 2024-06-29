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
            WITH latest_status AS (
                                                                SELECT\s
                                                                    cs.contract_id,
                                                                    cs.status,
                                                                    ROW_NUMBER() OVER (PARTITION BY cs.contract_id ORDER BY cs.send_date DESC) AS rn
                                                                FROM\s
                                                                    contract_status cs
                                                            )
                                                            SELECT\s
                                                                c.name,\s
                                                                c.created_by,\s
                                                                c.file,\s
                                                                c.created_date,\s
                                                                c.id,\s
                                                                c.status,\s
                                                                c.is_urgent,\s
                                                                c.approved_by,\s
                                                                ls.status AS statusCurrent,\s
                                                                c.mark_deleted
                                                            FROM\s
                                                                contract c
                                                            LEFT JOIN\s
                                                                latest_status ls ON c.id = ls.contract_id AND ls.rn = 1
                                                            WHERE\s
                                                                c.mark_deleted = 0\s
                                                                AND (c.created_by = :email OR c.id IN (:ids))
                                                                AND (ls.status = :statusCurrentSearch OR :statusCurrentSearch IS NULL)
                                                            ORDER BY\s
                                                                c.is_urgent DESC,\s
                                                                c.created_date DESC;
                                                            
                 """, nativeQuery = true)
    Page<Object[]>  findAllContract(Pageable p, String email , List<String> ids, String statusCurrentSearch);

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
                     c.file,
                     c.signa,
                     c.signb,
                     c.created_by,
                     c.approved_by,
                     c.is_urgent,
                     c.contract_type_id
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

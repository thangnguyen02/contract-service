package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.ContractAppendices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
@Repository
public interface ContractAppendicesRepository extends JpaRepository<ContractAppendices, String> {
    @Query(value = """
            WITH latest_status AS (        \s
                       SELECT
                         cs.contract_id,
                         cs.status,
                         cs.sender,
                         ROW_NUMBER() OVER (PARTITION BY cs.contract_id ORDER BY cs.send_date DESC) AS rn
                     FROM
                         contract_status cs
                 )
                 SELECT
                     c.name,
                     c.created_by,
                     c.file,
                     c.created_date,
                     c.id,
                     c.status,
                     c.approved_by,
                     ls.status AS statusCurrent,
                     c.mark_deleted,
                     c.value,
                     ls.sender,
                     c.contract_id,
                     c.number,
                     us.phone,
                     us.name as userName,
                     c.signA,
                     c.signB
                 FROM
                     contract_appendices c
                 LEFT JOIN
                     latest_status ls ON c.id = ls.contract_id AND ls.rn = 1
                 join 
                     users us on c.created_by = us.email
                 WHERE
                     c.mark_deleted = 0
                     AND (ls.status in (:statusCurrentSearch))
                     and (c.contract_id = :contractId) 
                 ORDER BY
                     c.created_date DESC;
                    \s""", nativeQuery = true)
    Page<Object[]> findAllContractAppendices(Pageable p, List<String> statusCurrentSearch, String contractId);

    @Query(value = """
        select * from contract_appendices c where mark_deleted = 0 and c.contract_id = :contractId
    """, nativeQuery = true)
    Page<ContractAppendices> findByContractId(String contractId, Pageable pageable);
    @Query(value = """
        select * from contract_appendices c where mark_deleted = 0 and c.contract_id = :contractId
    """, nativeQuery = true)
    List<ContractAppendices> findByContractId(String contractId);
    @Query(value = """
                    SELECT c.created_by, SUM(c.value), count(c.id) AS numberSales
                        FROM
                            contract_appendices c
                    WHERE
                        c.mark_deleted = 0
                        AND (c.created_by in (:emails))
                        and (month(c.created_date) = :monthSearch or :monthSearch is null)
                        and (year(c.created_date) = :yearSearch or :yearSearch is null)
                        AND (c.status = 'SUCCESS')
                    GROUP BY c.created_by
            """, nativeQuery = true)
    List<Object[]> getSaleAndNumberSales(List<String> emails, Integer monthSearch, Integer yearSearch);

    @Query(value = """
                    SELECT SUM(ca.value) AS numberSales
                        FROM
                            contract_appendices ca
                    WHERE
                        ca.mark_deleted = 0
                        and (month(ca.created_date) = :monthSearch or :monthSearch is null)
                        and (year(ca.created_date) = :yearSearch or :yearSearch is null)
                        AND (ca.status = 'SUCCESS')
                    GROUP BY ca.created_by
            """, nativeQuery = true)
    Double getTotalValue(Integer monthSearch, Integer yearSearch);


    @Query(value = """
            SELECT *
                 FROM
                     fpt_company.contract_appendices c
                 WHERE
                    c.mark_deleted = 0 and c.id = :id
            """, nativeQuery = true)
    Optional<ContractAppendices> findByIdContractAppendices(String id);
}
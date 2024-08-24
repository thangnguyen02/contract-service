package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@SuppressWarnings("ALL")
@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {

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
                     c.is_urgent,
                     c.approved_by,
                     ls.status AS statusCurrent,
                     pt.email,
                     c.mark_deleted,
                    (
                       SELECT JSON_ARRAYAGG(ca.id) AS appendices
                       FROM fpt_company.contract_appendices ca
                       WHERE ca.contract_id = c.id
                    ) AS appendices,
                     c.value,
                     ls.sender,
                     c.signA,
                     c.signB,
                     c.number
                 FROM
                     contract c
                 JOIN 
                     party pt on c.partybid = pt.id
                 LEFT JOIN
                     latest_status ls ON c.id = ls.contract_id AND ls.rn = 1
                 WHERE
                     c.mark_deleted = 0
                     AND (c.created_by = :email OR c.id IN (:ids))
                     AND (ls.status in (:statusCurrentSearch))
                     AND (
                     lower(c.name) like lower(:search)
                     OR lower(c.created_by) like lower(:search)
                     OR c.id like lower(:search)
                     OR c.approved_by like lower(:search) 
                     OR :search is null
                     )
                     
                 ORDER BY
                     c.is_urgent DESC,
                     c.updated_date DESC;
                    \s""", nativeQuery = true)
    List<Object[]> findAllContract(String email, List<String> ids, List<String> statusCurrentSearch, String search);

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
                     c.contract_type_id,
                     c.value,
                     c.status,
                     pa.phone,
                     pb.phone
                 FROM
                     fpt_company.contract c
                         LEFT JOIN
                     party pa ON c.partyaid = pa.id
                         LEFT JOIN
                     party pb ON c.partybid = pb.id
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
    Integer staticalNewContract(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query(value = """
            SELECT
                 count(id)
                 from contract  where 
                 mark_deleted = 0
                 and (status = :status or :status is null)
                 """, nativeQuery = true)
    Integer statisticSignStatus(@Param("status") String status);


    @Query(value = """
            WITH latest_status AS (        \s
                       SELECT
                         cs.contract_id,
                         cs.status,
                         ROW_NUMBER() OVER (PARTITION BY cs.contract_id ORDER BY cs.send_date DESC) AS rn
                     FROM
                         contract_status cs
                 )
                 SELECT
                     count(*)
                 FROM
                     contract c
                 LEFT JOIN
                     latest_status ls ON c.id = ls.contract_id AND ls.rn = 1
                 WHERE
                     c.mark_deleted = 0
                     AND (c.created_by = :email OR c.id IN (:ids))
                     AND (ls.status = :statusCurrentSearch)
                    \s""", nativeQuery = true)
    Integer getNumberContractBySale(String email, List<String> ids, String statusCurrentSearch);


    @Query(value = """
                    WITH  latest_status AS (
                        SELECT 
                            cs.contract_id,
                            cs.status,
                            ROW_NUMBER() OVER (PARTITION BY cs.contract_id ORDER BY cs.send_date DESC) AS rn
                        FROM
                            contract_status cs
                    )
                    SELECT
                        SUM(CASE WHEN ls.status = 'APPROVED' THEN 1 ELSE 0 END) AS approved_count, 
                        SUM(CASE WHEN ls.status = 'WAIT_APPROVE' THEN 1 ELSE 0 END) AS wait_approve_count,
                        SUM(CASE WHEN ls.status = 'SUCCESS' THEN 1 ELSE 0 END) AS done_count,
                        SUM(CASE WHEN (ls.status = 'SIGN_A_OK' or ls.status = 'SIGN_B_OK') THEN 1 ELSE 0 END) AS signed_count,
                        SUM(CASE WHEN (ls.status = 'WAIT_SIGN_A' or ls.status = 'WAIT_SIGN_B') THEN 1 ELSE 0 END) AS wait_sign,
                        SUM(CASE WHEN (ls.status = 'NEW' or ls.status = 'APPROVE_FAIL' or ls.status = 'SIGN_B_FAIL' or ls.status = 'SIGN_A_FAIL') THEN 1 ELSE 0 END) AS manager_count
                    FROM
                        contract c
                    LEFT JOIN
                        latest_status ls ON c.id = ls.contract_id AND ls.rn = 1
                    WHERE
                        c.mark_deleted = 0
                        AND (c.created_by = :email OR c.id IN (:ids))
            """, nativeQuery = true)
    String[]
    getNotificationContractNumber(String email, List<String> ids);

    @Query(value = """
            WITH reasons AS (
                SELECT\s
                    (SELECT\s
                        created_by
                     FROM
                        fpt_company.contract
                     WHERE
                        id = contract_id) AS created_by,
                    reason_id
                FROM
                    fpt_company.contract_history
                WHERE
                    reason_id IS NOT NULL\s
                    AND reason_id != ''
            )
            SELECT count(*) as reason_count, (select title from fpt_company.reason where id = reason_id) as title
            FROM reasons where created_by = :email GROUP BY reason_id limit :number
            """, nativeQuery = true)
    List<Object[]> countReason(String email, int number);

    @Query(value = """
            WITH reasons AS (
                SELECT\s
                    (SELECT\s
                        created_by
                     FROM
                        fpt_company.contract
                     WHERE
                        id = contract_id) AS created_by,
                    reason_id
                FROM
                    fpt_company.contract_history
                WHERE
                    reason_id IS NOT NULL\s
                    AND reason_id != ''
            )
            SELECT count(*) as reason_count, (select title from fpt_company.reason where id = reason_id) as title
            FROM reasons where created_by = :email GROUP BY reason_id limit :number
            """, nativeQuery = true)
    List<Object[]> countTopSale();

    @Query(value = """
            SELECT\s
                COUNT(*) AS number,
                created_by,
                (SELECT\s
                        name
                    FROM
                        fpt_company.users
                    WHERE
                        email = created_by) AS name
            FROM
                fpt_company.contract c
            WHERE
                c.status = 'SUCCESS'
            GROUP BY c.created_by
            ORDER BY number DESC
            """, nativeQuery = true)
    List<Object[]> contractSuccess();

    @Query(value = """
            WITH TopUsers AS (
                SELECT\s
                    ch.reason_id,
                    c.created_by,
                    COUNT(*) AS user_count
                FROM
                    fpt_company.contract_history ch
                    JOIN contract c ON ch.contract_id = c.id
                WHERE
                    ch.status IN ('APPROVE_FAIL', 'SIGN_A_FAIL', 'SIGN_B_FAIL')
                GROUP BY ch.reason_id, c.created_by
            ),
            TopUserDetails AS (
                SELECT\s
                    reason_id,
                    created_by AS topUser,
                    ROW_NUMBER() OVER (PARTITION BY reason_id ORDER BY user_count DESC) AS rn
                FROM\s
                    TopUsers
            )
            SELECT\s
                COUNT(ch.reason_id) AS totalNumberOfRejected,
                r.title AS reason,
                tud.topUser as userIsRejectedThemost,
                u.name AS NameUserIsRejectedThemost
            FROM
                fpt_company.contract_history ch
                JOIN reason r ON r.id = ch.reason_id
                JOIN TopUserDetails tud ON ch.reason_id = tud.reason_id AND tud.rn = 1
                JOIN fpt_company.users u ON u.email = tud.topUser
            WHERE
                ch.status IN ('APPROVE_FAIL', 'SIGN_A_FAIL', 'SIGN_B_FAIL')
            GROUP BY ch.reason_id, r.title, tud.topUser, u.name
            ORDER BY totalNumberOfRejected DESC
                      
            """, nativeQuery = true)
    List<Object[]> totalContractCejected();
}

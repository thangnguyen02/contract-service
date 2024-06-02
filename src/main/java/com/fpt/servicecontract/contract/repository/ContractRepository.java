package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {

    @Query(value = """
            select    
            name, 
            created_by, 
            file,
            created_date ,
            id, 
            status
            from contract  where mark_deleted = 0
            """, nativeQuery = true)
    Page<Object[]>  findAllContract(Pageable p);

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
                    c.mark_deleted = 0
            """, nativeQuery = true)
    List<Object[]> findByIdContract(String id);
}

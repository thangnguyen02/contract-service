package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.ContractTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContractTemplateRepository extends JpaRepository<ContractTemplate, String> {
    @Query(value = """
            select id, name_contract,number_contract,rule_contract,
                   term_contract,name,address,tax_number,presenter,position,
                   business_number,bank_id,bank_name,bank_acc_ower,email, created_date, updated_date from contract_template
                   where 1=1
                   and mark_deleted = false
            """, nativeQuery = true)
    Page<Object[]> findAllContractTemplate(Pageable p);
}

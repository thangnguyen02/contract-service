package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.dto.ContractInterface;
import com.fpt.servicecontract.contract.model.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {


    @Query(value = """
            select * from contract u join contract_party partyA on u.partyaid = partyA.id join contract_party partyB on u.partybid = partyB.id
            where 1=1
            and (lower(u.contract_name) like lower(:contractName) or :contractName is null)
            and (lower(u.contract_number) like lower(:contractNumber) or :contractNumber is null)
            and (u.contract_type_id = :contractTypeId or :contractTypeId is null)
            and (u.status_id = :statusId or :statusId is null)
            and (u.created_by = :createdBy or :createdBy is null)
            and (lower(u.content) = lower(:content) or :content is null)
            and (lower(partyA.name) like lower(:namePartyA) or :namePartyA is null)
            and (lower(partyB.name ) like lower(:namePartyB) or :namePartyB is null)
    """, nativeQuery = true)
    public Page<ContractInterface> search(
            @Param("contractName") String contractName,
            @Param("contractNumber") String contractNumber,
            @Param("contractTypeId") String contractTypeId,
            @Param("statusId") String statusId,
            @Param("createdBy") String createdBy,
            @Param("content") String content,
            @Param("namePartyA") String namePartyA,
            @Param("namePartyB") String namePartyB,
            Pageable pageable
    );
}

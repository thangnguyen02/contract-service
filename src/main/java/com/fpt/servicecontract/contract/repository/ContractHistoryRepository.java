package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.ContractHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContractHistoryRepository extends JpaRepository<ContractHistory, String> {

    @Query(value = """
                select u.id, u.contract_id, u.created_by, u.created_date, u.status ,u.contract_name, u.comment from contract_history
                u where 1=1
                    and u.contract_id = :contractId
                    and ( u.created_by = :createdBy or :createdBy is null)
                    order by created_date desc
            """, nativeQuery = true)
    List<Object[]> getContractHistoriesByContractId(@Param("contractId") String contractId, @Param("createdBy") String createdBy);
}

package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.OldContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OldContractRepository extends JpaRepository<OldContract, String> {

    @Query(value = "select * from old_contract where is_deleted = 0", nativeQuery = true)
    Page<OldContract> findAll(Pageable pageable);
}

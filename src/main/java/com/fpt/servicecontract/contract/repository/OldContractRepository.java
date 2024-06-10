package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.OldContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;

@Repository
public interface OldContractRepository extends JpaRepository<OldContract, String> {

    @Query(value = "select * from old_contract where is_deleted = 0", nativeQuery = true)
    Page<OldContract> findAll(Pageable pageable);

    @Query(value = """
            SELECT
                 count(id)
                 from old_contract  where 
                 created_date between :fromDate and :toDate
                 and is_deleted = 0
                 """, nativeQuery = true)
    Integer  staticalOldContract(
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate
    );
}

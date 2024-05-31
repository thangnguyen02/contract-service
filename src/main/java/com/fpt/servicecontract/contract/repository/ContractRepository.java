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
            select name, created_by, file, created_date from contract
            """, nativeQuery = true)
    Page<Object[]>  findAllContract(Pageable p);
}

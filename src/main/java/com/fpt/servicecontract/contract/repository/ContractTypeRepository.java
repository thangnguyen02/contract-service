package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.ContractType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ContractTypeRepository  extends PagingAndSortingRepository<ContractType, String> {
    Optional<ContractType> findById(String id);

    ContractType save(ContractType contractType);

    @Query(value = "select id, title, description, mark_deleted from contract_type where lower(title) like lower(:title) or :title is null order by created_date desc", nativeQuery = true)
    Page<ContractType> findByTitle(Pageable pageable,  String title);

    void deleteById(String id);
}

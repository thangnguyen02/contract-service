package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.Reason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ReasonRepository extends PagingAndSortingRepository<Reason, String> {
    Optional<Reason> findById(String id);

    Reason save(Reason contractType);

    @Query(value = "select id, title, description, mark_deleted from reason where lower(title) like lower(:title) or :title is null", nativeQuery = true)
    Page<Reason> findByTitle(Pageable pageable,  String title);

    void deleteById(String id);
}

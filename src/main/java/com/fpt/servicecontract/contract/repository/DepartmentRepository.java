package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    Page<Department> findByMarkDeletedOrderByCreatedDateDesc(Boolean markDeleted, Pageable pageable);
}

package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    @Query(value = """
        select id, created_date, description, title, updated_date, mark_deleted
        from department
        where mark_deleted = 0
        and (lower(title) like lower(:title) or :title is null)
    """, nativeQuery = true)
    Page<Department> search(String title, Pageable pageable);
}

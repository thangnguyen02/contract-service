package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.PaySlip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaySlipRepository extends JpaRepository<PaySlip, String> {
    @Query(value = """
                    SELECT id, email, commission_percentage, total_value_contract,
                           base_salary, client_deployment_percentage, bonus_reaches_threshold, food_allowance,
                           transportation_or_phone_allowance, total_salary, created_date
                    FROM
                        pay_slip
                    WHERE
                        created_date between :fromDate and :toDate
            """, nativeQuery = true)
    Page<Object[]> getAllPaySlip(Pageable pageable, LocalDate fromDate, LocalDate toDate);
}

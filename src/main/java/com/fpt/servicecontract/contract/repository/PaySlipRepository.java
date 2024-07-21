package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.PaySlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaySlipRepository extends JpaRepository<PaySlip, String> {
    @Query(value = """
                    SELECT id, email, commissionPercentage, totalValueContract,
                           baseSalary, clientDeploymentPercentage, bonusReachesThreshold, foodAllowance,
                           transportationOrPhoneAllowance, totalSalary, createdDate,
                           DATE_FORMAT(CURDATE(), '%Y-%m-01') AS first_date,
                           LAST_DAY(CURDATE()) AS last_date
                    FROM
                        pay_slip
                    WHERE
                        created_date between first_date and last_date
            """, nativeQuery = true)
    List<Object[]> getAllPaySlip();
}

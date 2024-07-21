package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.PaySlipFormula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaySlipFormulaRepository extends JpaRepository<PaySlipFormula, String> {
}

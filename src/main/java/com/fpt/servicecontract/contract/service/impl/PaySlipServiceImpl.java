package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.auth.dto.UserInterface;
import com.fpt.servicecontract.auth.model.Permission;
import com.fpt.servicecontract.auth.repository.UserRepository;
import com.fpt.servicecontract.contract.model.PaySlip;
import com.fpt.servicecontract.contract.model.PaySlipFormula;
import com.fpt.servicecontract.contract.repository.PaySlipFormulaRepository;
import com.fpt.servicecontract.contract.repository.PaySlipRepository;
import com.fpt.servicecontract.contract.service.PaySlipService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaySlipServiceImpl implements PaySlipService {

    private final PaySlipRepository paySlipRepository;
    private final PaySlipFormulaRepository paySlipFormulaRepository;
    private final UserRepository userRepository;

    @Override
    public BaseResponse CalculateAllPaySlip() {
        List<PaySlipFormula> paySlipFormulas = paySlipFormulaRepository.findAll();
        if (paySlipFormulas.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Not have any formula to calculate paySlip", true, null);
        }
        List<String> saleEmails = userRepository.getUserWithPermissionList(Permission.SALE.name()).stream().map(UserInterface::getEmail).toList();
        List<Object[]> saleAndNumberSales = userRepository.getSaleAndNumberSales(saleEmails);
        List<PaySlip> paySlips = new ArrayList<>();
        for (Object[] saleAndNumber : saleAndNumberSales) {
            String saler = (String) saleAndNumber[0];
            Double numberSale = (Double) saleAndNumber[1];
            Optional<PaySlipFormula> formulaOptional = paySlipFormulas.stream().filter(e ->
                 numberSale >= e.getFromValueContract() && numberSale <= e.getToValueContract()
            ).findFirst();
            if (formulaOptional.isPresent()) {
                PaySlipFormula formula = formulaOptional.get();
                Double baseSalary = formula.getBaseSalary();
                Double commissionPercentage = numberSale / 100 * formula.getCommissionPercentage();
                Double clientDeploymentPercentage = numberSale / 100 * formula.getClientDeploymentPercentage();
                Double bonusReachesThreshold = formula.getBonusReachesThreshold();
                Double foodAllowance = formula.getFoodAllowance();
                Double transportationOrPhoneAllowance = formula.getTransportationOrPhoneAllowance();
                Double totalSalary = baseSalary + commissionPercentage + clientDeploymentPercentage + bonusReachesThreshold + foodAllowance + transportationOrPhoneAllowance;

                PaySlip paySlip = PaySlip.builder()
                        .baseSalary(baseSalary)
                        .totalValueContract(numberSale)
                        .email(saler)
                        .commissionPercentage(commissionPercentage)
                        .clientDeploymentPercentage(clientDeploymentPercentage)
                        .bonusReachesThreshold(bonusReachesThreshold)
                        .foodAllowance(foodAllowance)
                        .transportationOrPhoneAllowance(transportationOrPhoneAllowance)
                        .totalSalary(totalSalary)
                        .createdDate(LocalDate.now())
                        .build();
                paySlips.add(paySlip);
            }
        }
        paySlipRepository.saveAll(paySlips);
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "All paySlips of this month calculated", true, null);
    }

    @Override
    public BaseResponse GetAllPaySlip() {
        List<Object[]> paySlips = paySlipRepository.getAllPaySlip();
        List<PaySlip> paySlipList = new ArrayList<>();
        for (Object[] obj : paySlips) {
            paySlipList.add(PaySlip.builder()
                            .id(Objects.nonNull(obj[0]) ? obj[0].toString() : null)
                            .email(Objects.nonNull(obj[1]) ? obj[1].toString() : null)
                            .commissionPercentage(Objects.nonNull(obj[2]) ? Double.parseDouble(obj[2].toString()) : null)
                            .totalValueContract(Objects.nonNull(obj[3]) ? Double.parseDouble(obj[3].toString()) : null)
                            .baseSalary(Objects.nonNull(obj[4]) ? Double.parseDouble(obj[4].toString()) : null)
                    .build());
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "All paySlips of this month", true, paySlipRepository.findAll());
    }

}

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
import com.fpt.servicecontract.utils.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
        List<Object[]> saleAndNumberSalesHaveCommission = userRepository.getSaleAndNumberSales(saleEmails);
        Map<String, Double> saleAndNumberSalesAll = new HashMap<>();
        for (Object[] saleAndNumber : saleAndNumberSalesHaveCommission) {
            saleAndNumberSalesAll.put((String) saleAndNumber[0], (Double) saleAndNumber[1]);
        }
        List<PaySlip> paySlips = new ArrayList<>();

        for (String email : saleEmails) {
            Double numberSale = DataUtil.isNullObject(saleAndNumberSalesAll.get(email)) ? 0 : saleAndNumberSalesAll.get(email);
            Optional<PaySlipFormula> formulaOptional = paySlipFormulas.stream().filter(e ->
                 numberSale >= e.getFromValueContract() && numberSale < e.getToValueContract()
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
                        .email(email)
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
    public BaseResponse GetAllPaySlip(Pageable pageable, LocalDate fromDate, LocalDate toDate) {
        Page<Object[]> paySlips = paySlipRepository.getAllPaySlip(pageable, fromDate, toDate);
        if (paySlips.getTotalElements() == 0) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "No paySlips found", true, null);
        }
        List<PaySlip> paySlipList = new ArrayList<>();
        for (Object[] obj : paySlips) {
            paySlipList.add(PaySlip.builder()
                            .id(Objects.nonNull(obj[0]) ? obj[0].toString() : null)
                            .email(Objects.nonNull(obj[1]) ? obj[1].toString() : null)
                            .commissionPercentage(Objects.nonNull(obj[2]) ? Double.parseDouble(obj[2].toString()) : null)
                            .totalValueContract(Objects.nonNull(obj[3]) ? Double.parseDouble(obj[3].toString()) : null)
                            .baseSalary(Objects.nonNull(obj[4]) ? Double.parseDouble(obj[4].toString()) : null)
                            .clientDeploymentPercentage(Objects.nonNull(obj[5]) ? Double.parseDouble(obj[5].toString()) : null)
                            .bonusReachesThreshold(Objects.nonNull(obj[6]) ? Double.parseDouble(obj[6].toString()) : null)
                            .foodAllowance(Objects.nonNull(obj[7]) ? Double.parseDouble(obj[7].toString()) : null)
                            .transportationOrPhoneAllowance(Objects.nonNull(obj[8]) ? Double.parseDouble(obj[8].toString()) : null)
                            .totalSalary(Objects.nonNull(obj[9]) ? Double.parseDouble(obj[9].toString()) : null)
                            .createdDate(Objects.nonNull(obj[10]) ? LocalDate.parse(obj[10].toString()) : null)
                    .build());
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "All paySlips of this month", true, new PageImpl<>(paySlipList, pageable,
                paySlips.getTotalElements()));
    }

    @Override
    public BaseResponse GetPaySlipById(Pageable pageable, LocalDate fromDate, LocalDate toDate, String email) {
        Page<Object[]> paySlips = paySlipRepository.getPaySlipByEmail(pageable, fromDate, toDate, email);
        if (paySlips.getTotalElements() == 0) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "No paySlips found", true, null);
        }
        List<PaySlip> paySlipList = new ArrayList<>();
        for (Object[] obj : paySlips) {
            paySlipList.add(PaySlip.builder()
                    .id(Objects.nonNull(obj[0]) ? obj[0].toString() : null)
                    .email(Objects.nonNull(obj[1]) ? obj[1].toString() : null)
                    .commissionPercentage(Objects.nonNull(obj[2]) ? Double.parseDouble(obj[2].toString()) : null)
                    .totalValueContract(Objects.nonNull(obj[3]) ? Double.parseDouble(obj[3].toString()) : null)
                    .baseSalary(Objects.nonNull(obj[4]) ? Double.parseDouble(obj[4].toString()) : null)
                    .clientDeploymentPercentage(Objects.nonNull(obj[5]) ? Double.parseDouble(obj[5].toString()) : null)
                    .bonusReachesThreshold(Objects.nonNull(obj[6]) ? Double.parseDouble(obj[6].toString()) : null)
                    .foodAllowance(Objects.nonNull(obj[7]) ? Double.parseDouble(obj[7].toString()) : null)
                    .transportationOrPhoneAllowance(Objects.nonNull(obj[8]) ? Double.parseDouble(obj[8].toString()) : null)
                    .totalSalary(Objects.nonNull(obj[9]) ? Double.parseDouble(obj[9].toString()) : null)
                    .createdDate(Objects.nonNull(obj[10]) ? LocalDate.parse(obj[10].toString()) : null)
                    .build());
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "All paySlips of this month", true, new PageImpl<>(paySlipList, pageable,
                paySlips.getTotalElements()));
    }

}

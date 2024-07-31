package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.dto.StatisticalDto;
import com.fpt.servicecontract.contract.model.Department;
import com.fpt.servicecontract.contract.repository.DepartmentRepository;
import com.fpt.servicecontract.contract.service.DepartmentService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    @Override
    public BaseResponse addDepartment(Department department) {
        department.setCreatedDate(LocalDate.now());
        department.setUpdatedDate(LocalDate.now());
        department.setMarkDeleted(false);
        try {
            var result = departmentRepository.save(department);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Created successfully", true, result);
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, e.getMessage(), false, null);
        }
    }

    @Override
    public BaseResponse updateDepartment(Department department) {
        var departmentOptional = departmentRepository.findById(department.getId());
        if (departmentOptional.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.NOT_FOUND, "Department not found", true, null);
        }

        department.setUpdatedDate(LocalDate.now());
        try {
            departmentRepository.save(department);
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Updated successfully", true, department);
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, e.getMessage(), false, null);
        }
    }

    @Override
    public BaseResponse deleteDepartment(String id) {
        var departmentOptional = departmentRepository.findById(id);
        if (departmentOptional.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.NOT_FOUND, "Department not found", true, null);
        }
        departmentOptional.get().setMarkDeleted(true);
        try {
            departmentRepository.save(departmentOptional.get());
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "Deleted successfully", true, null);
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, e.getMessage(), false, null);
        }
    }

    @Override
    public BaseResponse getAllDepartments(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Get All successfully", true, departmentRepository.findByMarkDeletedOrderByCreatedDateDesc( false, pageable));
    }
}

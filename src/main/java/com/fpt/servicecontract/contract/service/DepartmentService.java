package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.model.Department;
import com.fpt.servicecontract.utils.BaseResponse;

public interface DepartmentService {
    BaseResponse addDepartment(Department department);
    BaseResponse updateDepartment(Department department);
    BaseResponse deleteDepartment(String id);
    BaseResponse getAllDepartments(int page, int size);
}

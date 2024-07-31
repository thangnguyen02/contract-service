package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.contract.model.Department;
import com.fpt.servicecontract.contract.service.DepartmentService;
import com.fpt.servicecontract.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/{page}/{size}")
    public ResponseEntity<BaseResponse> getAll(
            @PathVariable int page,
            @PathVariable int size
    ) {
        return ResponseEntity.ok(departmentService.getAllDepartments(page, size));
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> create(
            @RequestBody Department department
    ) {
        return ResponseEntity.ok(departmentService.addDepartment(department));
    }

    @PutMapping()
    public ResponseEntity<BaseResponse> update(
            @RequestBody Department department
    ) {
        return ResponseEntity.ok(departmentService.updateDepartment(department));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(
        @PathVariable String id
    ) {
        return ResponseEntity.ok(departmentService.deleteDepartment(id));
    }
}

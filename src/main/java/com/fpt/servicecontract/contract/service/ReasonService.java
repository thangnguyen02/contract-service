package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.model.Reason;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ReasonService {


    Page<Reason> getAllReasons(int page, int size, String title);

    Optional<Reason> getReasonById(String id);

    Reason createReason(Reason reason);

    Reason updateReason(String id, Reason reasonDetails);

    void deleteReason(String id);
}

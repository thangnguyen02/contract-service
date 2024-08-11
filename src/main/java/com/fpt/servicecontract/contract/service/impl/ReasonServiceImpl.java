package com.fpt.servicecontract.contract.service.impl;

import com.fpt.servicecontract.contract.model.ContractType;
import com.fpt.servicecontract.contract.model.Reason;
import com.fpt.servicecontract.contract.repository.ContractTypeRepository;
import com.fpt.servicecontract.contract.repository.ReasonRepository;
import com.fpt.servicecontract.contract.service.ContractTypeService;
import com.fpt.servicecontract.contract.service.ReasonService;
import com.fpt.servicecontract.utils.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReasonServiceImpl implements ReasonService {

    @Autowired
    private ReasonRepository reasonRepository;


    @Override
    public Page<Reason> getAllReasons(int page, int size, String title) {
        Pageable pageable = PageRequest.of(page, size);
        return reasonRepository.findByTitle(pageable, QueryUtils.appendPercent(title));
    }

    @Override
    public Optional<Reason> getReasonById(String id) {
        return reasonRepository.findById(id);
    }

    @Override
    public Reason createReason(Reason reason) {
        return reasonRepository.save(reason);
    }

    @Override
    public Reason updateReason(String id, Reason reasonDetails) {
        Optional<Reason> reasonOptional = reasonRepository.findById(id);

        if (reasonOptional.isPresent()) {
            Reason reason = reasonOptional.get();
            reason.setTitle(reasonDetails.getTitle());
            reason.setDescription(reasonDetails.getDescription());
            reason.setMarkDeleted(reasonDetails.getMarkDeleted());
            return reasonRepository.save(reason);
        } else {
            throw new RuntimeException("ContractType not found with id " + id);
        }
    }

    @Override
    public void deleteReason(String id) {
        reasonRepository.deleteById(id);
    }
}

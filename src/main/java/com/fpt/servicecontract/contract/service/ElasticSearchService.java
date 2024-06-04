package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.model.Contract;

import java.io.IOException;

public interface ElasticSearchService {
    String indexContract(Contract result) throws IOException;
}

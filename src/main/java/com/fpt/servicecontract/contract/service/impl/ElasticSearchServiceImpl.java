package com.fpt.servicecontract.contract.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.fpt.servicecontract.contract.model.Contract;
import com.fpt.servicecontract.contract.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

    @Autowired
    private ElasticsearchClient esClient;

    @Override
    public String indexContract(Contract contract) throws IOException {
        IndexRequest<Contract> request = IndexRequest.of(i -> i
                .index("contract")
                .id(contract.getId())
                .document(contract)
        );

        IndexResponse response = esClient.index(request);
        return response.id();
    }
}

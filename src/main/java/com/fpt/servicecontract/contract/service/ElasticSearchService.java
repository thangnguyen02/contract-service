package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.dto.ContractRequest;
import com.fpt.servicecontract.contract.dto.SearchRequestBody;
import com.fpt.servicecontract.contract.model.Contract;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public interface ElasticSearchService {

    <T> String indexDocument(String indexName, T document, Function<T, String> idExtractor) throws IOException;

    <T> Page<T> search(String indexName, SearchRequestBody keyword, Class<T> clazz) throws IOException;

    String deleteDocumentById(String indexName, String documentId) throws IOException;

}

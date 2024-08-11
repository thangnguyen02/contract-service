package com.fpt.servicecontract.contract.service;

import com.fpt.servicecontract.contract.dto.request.SearchRequestBody;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.function.Function;

public interface ElasticSearchService {

    <T> String indexDocument(String indexName, T document, Function<T, String> idExtractor) throws IOException;

    <T> Page<T> search(String indexName, SearchRequestBody keyword, Class<T> clazz) throws IOException;

    String deleteDocumentById(String indexName, String documentId) throws IOException;

    <T> T getDocumentById(String indexName, String documentId, Class<T> clazz) throws IOException;
}

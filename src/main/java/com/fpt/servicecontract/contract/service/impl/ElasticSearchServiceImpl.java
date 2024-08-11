package com.fpt.servicecontract.contract.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.fpt.servicecontract.contract.dto.request.SearchRequestBody;
import com.fpt.servicecontract.contract.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

    @Autowired
    private ElasticsearchClient esClient;

    public <T> String indexDocument(String indexName, T document, Function<T, String> idExtractor) throws IOException {
        IndexRequest<T> request = IndexRequest.of(i -> i
                .index(indexName)
                .id(idExtractor.apply(document))
                .document(document)
        );

        IndexResponse response = esClient.index(request);
        return response.id();
    }

    public <T> Page<T> search(String indexName, SearchRequestBody searchRequestBody, Class<T> clazz) throws IOException {
        Query multiMatchQuery = Query.of(q -> q
                .multiMatch(MultiMatchQuery.of(m -> m
                        .query(searchRequestBody.getKey())
//                        .fuzziness("AUTO")
                ))
        );
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(indexName)
                .query(multiMatchQuery)
                .from(searchRequestBody.getPage() * searchRequestBody.getSize())
                .size(searchRequestBody.getSize())
        );

        SearchResponse<T> searchResponse = esClient.search(searchRequest, clazz);

        List<T> lst = searchResponse.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

        long totalHits = searchResponse.hits().total().value();

        return new PageImpl<>(lst, PageRequest.of(searchRequestBody.getPage(), searchRequestBody.getSize()), totalHits);
    }
    public String deleteDocumentById(String indexName, String documentId) throws IOException {
        DeleteRequest request = DeleteRequest.of(d -> d
                .index(indexName)
                .id(documentId)
        );
        DeleteResponse response = esClient.delete(request);
        return response.result().name();
    }

}

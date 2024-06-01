package com.fpt.servicecontract.contract.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cluster.HealthRequest;
import co.elastic.clients.elasticsearch.cluster.HealthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/elasticsearch")
@RequiredArgsConstructor
public class ElasticsearchController {

    private final ElasticsearchClient esClient;

    @GetMapping("/test-connection")
    public ResponseEntity<String> testConnection() {
        try {
            HealthResponse healthResponse = esClient.cluster().health(new HealthRequest.Builder().build());

            return ResponseEntity.ok("Elasticsearch connection test successful. Cluster health status: " + healthResponse.status());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Elasticsearch connection test failed: " + e.getMessage());
        }
    }
}

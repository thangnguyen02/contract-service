package com.fpt.servicecontract.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
@Configuration
public class ElasticSearchConfig {

    @Value("${elasticsearch.cloud.endpoint}")
    private String cloudEndpoint;

    @Value("${elasticsearch.cloud.username}")
    private String username;

    @Value("${elasticsearch.cloud.password}")
    private String password;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(HttpHost.create(cloudEndpoint))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider())
                        .setDefaultIOReactorConfig(IOReactorConfig.custom()
                                .setIoThreadCount(1)
                                .build())
                        .setMaxConnTotal(100)
                        .setMaxConnPerRoute(100));

        RestClient restClient = builder.build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }

    private BasicCredentialsProvider credentialsProvider() {
        BasicCredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        return provider;
    }
}
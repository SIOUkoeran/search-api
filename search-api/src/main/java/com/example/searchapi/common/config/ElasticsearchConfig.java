package com.example.searchapi.common.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = {"com.example.searchapi.poi.repository",
    "com.example.searchapi.category.repository", "com.example.searchapi.address.repository"})
@Slf4j
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {


    @Value("${esHost}")
    private String[] esHosts;


    @Override
    public RestHighLevelClient elasticsearchClient() {

        ClientConfiguration clientConfiguration =
            ClientConfiguration.builder()
                .connectedTo(esHosts)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}

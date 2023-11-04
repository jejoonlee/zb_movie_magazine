package com.jejoonlee.movmag.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticConfig extends AbstractElasticsearchConfiguration {

    @Value("${spring.elastic.url}")
    private String elasticUrl;


    @Override
    public RestHighLevelClient elasticsearchClient() {

        ClientConfiguration clientConfiguration =
                ClientConfiguration.builder()
                        .connectedTo(elasticUrl)
                        .withConnectTimeout(10000)
                        .withSocketTimeout(100000)
                        .build();

        return RestClients.create(clientConfiguration).rest();
    }
}

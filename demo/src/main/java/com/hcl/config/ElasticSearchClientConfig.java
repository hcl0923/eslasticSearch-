package com.hcl.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: hcl-es-api
 * @description:
 * @author: 作者
 * @create: 2022-02-07 11:14
 */

@Configuration
public class ElasticSearchClientConfig {
    //spring <beans id="" class=""
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("localhost",9200,"http")));
        return client;
    }
}

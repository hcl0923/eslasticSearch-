package com.hcl;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class HclEsJdApplicationTests {
    @Autowired
    private RestHighLevelClient client;

    @Test
    void contextLoads() throws IOException {
        GetRequest getRequest=new GetRequest("kuang_index","1");
        //不获取返回的_source的上下文
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists=client.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

}

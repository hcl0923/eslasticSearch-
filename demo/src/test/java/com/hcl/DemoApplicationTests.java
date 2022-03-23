package com.hcl;

import com.alibaba.fastjson.JSON;
import com.hcl.pojo.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    @Test
    void contextLoads() {

    }

    @Test
    void testCreateIndex() throws IOException {
        //1.创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("kuang_index");
//        2.客户端执行请求 获取响应
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }
    //获取索引，判断是否存在
    @Test
    void testExistIndex() throws IOException {
        //1.创建索引请求
        GetIndexRequest request = new GetIndexRequest("kuang_index2");
//        2.客户端执行请求 获取响应
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }
    //获取索引，判断是否存在
    @Test
    void testDeleteIndex() throws IOException {
        //1.创建索引请求
        DeleteIndexRequest request = new DeleteIndexRequest("kuang_index");
//        2.客户端执行请求 获取响应
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete);
    }
    //添加文档
    @Test
    void testAddDocument() throws IOException {
        //创建对象
        User user=new User("狂胜说",3);
        //创建请求
        IndexRequest request = new IndexRequest("kuang_index");
        //规则put /kuang_index/1
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        //将我们的数据放入请求 json
        request.source(JSON.toJSONString(user), XContentType.JSON);
        //客户端发送请求
        IndexResponse indexResponse=client.index(request,RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status());
    }
    //判断是否存在文档
    @Test
    void testIsExist() throws IOException {
        GetRequest getRequest=new GetRequest("kuang_index","1");
        //不获取返回的_source的上下文
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists=client.exists(getRequest,RequestOptions.DEFAULT);
        System.out.println(exists);
    }
    //获取文档信息
    @Test
    void testGetDocument() throws IOException {
        GetRequest getRequest=new GetRequest("kuang_index","1");
        GetResponse response=client.get(getRequest,RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
        System.out.println(response);
    }
    //更新文档信息
    @Test
    void testUpdateDocument() throws IOException {
        UpdateRequest updateRequest=new UpdateRequest("kuang_index","1");
        updateRequest.timeout("1s");
        User user=new User("狂神说JAVA",18);
        updateRequest.doc(JSON.toJSONString(user),XContentType.JSON);
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());
    }
    //删除文档信息
    @Test
    void testDeleteRequest() throws IOException {
        DeleteRequest request=new DeleteRequest("kuang_index","1");;
        request.timeout("1s");
        DeleteResponse response=client.delete(request,RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    //大批量数据
    @Test
    void testBulkRequest() throws IOException {
        BulkRequest request=new BulkRequest("kuang_index");
        request.timeout("10s");
        ArrayList<User> arrayList=new ArrayList<>();
        arrayList.add(new User("kuangshen1",1));
        arrayList.add(new User("kuangshen2",2));
        arrayList.add(new User("kuangshen3",3));
        arrayList.add(new User("kuangshen4",4));
        for(int i=0;i<arrayList.size();i++){
            request.add(
                    new IndexRequest("kuang_index").id(""+(i+1)).source(JSON.toJSONString(arrayList.get(i)),XContentType.JSON)
            );
        }
        BulkResponse response=client.bulk(request,RequestOptions.DEFAULT);
        System.out.println(response.hasFailures());//返回false代表成功
    }

    //查询
    //SearchRequest搜索请求
    //SearchSourceBuiler条件构造
    //HighlightBuiler构建高亮
    //TermQueryBuiler精确查询
    //MatchALlQueryBuiler
    //xxxQueryBuilder对应我们刚才看到的所有命令
    @Test
    void testSearch() throws IOException {
        SearchRequest request=new SearchRequest("kuang_index");
        //构建搜索条件
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.highlighter();
//      查询条件，我们可以使用QueryBuilers工具来实现
        //TermQuery精确
        TermQueryBuilder termQueryBuilder=QueryBuilders.termQuery("name","kuangshen1");
//        MatchAllQueryBuilder matchAllQueryBuilder=QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(termQueryBuilder);
//        searchSourceBuilder.from();
//        searchSourceBuilder.size();
        searchSourceBuilder.timeout(new TimeValue(30, TimeUnit.SECONDS));
        request.source(searchSourceBuilder);
        SearchResponse response=client.search(request,RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response.getHits()));
        System.out.println("=================");
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
            System.out.println(hit.getHighlightFields());
        }
    }
}

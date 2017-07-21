//
//package com.cds.learn.es;
///**
//* Created by zhanglei11 on 2016/7/20.
//*/
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
//import org.elasticsearch.action.bulk.BulkItemResponse;
//import org.elasticsearch.action.bulk.BulkRequestBuilder;
//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.action.delete.DeleteRequest;
//import org.elasticsearch.action.delete.DeleteRequestBuilder;
//import org.elasticsearch.action.delete.DeleteResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.search.SearchRequestBuilder;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.Requests;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.common.transport.TransportAddress;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.search.SearchHits;
//import org.elasticsearch.search.aggregations.AggregationBuilder;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.elasticsearch.search.sort.SortOrder;
//
//import java.io.IOException;
//import java.net.InetAddress;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//
///**
//* <p>
//* ES帮助类
//* </p>
//*
//* @author zhanglei11 2016年07月20日 13:46
//* @version V1.0
//* @modificationHistory=========================逻辑或功能性重大变更记录
//* @modify by user: $author$ $date$
//* @modify by reason: {方法名}:{原因}
//*/
//public class ESHelper {
//
//    private static final Logger LOG = LogManager.getLogger(ESHelper.class);
//
//    // ES客户端
//    private static Client ESClient = null;
//
//    static {
//
//        String esClusterName = null;
//        String esClusterAddress = null;
//
//        try{
//            if(StringUtils.isNotEmpty(esClusterName) && StringUtils.isNotEmpty(esClusterAddress)){
//                initESClient(esClusterName, esClusterAddress);
//            }else{
//                LOG.warn("Could not instance client of Elasticsearch. Parameters are => cluster address:{}, cluster name:{}",
//                        esClusterAddress, esClusterName);
//            }
//        }catch (Exception e){
//            LOG.error("Could not instance ESClient.", e);
//        }
//    }
//
//    /**
//     * 批量建立ES索引
//     *
//     * @param list
//     * @return
//     * @author zhanglei11 2015年7月9日 上午11:13:13
//     */
//    public static boolean addIndex(String indexName, String typeName, List<Map<String, Object>> list) {
//        long t = System.currentTimeMillis();
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            BulkRequestBuilder bulkRequest = ESClient.prepareBulk();
//            for(Map<String, Object> data : list){
//                byte[] json = mapper.writeValueAsBytes(data);
//                bulkRequest.add(new IndexRequest(indexName, typeName).source(json));
//            }
//
//            BulkResponse response = bulkRequest.execute().actionGet();
//            if(response.hasFailures()){
//                BulkItemResponse[] itemResponses = response.getItems();
//                for(BulkItemResponse itemResponse : itemResponses){
//                    // TODO Must do something to handle failures.
//                    LOG.error("Add ES Index failed! DOC_ID: {}, Reason: {}", itemResponse.getId(), itemResponse.getFailureMessage());
//                }
//            }
//        } catch (JsonProcessingException e) {
//            LOG.error("Build index fail.", e);
//            return false;
//        }
//        LOG.debug("build index complete,num:{}, cost:{}", list.size(), System.currentTimeMillis() - t);
//        return true;
//    }
//
//    /**
//     * 批量删除ES索引
//     *
//     * @param docIds
//     *
//     *
//     */
//    public static void deleteIndex(String indexName, String typeName, List<String> docIds){
//        BulkRequestBuilder bulkRequest = ESClient.prepareBulk();
//        for(String docId : docIds){
//            bulkRequest.add(new DeleteRequest(indexName, typeName, docId));
//        }
//        BulkResponse response = bulkRequest.execute().actionGet();
//        if(response.hasFailures()){
//            BulkItemResponse[] itemResponses = response.getItems();
//            for(BulkItemResponse itemResponse : itemResponses){
//                // TODO Must do something to handle failures.
//                LOG.error("ES Index delete failed! DOC_ID: {}, Reason: {}", itemResponse.getId(), itemResponse.getFailureMessage());
//            }
//        }
//    }
//
//    /**
//     * 删除ES索引
//     *
//     * @param indexName
//     * @param typeName
//     * @param data
//     * @return
//     */
//    public static boolean deleteIndex(String indexName, String typeName, Map<String, Object> data){
//        DeleteRequestBuilder requestBuilder = ESClient.prepareDelete(indexName, typeName,
//                (String) data.get("rowKey"));
//        DeleteResponse response = requestBuilder.execute().actionGet();
//        if(!response.isFound()){
//            LOG.error("ES Index not found! DOC_ID: {}", response.getId());
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 从ES查询数据
//     *
//     * @param query
//     * @return
//     *
//     */
//    public static SearchHits queryWithES(SearchRequestBuilder query){
//        SearchHits response = query.execute().actionGet().getHits();
//        return response;
//    }
//
//    /**
//     * 聚合查询
//     *
//     * @param queryBuilder
//     * @param aggs
//     * @param indices
//     * @return
//     */
//    public static SearchResponse aggregationWithES(QueryBuilder queryBuilder, AggregationBuilder aggs, String... indices){
//        return ESClient.prepareSearch(indices).setQuery(queryBuilder).addAggregation(aggs).execute().actionGet();
//    }
//
//    /**
//     * 构造查询对象
//     *
//     * @param index
//     * @param type
//     * @param queryBuilder
//     * @param retField
//     * @param sortField
//     * @param start
//     * @param rows
//     * @return
//     */
//    public static SearchRequestBuilder buildSearch(String[] index, String type, QueryBuilder queryBuilder, String retField, String sortField, SortOrder sortOrder, int start, int rows){
//
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(queryBuilder).from(start).size(rows);
//
//        if(StringUtils.isNotEmpty(retField)){
//            searchSourceBuilder.field(retField);
//        }
//
//        if(StringUtils.isNotEmpty(sortField)){
//            searchSourceBuilder.sort(sortField, sortOrder);
//        }
//
//        LOG.debug("ES Query string: " + searchSourceBuilder.toString());
//
//        return ESClient.prepareSearch().setIndices(index).setTypes(type)
//                .setExtraSource(searchSourceBuilder.buildAsBytes(Requests.CONTENT_TYPE));
//    }
//
//    /**
//     * @param index
//     * @param type
//     * @param searchSourceBuilder
//     * @return
//     */
//    public static SearchRequestBuilder buildSearch(String[] index, String type, SearchSourceBuilder searchSourceBuilder){
//        return ESClient.prepareSearch().setIndices(index).setTypes(type)
//                .setExtraSource(searchSourceBuilder.buildAsBytes(Requests.CONTENT_TYPE));
//    }
//
//    /**
//     * 统计数据量
//     *
//     * @return 符合条件的数据量
//     */
//    public static long countWithQuery(String[] indexName, String typeName, QueryBuilder queryBuilder){
//        SearchRequestBuilder builder = ESClient.prepareSearch(indexName).setTypes(typeName)
//                .setQuery(queryBuilder).setFrom(0).setSize(0);
//        return countWithQuery(builder);
//    }
//
//    /**
//     * 统计数据量
//     *
//     * @param query
//     * @return
//     *
//     */
//    public static long countWithQuery(SearchRequestBuilder query){
//        return query.execute().actionGet().getHits().getTotalHits();
//    }
//
//    /**
//     * 获取ES客户端
//     *
//     * @return
//     */
//    public static Client getESClient(String esClusterName, String esClusterAddress){
//        if(ESClient == null){
//            synchronized (ESHelper.class){
//                if(ESClient == null){
//                    initESClient(esClusterName, esClusterAddress);
//                }
//            }
//        }
//        return ESClient;
//    }
//
//    /**
//     * 初始化ES客户端
//     *
//     * @return
//     */
//    private static Client initESClient(String esClusterName, String esClusterAddress){
//        int esClientTimeout = 180000;
//        try{
//            String[] hostPair = esClusterAddress.split(":");
//            TransportAddress[] addrs = new TransportAddress[hostPair.length];
//
//            int i = 0;
//            String[] keyValuePair;
//            for (String t : hostPair) {
//                keyValuePair = t.split(":");
//                if (2 != keyValuePair.length) {
//                    throw new IOException("ES's host is not correct:" + Arrays.toString(keyValuePair));
//                }
//                addrs[i] = new InetSocketTransportAddress(InetAddress.getByName(keyValuePair[0]), Integer.valueOf(keyValuePair[1]));
//                i++;
//            }
//
//            Settings settings = Settings.settingsBuilder()
//                    .put("cluster.name", esClusterName)
//                    .put("client.transport.sniff", true)
//                    .put("client.transport.ping_timeout", esClientTimeout + "s").build();
//
//            ESClient = TransportClient.builder().settings(settings).build().addTransportAddresses(addrs);
//            return ESClient;
//        }catch(Exception e){
//            LOG.error("Address error!", e);
//        }
//        return null;
//    }
//
//    /**
//     * 获取ES集群索引列表
//     *
//     * @return
//     */
//    public static List<String> getIndicesOfCluster(){
//        List<String> indicesOfCluster = null;
//        try {
//            // TODO Indices must be in cache to avoid get everyone time
//            String[] indices = ESClient.admin().indices().getIndex(new GetIndexRequest()).get().indices();
//            if(indices != null && indices.length > 0){
//                indicesOfCluster = Arrays.asList(indices);
//            }
//        } catch (InterruptedException e) {
//            LOG.error("Interrupted Exception while list indices of elasticsearch cluster.", e);
//        } catch (ExecutionException e) {
//            LOG.error("Execution exception while list indices of elasticsearch cluster.", e);
//        }
//
//        return indicesOfCluster;
//    }
//
////    /**
////     * <p>人脸碰撞查询语句分组</p>
////     *
////     * @param rowKey
////     * @param totalCount 数据总量
////     * @param batchSize Solr查询规模
////     * @param start 数据起始位置
////     * @return
////     */
////    public static List<SearchRequestBuilder> splitFaceSimilarQuery(String rowKey, long totalCount, int batchSize, int start){
////        List<SearchRequestBuilder> queries = new ArrayList<>();
////        if(totalCount == 0){
////            // Do nothing
////        }else if(totalCount <= batchSize){
////            SearchRequestBuilder copy = createRowKeyQuery(rowKey);
////            copy.setFrom(start);
////            copy.setSize((int) totalCount);
////            queries.add(copy);
////        }else{
////            int taskNum = (int)(totalCount / batchSize);
////            if(totalCount % batchSize != 0){
////                taskNum += 1;
////            }
////            for (int i = 0;i < taskNum;i++) {
////                SearchRequestBuilder copy = createRowKeyQuery(rowKey);
////                copy.setFrom(i * batchSize);
////                copy.setSize(batchSize);
////                queries.add(copy);
////            }
////        }
////        return queries;
////    }
//}

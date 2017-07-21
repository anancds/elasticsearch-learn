package com.cds.learn.es;

import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * <p></p>
 *
 * @author chendongsheng5 2017/1/4 16:21
 * @version V1.0
 * @modificationHistory =========================逻辑或功能性重大变更记录
 * @modify by user: chendongsheng5 2017/1/4 16:21
 * @modify by reason:{方法名}:{原因}
 */
@SuppressWarnings("unchecked") public class ESTest {

	private static Client ESClient = null;

	public static List<String> getIndicesOfCluster(){
		List<String> indicesOfCluster = null;
		try {
			// TODO Indices must be in cache to avoid get everyone time
			String[] indices = ESClient.admin().indices().getIndex(new GetIndexRequest()).get().indices();
			if(indices != null && indices.length > 0){
				indicesOfCluster = Arrays.asList(indices);
			}
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}

		return indicesOfCluster;
	}

	public static void main(String[] args) throws UnknownHostException {

		int esClientTimeout = 180000;

		Settings settings = Settings.settingsBuilder()
				.put("cluster.name", "SERVICE-ELASTICSEARCH-ebea7a35c00a463390644af5d17f764c")
				.put("client.transport.sniff", true).put("client.transport.ping_timeout", esClientTimeout + "s").build();
		TransportAddress[] addrs = new TransportAddress[1];
		addrs[0] = new InetSocketTransportAddress(InetAddress.getByName("10.65.214.213"), 9300);
		ESClient = TransportClient.builder().settings(settings).build().addTransportAddresses(addrs);
//				TransportClient client = new PreBuiltTransportClient(settings)
//						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.33.25.79"), 9200));


		for (int i = 0; i< 100000; i++) {
			String json = ESUtils.toJson(new LogModel());
//		System.out.println(json);

			IndexResponse response = ESClient.prepareIndex("abc", "abc")
					//必须为对象单独指定ID
					.setSource(json).execute().actionGet();
			//多次index这个版本号会变
//			System.out.println("response.version():" + response.getId());
		}
		SearchResponse response1 = ESClient.prepareSearch("abc")
				.setTypes("abc")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.termQuery("systemName", "oa"))
				.execute()
				.actionGet();
		System.out.println(response1.getHits().getTotalHits());

//		SearchResponse response2 = ESClient.prepareSearch().execute().actionGet();
//		System.out.println(response2.getHits());
		ESClient.close();
	}
}

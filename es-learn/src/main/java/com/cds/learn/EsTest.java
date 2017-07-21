//package com.cds.learn;
//
//import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
//
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.Date;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.common.transport.TransportAddress;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
//
//public class EsTest {
//
//  public static void main(String[] args) throws IOException {
//
//    int esClientTimeout = 180000;
//    Settings settings = Settings.builder()
//        .put("cluster.name", "my-application")
//        .put("client.transport.sniff", true)
//        .put("xpack.security.user", "elastic:changeme")
//        .put("client.transport.ping_timeout", esClientTimeout + "s").build();
//
//    TransportAddress[] addrs = new TransportAddress[1];
//    addrs[0] = new InetSocketTransportAddress(InetAddress.getByName("10.65.223.236"), 9300);
//
////    TransportClient client = new PreBuiltTransportClient(settings).addTransportAddresses(addrs);
//    TransportClient client = new PreBuiltXPackTransportClient(settings).addTransportAddresses(addrs);
//
//
//    for (int i = 2; i < 100; i++) {
//
//      IndexResponse response = client.prepareIndex("twitter", "tweet", String.valueOf(i))
//          .setSource(jsonBuilder()
//              .startObject()
//              .field("user", "kimchy")
//              .field("postDate", new Date())
//              .field("message", "trying out Elasticsearch")
//              .endObject()
//          )
//          .get();
//    }
//
//    client.close();
//  }
//}

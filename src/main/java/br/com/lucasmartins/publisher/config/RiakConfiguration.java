package br.com.lucasmartins.publisher.config;

import java.net.UnknownHostException;

import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakNode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RiakConfiguration {
    
    @Value("${riak.remote-address:172.17.0.2}")
    private String remoteAddress;

    @Value("${riak.remote-port:8098}")
    private Integer remotePort;

    // This will create a client object that we can use to interact with Riak
    public RiakCluster setUpCluster() throws UnknownHostException {
        // This example will use only one node listening on localhost:10017
        RiakNode node = new RiakNode.Builder()
                .withRemoteAddress(remoteAddress)
                .withRemotePort(remotePort)
                .build();

        // This cluster object takes our one node as an argument
        RiakCluster cluster = new RiakCluster.Builder(node)
                .build();

        // The cluster must be started to work, otherwise you will see errors
        cluster.start();

        return cluster;
    }

}
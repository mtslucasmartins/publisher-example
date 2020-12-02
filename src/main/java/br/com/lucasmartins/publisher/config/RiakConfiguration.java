package br.com.lucasmartins.publisher.config;

import java.net.UnknownHostException;

import com.basho.riak.client.api.RiakClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RiakConfiguration {
    
    @Value("${riak.remote-address:172.17.0.2}")
    private String remoteAddress;

    @Value("${riak.remote-port:8098}")
    private Integer remotePort;

    public RiakClient setUpClient() throws UnknownHostException {
        return RiakClient.newClient(remoteAddress);
    }

}

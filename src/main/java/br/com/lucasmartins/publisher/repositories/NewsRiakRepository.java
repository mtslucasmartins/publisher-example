package br.com.lucasmartins.publisher.repositories;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.RiakObject;
import com.basho.riak.client.core.util.BinaryValue;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Repository;

import br.com.lucasmartins.publisher.config.RiakConfiguration;
import br.com.lucasmartins.publisher.domain.models.News;

@Repository
public class NewsRiakRepository {

    private RiakConfiguration configuration;
    
    public NewsRiakRepository(RiakConfiguration configuration) {
        this.configuration = configuration;
    }

    public void fetch(String externalId) throws Exception {
        RiakClient client = new RiakClient(configuration.setUpCluster());
        
        Namespace quotesBucket = new Namespace("news");

        Location quoteObjectLocation = new Location(quotesBucket, externalId);

         // Now we can verify that the object has been stored properly by
        // creating and executing a FetchValue operation
        FetchValue fetchOp = new FetchValue.Builder(quoteObjectLocation)
                .build();

        RiakObject fetchedObject = client.execute(fetchOp).getValue(RiakObject.class);

        fetchedObject.getValue().toStringUtf8();
        System.out.println("Success! The object we created and the object we fetched have the same value");
        System.out.println("" + fetchedObject.getValue().toStringUtf8());
    }

    public void store(News news) throws Exception {
        RiakClient client = new RiakClient(configuration.setUpCluster());

        RiakObject quoteObject = new RiakObject()
            // We tell Riak that we're storing plaintext, not JSON, HTML, etc.
            // .setContentType("application/json")
            // .setCharset("utf-8")
            // Objects are ultimately stored as binaries
            .setValue(BinaryValue.create(serialize(news)));

        Namespace quotesBucket = new Namespace("news");

        Location quoteObjectLocation = new Location(quotesBucket, news.getExternalId());
        System.out.println("Location object created for quote object");

        // With our RiakObject in hand, we can create a StoreValue operation
        StoreValue storeOp = new StoreValue.Builder(quoteObject)
                .withLocation(quoteObjectLocation)
                .build();
        System.out.println("StoreValue operation created");

        StoreValue.Response storeOpResp = client.execute(storeOp);
        System.out.println("Object storage operation successfully completed");  
    }

    private String serialize(News news) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(news);
    }

}
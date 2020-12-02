package br.com.lucasmartins.publisher.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.commands.indexes.IntIndexQuery;
import com.basho.riak.client.api.commands.kv.DeleteValue;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.RiakObject;
import com.basho.riak.client.core.query.indexes.LongIntIndex;
import com.basho.riak.client.core.query.indexes.StringBinIndex;
import com.basho.riak.client.core.util.BinaryValue;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Repository;

import br.com.lucasmartins.publisher.config.RiakConfiguration;
import br.com.lucasmartins.publisher.domain.models.News;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class NewsRiakRepository {

    private static final String BUCKET_TYPE = "noticias";
    private static final String BUCKET_NAME = "ultimas_noticias";

    private final Namespace NAMESPACE = new Namespace(BUCKET_NAME);

    private RiakClient client;

    public NewsRiakRepository(RiakConfiguration configuration) throws Exception {
        this.client = configuration.setUpClient();
    }

    private Set<String> listAllKeys() throws Exception {
        Location allKeysObjLocation = new Location(NAMESPACE, "all_keys");

        FetchValue fetchOperation = new FetchValue.Builder(allKeysObjLocation).build();

        RiakObject object = client.execute(fetchOperation).getValue(RiakObject.class);

        return object == null ? new HashSet<>() : deserialize(object.getValue().toStringUtf8(), Set.class);
    }

    private void appendToAllKeys(String key) throws Exception {
        Set<String> keys = listAllKeys();

        keys.add(key);

        RiakObject object = new RiakObject()
            .setContentType("application/json")
            .setCharset("UTF-8")
            .setValue(BinaryValue.create(serialize(keys)));
        
        log.info("");
        log.info("Creating location object...");
        Location objectLocation = new Location(NAMESPACE, "all_keys");
        log.info("Location object created!");

        log.info("");
        log.info("Creating StoreValue operation object...");
        StoreValue storeOperation = new StoreValue.Builder(object)
                .withLocation(objectLocation)
                .build();
        log.info("StoreValue operation object created!");

        log.info("");
        log.info("executing object store operation...");
        StoreValue.Response storeOperationResponse = client.execute(storeOperation);
        log.info("Object storage operation successfully completed!");
        // storeOperationResponse.getValue(Set.class);
    } 

    public News findByKey(String key) throws Exception {        
        Location quoteObjectLocation = new Location(NAMESPACE, key);

        // Now we can verify that the object has been stored properly by
        // creating and executing a FetchValue operation
        FetchValue fetchOp = new FetchValue.Builder(quoteObjectLocation).build();

        RiakObject fetchedObject = client.execute(fetchOp).getValue(RiakObject.class);

        fetchedObject.getValue().toStringUtf8();
        System.out.println("Success! The object we created and the object we fetched have the same value");
        System.out.println("" + fetchedObject.getValue().toStringUtf8());

        return deserialize(fetchedObject.getValue().toStringUtf8());
    }

    public void deleteByKey(String key) throws Exception {        
        Location quoteObjectLocation = new Location(NAMESPACE, key);

        DeleteValue fetchOp = new DeleteValue.Builder(quoteObjectLocation).build();

        client.execute(fetchOp);
    }

    public List<News> fetch() throws Exception {
        List<News> news = new ArrayList<>();
        Set<String> keys = listAllKeys();

        for (String entry : keys) {
            news.add(findByKey(entry));
        }

        return news;
    }

    public News store(News news) throws Exception {
        String key = news.getExternalId();

        RiakObject object = new RiakObject()
            .setContentType("application/json")
            .setCharset("UTF-8")
            .setValue(BinaryValue.create(serialize(news)));

        log.info("");
        log.info("Creating indexes for object...");
        object.getIndexes() 
              .getIndex(LongIntIndex.named("creation"))
              .add(new Date().getTime());
        object.getIndexes() 
              .getIndex(StringBinIndex.named("external_id"))
              .add(news.getExternalId());
        log.info("Indexes successfully created!");

        log.info("");
        log.info("Creating location object...");
        Location objectLocation = new Location(NAMESPACE, key);
        log.info("Location object created!");

        log.info("");
        log.info("Creating StoreValue operation object...");
        StoreValue storeOperation = new StoreValue.Builder(object)
                .withLocation(objectLocation)
                .build();
        log.info("StoreValue operation object created!");

        log.info("");
        log.info("executing object store operation...");
        StoreValue.Response storeOperationResponse = client.execute(storeOperation);
        log.info("Object storage operation successfully completed!");

        this.appendToAllKeys(key);

        return storeOperationResponse.getValue(News.class);
    }

    private <T> String serialize(T t) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(t);
    }

    private <T> T deserialize(String value, Class<T> clazz) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(value, clazz);
    }

    private String serialize(News news) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(news);
    }

    private News deserialize(String news) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(news, News.class);
    }

}
package io.praecepta.rules.hub.nosqlbased.dao;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.Properties;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;

public class PraeceptaMongoDbFactory  {
	
    public static MongoCollection<Document>  getRuleGroupMongoDb(Properties properties){
        String ip = (String) properties.get("mongodb.ip");
        int port = Integer.valueOf((String)properties.get("mongodb.port"));

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        ConnectionString connectionString = new ConnectionString("mongodb://"+ ip + ":"+ port);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();


        MongoClient mongoClient = MongoClients.create(clientSettings);
        MongoDatabase database = mongoClient.getDatabase("percepta");
        database.withCodecRegistry(pojoCodecRegistry);
        return database.getCollection("rulegroup", Document.class);
    }


    public static MongoCollection<PraeceptaSideCarsInfo>  getSidecarMongoDb(Properties properties){
        String ip = (String) properties.get("mongodb.ip");
        int port = Integer.valueOf((String)properties.get("mongodb.port"));

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        ConnectionString connectionString = new ConnectionString("mongodb://"+ ip + ":"+ port);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();

        MongoClient mongoClient = MongoClients.create(clientSettings);
        MongoDatabase database = mongoClient.getDatabase("percepta");
        database.withCodecRegistry(pojoCodecRegistry);
        return database.getCollection("sidecar", PraeceptaSideCarsInfo.class);
    }

    public static MongoCollection<PraeceptaRuleSpace> getRuleSpaceMongoDb(Properties properties){
        String ip = (String) properties.get("mongodb.ip");
        int port = Integer.valueOf((String)properties.get("mongodb.port"));


        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        ConnectionString connectionString = new ConnectionString("mongodb://"+ ip + ":"+ port);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();

        MongoClient mongoClient = MongoClients.create(clientSettings);

        MongoDatabase database = mongoClient.getDatabase("percepta");
        database.withCodecRegistry(pojoCodecRegistry);
        return database.getCollection("rulespace", PraeceptaRuleSpace.class);
    }

    public static Document prepareRuleSpaceSearchQuery(PraeceptaRuleSpaceCompositeKey key){
        Document searchQuery = new Document();
        searchQuery.put("ruleSpaceKey.spaceName", key.getSpaceName());
        searchQuery.put("ruleSpaceKey.clientId", key.getClientId());
        searchQuery.put("ruleSpaceKey.appName", key.getAppName());
        if(key.getVersion() != null)
            searchQuery.put("ruleSpaceKey.version", key.getVersion());
        searchQuery.put("active", true);
        return searchQuery;
    }

    public static Document prepareDefaultSearchQuery(){
        Document searchQuery = new Document();

        searchQuery.put("active", true);
        return searchQuery;
    }

    public static Document prepareSidecarSearchQuery(PraeceptaRuleSpaceCompositeKey key){
        Document searchQuery = new Document();
        searchQuery.put("ruleSpaceKey.spaceName", key.getSpaceName());
        searchQuery.put("ruleSpaceKey.clientId", key.getClientId());
        searchQuery.put("ruleSpaceKey.appName", key.getAppName());
        if(key.getVersion() != null)
            searchQuery.put("ruleSpaceKey.version", key.getVersion());
        searchQuery.put("active", true);
        return searchQuery;
    }
    public static Document prepareRuleGroupSearchQuery(PraeceptaRuleSpaceCompositeKey key){
        Document searchQuery = new Document();
        searchQuery.put("ruleSpaceKey.spaceName", key.getSpaceName());
        searchQuery.put("ruleSpaceKey.clientId", key.getClientId());
        searchQuery.put("ruleSpaceKey.appName", key.getAppName());
        if(key.getVersion() != null)
            searchQuery.put("ruleSpaceKey.version", key.getVersion());
        searchQuery.put("active", true);
        return searchQuery;
    }
}

package io.praecepta.rules.hub.nosqlbased.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.bson.Document;

import com.mongodb.BasicDBList;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import io.praecepta.core.helper.GsonHelperWithAdapter;
import io.praecepta.core.helper.PraeceptaActionStrategyInterfaceAdapter;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.model.projection.IPraeceptaAction;

public class PraeceptaRuleGroupMongoDbDao implements  IPraeceptaRuleGroupDao{

    GsonHelperWithAdapter gsonHelperWithAdapter = new GsonHelperWithAdapter(IPraeceptaAction.class, new PraeceptaActionStrategyInterfaceAdapter());
    MongoCollection<Document> collection;
    public PraeceptaRuleGroupMongoDbDao(Properties properties){
        collection = PraeceptaMongoDbFactory.getRuleGroupMongoDb(properties);
    }

    private List<PraeceptaRuleGroup> iterateList(FindIterable<Document> cursor) {
        List<PraeceptaRuleGroup> praeceptaRuleGroupList = new ArrayList<>();
        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                Document document = cursorIterator.next();
                praeceptaRuleGroupList.add(gsonHelperWithAdapter.toEntity((String) document.get("data"), PraeceptaRuleGroup.class));
            }
        }
        return praeceptaRuleGroupList;
    }



    @Override
    public PraeceptaRuleGroup fetchById(PraeceptaRuleSpaceCompositeKey id) {
        //return iterateList(collection.find());
        return null;
    }

    @Override
    public Collection<PraeceptaRuleGroup> fetchAll() {
        return iterateList(collection.find());
    }

    public List<PraeceptaRuleGroup> fetchByKey(PraeceptaRuleSpaceCompositeKey key){

        return iterateList(collection.find(PraeceptaMongoDbFactory.prepareRuleSpaceSearchQuery(key)));
    }

    @Override
    public Collection<PraeceptaRuleGroup> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
        BasicDBList queryList = new BasicDBList();
        for (PraeceptaRuleSpaceCompositeKey key : ids) {
            queryList.add(PraeceptaMongoDbFactory.prepareRuleGroupSearchQuery(key));
        }
        Document query = new Document( "$or", queryList );
        return iterateList(collection.find(query));
    }

    @Override
    public Collection<PraeceptaRuleGroup> findByExample(PraeceptaRuleGroup praeceptaRuleGroup) {
        return iterateList(collection.find(PraeceptaMongoDbFactory.prepareRuleGroupSearchQuery(praeceptaRuleGroup.getRuleSpaceKey())));
    }

    @Override
    public void insert(PraeceptaRuleGroup praeceptaRuleGroup) {
        String data = gsonHelperWithAdapter.toJson(praeceptaRuleGroup);
        Document document = new Document();
        document.put("data", data);
        document.put("ruleSpaceKey", praeceptaRuleGroup.getRuleSpaceKey());
        document.put("active", true);
        document.put("ruleGroupName", praeceptaRuleGroup.getRuleGroupName());

        collection.insertOne(document);
    }

    @Override
    public void insertAll(Collection<PraeceptaRuleGroup> praeceptaRuleGroups) {
        List<Document> records = new ArrayList<>();
        for(PraeceptaRuleGroup praeceptaRuleGroup : praeceptaRuleGroups){
            String data = gsonHelperWithAdapter.toJson(praeceptaRuleGroup);
            Document document = new Document();
            document.put("data", data);
            document.put("ruleSpaceKey", praeceptaRuleGroup.getRuleSpaceKey());
            document.put("active", true);
            document.put("ruleGroupName", praeceptaRuleGroup.getRuleGroupName());

            records.add(document);
        }
        collection.insertMany(records);
    }

    @Override
    public void update(PraeceptaRuleGroup praeceptaRuleGroup) {
        Document document = gsonHelperWithAdapter.toEntity(gsonHelperWithAdapter.toJson(praeceptaRuleGroup), Document.class);
        document.put("active", true);
        collection.insertOne(document);
    }

    @Override
    public void updateAll(Collection<PraeceptaRuleGroup> praeceptaRuleGroups) {
        insertAll(praeceptaRuleGroups);
    }

    @Override
    public void deleteById(PraeceptaRuleSpaceCompositeKey id) {
        Document updateQuery = new Document();
        updateQuery.append("$set",
                new Document().append("active", true));
        collection.updateMany(PraeceptaMongoDbFactory.prepareRuleGroupSearchQuery(id), updateQuery);
    }

    @Override
    public void delete(PraeceptaRuleGroup praeceptaRuleGroup) {
        deleteById(praeceptaRuleGroup.getRuleSpaceKey());
    }

    @Override
    public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
        BasicDBList queryList = new BasicDBList();
        for (PraeceptaRuleSpaceCompositeKey key : ids) {
            queryList.add(PraeceptaMongoDbFactory.prepareRuleGroupSearchQuery(key));
        }
        Document query = new Document( "$or", queryList );
        Document updateQuery = new Document();
        updateQuery.append("$set",
                new Document().append("active", false));
        collection.updateMany(query, updateQuery);
    }

    @Override
    public void deleteAll(Collection<PraeceptaRuleGroup> praeceptaRuleGroups) {
        BasicDBList queryList = new BasicDBList();
        for (PraeceptaRuleGroup praeceptaRuleGroup : praeceptaRuleGroups) {
            PraeceptaRuleSpaceCompositeKey key = praeceptaRuleGroup.getRuleSpaceKey();

            queryList.add(PraeceptaMongoDbFactory.prepareRuleGroupSearchQuery(key));
        }
        Document query = new Document( "$or", queryList );

        Document updateQuery = new Document();
        updateQuery.append("$set",
                new Document().append("active", false));
        collection.updateMany(query, updateQuery);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Clear is not supported.");
    }

    @Override
    public PraeceptaRuleGroup fetchByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {
        Document searchQuery = PraeceptaMongoDbFactory.prepareRuleGroupSearchQuery(key);
        searchQuery.put("ruleGroupName", ruleGroupName);
        FindIterable<Document> cursor = collection.find(searchQuery);

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
            	Document document = cursorIterator.next();
                return gsonHelperWithAdapter.toEntity((String) document.get("data"), PraeceptaRuleGroup.class);
            }
        }

        return null;
    }
}

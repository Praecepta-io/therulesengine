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

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import org.hibernate.criterion.Restrictions;

public class PraeceptaRuleSpaceMongoDbDao implements  IPraeceptaRuleSpaceDao{
    MongoCollection<PraeceptaRuleSpace> collection;

    MongoCollection<Document> ruleGroupCollection;

//    GsonHelperWithAdapter gsonHelperWithAdapter = new GsonHelperWithAdapter(IPraeceptaAction.class, new PraeceptaActionStrategyInterfaceAdapter());
   
    public PraeceptaRuleSpaceMongoDbDao(Properties properties){
        collection = PraeceptaMongoDbFactory.getRuleSpaceMongoDb(properties);
        ruleGroupCollection = PraeceptaMongoDbFactory.getRuleGroupMongoDb(properties);
    }
    @Override
    public Collection<PraeceptaRuleSpace> fetchAll() {

        Document searchQuery = new Document();
        searchQuery.put("active", true);
        return iterateList(collection.find(searchQuery));
    }

    @Override
    public Collection<PraeceptaRuleSpace> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
       if(!PraeceptaObjectHelper.isObjectEmpty(ids)) {
           BasicDBList queryList = new BasicDBList();

           for (PraeceptaRuleSpaceCompositeKey key : ids) {
               queryList.add(PraeceptaMongoDbFactory.prepareRuleSpaceSearchQuery(key));
           }

           Document query = new Document("$or", queryList);

           return iterateList(collection.find(query));
       }else{
           return new ArrayList<>();
       }
    }
    @Override
    public Collection<PraeceptaRuleSpace> findByExample(PraeceptaRuleSpace praeceptaRuleSpace) {
        return iterateList(collection.find(PraeceptaMongoDbFactory.prepareRuleSpaceSearchQuery(praeceptaRuleSpace.getRuleSpaceKey())));
    }

    private List<PraeceptaRuleSpace> iterateList(FindIterable<PraeceptaRuleSpace> cursor) {
        List<PraeceptaRuleSpace> praeceptaRuleSpaceList = new ArrayList<>();
        try (final MongoCursor<PraeceptaRuleSpace> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                PraeceptaRuleSpace praeceptaRuleSpace = cursorIterator.next();
                PraeceptaRuleSpaceCompositeKey praeceptaRuleSpaceCompositeKey = praeceptaRuleSpace.getRuleSpaceKey();
                praeceptaRuleSpaceCompositeKey.setVersion(praeceptaRuleSpace.getRuleSpaceKey().getVersion());
                praeceptaRuleSpace.setRuleSpaceKey(praeceptaRuleSpaceCompositeKey);
                praeceptaRuleSpaceList.add(praeceptaRuleSpace);
            }
        }
        return praeceptaRuleSpaceList;
    }

    @Override
    public void insert(PraeceptaRuleSpace praeceptaRuleSpace) {
        collection.insertOne(praeceptaRuleSpace);
    }

    @Override
    public void insertAll(Collection<PraeceptaRuleSpace> praeceptaRuleSpaces) {
        collection.insertMany((List<? extends PraeceptaRuleSpace>) praeceptaRuleSpaces);

    }

    @Override
    public void update(PraeceptaRuleSpace praeceptaRuleSpace) {
       insert(praeceptaRuleSpace);
    }

    @Override
    public void updateAll(Collection<PraeceptaRuleSpace> praeceptaRuleSpaces) {
        insertAll(praeceptaRuleSpaces);
    }

    @Override
    public void deleteById(PraeceptaRuleSpaceCompositeKey id) {

        Document query = PraeceptaMongoDbFactory.prepareRuleSpaceSearchQuery(id);

        Document updateQuery = new Document();
        updateQuery.append("$set",
                new Document().append("active", false));

        collection.updateMany(query, updateQuery);

    }

    @Override
    public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {

        BasicDBList queryList = new BasicDBList();
        for (PraeceptaRuleSpaceCompositeKey key : ids) {
            queryList.add(PraeceptaMongoDbFactory.prepareRuleSpaceSearchQuery(key));
        }
        Document query = new Document( "$or", queryList );

        Document updateQuery = new Document();
        updateQuery.append("$set",
                new Document().append("active", false));

        collection.updateMany(query, updateQuery);
    }

    @Override
    public void deleteAll(Collection<PraeceptaRuleSpace> praeceptaRuleSpaces) {

        BasicDBList queryList = new BasicDBList();
        for (PraeceptaRuleSpace praeceptaRuleSpace : praeceptaRuleSpaces) {
            PraeceptaRuleSpaceCompositeKey key = praeceptaRuleSpace.getRuleSpaceKey();

            queryList.add(PraeceptaMongoDbFactory.prepareRuleSpaceSearchQuery(key));
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
    public void delete(PraeceptaRuleSpace ruleSpace) {
        deleteById(ruleSpace.getRuleSpaceKey());
    }

    @Override
    public List<PraeceptaRuleSpace> fetchByKey(PraeceptaRuleSpaceCompositeKey key) {
        FindIterable<PraeceptaRuleSpace> cursor = collection.find(PraeceptaMongoDbFactory.prepareRuleSpaceSearchQuery(key));

        List<PraeceptaRuleSpace> ruleSpaceList =  iterateList(cursor);

        for(PraeceptaRuleSpace praeceptaRuleSpace : ruleSpaceList){
            FindIterable<Document> ruleGroupCursor = ruleGroupCollection.find(PraeceptaMongoDbFactory.prepareRuleSpaceSearchQuery(praeceptaRuleSpace.getRuleSpaceKey()));

            List<PraeceptaRuleGroup> praeceptaRuleGroupList = new ArrayList<>();
            try (final MongoCursor<Document> cursorIterator = ruleGroupCursor.cursor()) {
                while (cursorIterator.hasNext()) {
                    Document document = cursorIterator.next();
                    praeceptaRuleGroupList.add(GsonHelper.toEntity((String) document.get("data"), PraeceptaRuleGroup.class));
                }
            }
            praeceptaRuleSpace.setPraeceptaRuleGrps(praeceptaRuleGroupList);
        }
        return ruleSpaceList;
    }

    @Override
    public PraeceptaRuleSpace fetchByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {
        key.setVersion(version);
        Document searchQuery = PraeceptaMongoDbFactory.prepareRuleSpaceSearchQuery(key);
        FindIterable<PraeceptaRuleSpace> cursor = collection.find(searchQuery);

        try (final MongoCursor<PraeceptaRuleSpace> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                PraeceptaRuleSpace praeceptaRuleSpace = cursorIterator.next();
                PraeceptaRuleSpaceCompositeKey praeceptaRuleSpaceCompositeKey = praeceptaRuleSpace.getRuleSpaceKey();
                praeceptaRuleSpaceCompositeKey.setVersion(praeceptaRuleSpace.getRuleSpaceKey().getVersion());
                return praeceptaRuleSpace;
            }
        }

        return null;
    }

    @Override
    public boolean deleteByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {
        key.setVersion(version);
        deleteById(key);
        return true;
    }


}

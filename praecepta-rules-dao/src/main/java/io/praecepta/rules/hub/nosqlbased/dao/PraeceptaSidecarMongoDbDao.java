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

import io.praecepta.core.helper.ObjectHelper;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSideCars;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public class PraeceptaSidecarMongoDbDao implements IPraeceptaRuleSideCarsDao {

    MongoCollection<PraeceptaRuleSideCars> collection;

    public PraeceptaSidecarMongoDbDao(Properties properties){
        collection = PraeceptaMongoDbFactory.getSidecarMongoDb(properties);
    }

    private List<PraeceptaRuleSideCars> iterateList(FindIterable<PraeceptaRuleSideCars> cursor) {
        List<PraeceptaRuleSideCars> praeceptaRuleSideCarsList = new ArrayList<>();
        try (final MongoCursor<PraeceptaRuleSideCars> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                praeceptaRuleSideCarsList.add(cursorIterator.next());
            }
        }
        return praeceptaRuleSideCarsList;
    }

    @Override
    public PraeceptaRuleSideCars fetchById(PraeceptaRuleSpaceCompositeKey id) {
        return null;
    }

    @Override
    public Collection<PraeceptaRuleSideCars> fetchAll() {
        return iterateList(collection.find());
    }

    @Override
    public Collection<PraeceptaRuleSideCars> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
        if(!ObjectHelper.isObjectEmpty(ids)) {
            BasicDBList queryList = new BasicDBList();

            for (PraeceptaRuleSpaceCompositeKey key : ids) {
                queryList.add(PraeceptaMongoDbFactory.prepareSidecarSearchQuery(key));
            }

            Document query = new Document("$or", queryList);

            return iterateList(collection.find(query));
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public Collection<PraeceptaRuleSideCars> findByExample(PraeceptaRuleSideCars praeceptaRuleSideCars) {
        return iterateList(collection.find(PraeceptaMongoDbFactory.prepareSidecarSearchQuery(praeceptaRuleSideCars.getRuleSpaceKey())));
    }

    @Override
    public void insert(PraeceptaRuleSideCars praeceptaRuleSideCars) {
        praeceptaRuleSideCars.setActive(true);
        collection.insertOne(praeceptaRuleSideCars);
    }

    @Override
    public void insertAll(Collection<PraeceptaRuleSideCars> praeceptaRuleSideCars) {
        collection.insertMany((List<? extends PraeceptaRuleSideCars>) praeceptaRuleSideCars);
    }

    @Override
    public void update(PraeceptaRuleSideCars praeceptaRuleSideCars) {
        insert(praeceptaRuleSideCars);
    }

    @Override
    public void updateAll(Collection<PraeceptaRuleSideCars> praeceptaRuleSideCars) {
        insertAll(praeceptaRuleSideCars);
    }

    @Override
    public boolean deleteByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName){

        Document searchQuery = PraeceptaMongoDbFactory.prepareSidecarSearchQuery(key);
        searchQuery.put("ruleGroupSideCars.ruleGrpName", ruleGroupName);

        Document updateQuery = new Document();
        updateQuery.append("$set",
                new Document().append("active", false));

        collection.updateMany(searchQuery, updateQuery);
        return false;
    }
    @Override
    public void delete(PraeceptaRuleSideCars praeceptaRuleSideCars) {
        Document searchQuery = PraeceptaMongoDbFactory.prepareSidecarSearchQuery(praeceptaRuleSideCars.getRuleSpaceKey());
        Document updateQuery = new Document();
        updateQuery.append("$set",
                new Document().append("active", false));
        collection.updateMany(searchQuery, updateQuery);
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
    public void deleteAll(Collection<PraeceptaRuleSideCars> praeceptaRuleSideCars) {
        BasicDBList queryList = new BasicDBList();
        for (PraeceptaRuleSideCars praeceptaRuleSideCar : praeceptaRuleSideCars) {
            PraeceptaRuleSpaceCompositeKey key = praeceptaRuleSideCar.getRuleSpaceKey();

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

    }


    @Override
    public PraeceptaRuleSideCars fetchByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {
        Document searchQuery = PraeceptaMongoDbFactory.prepareSidecarSearchQuery(key);
        searchQuery.put("ruleGroupSideCars.ruleGrpName", ruleGroupName);
        FindIterable<PraeceptaRuleSideCars> cursor = collection.find(searchQuery);

        try (final MongoCursor<PraeceptaRuleSideCars> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                return  cursorIterator.next();
            }
        }

        return null;
    }
}

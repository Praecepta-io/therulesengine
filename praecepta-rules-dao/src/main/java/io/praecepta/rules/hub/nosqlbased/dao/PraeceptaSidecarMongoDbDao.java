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

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;

public class PraeceptaSidecarMongoDbDao implements IPraeceptaRuleSideCarsDao {

    MongoCollection<PraeceptaSideCarsInfo> collection;

    public PraeceptaSidecarMongoDbDao(Properties properties){
        collection = PraeceptaMongoDbFactory.getSidecarMongoDb(properties);
    }

    private List<PraeceptaSideCarsInfo> iterateList(FindIterable<PraeceptaSideCarsInfo> cursor) {
        List<PraeceptaSideCarsInfo> praeceptaRuleSideCarsList = new ArrayList<>();
        try (final MongoCursor<PraeceptaSideCarsInfo> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                praeceptaRuleSideCarsList.add(cursorIterator.next());
            }
        }
        return praeceptaRuleSideCarsList;
    }

    @Override
    public PraeceptaSideCarsInfo fetchById(PraeceptaRuleSpaceCompositeKey id) {
        return null;
    }

    @Override
    public Collection<PraeceptaSideCarsInfo> fetchAll() {
        return iterateList(collection.find());
    }

    @Override
    public Collection<PraeceptaSideCarsInfo> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
        if(!PraeceptaObjectHelper.isObjectEmpty(ids)) {
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
    public Collection<PraeceptaSideCarsInfo> findByExample(PraeceptaSideCarsInfo praeceptaRuleSideCars) {
        return iterateList(collection.find(PraeceptaMongoDbFactory.prepareSidecarSearchQuery(praeceptaRuleSideCars.getRuleSpaceKey())));
    }

    @Override
    public void insert(PraeceptaSideCarsInfo praeceptaRuleSideCars) {
        praeceptaRuleSideCars.setActive(true);
        collection.insertOne(praeceptaRuleSideCars);
    }

    @Override
    public void insertAll(Collection<PraeceptaSideCarsInfo> praeceptaRuleSideCars) {
        collection.insertMany((List<? extends PraeceptaSideCarsInfo>) praeceptaRuleSideCars);
    }

    @Override
    public void update(PraeceptaSideCarsInfo praeceptaRuleSideCars) {
        insert(praeceptaRuleSideCars);
    }

    @Override
    public void updateAll(Collection<PraeceptaSideCarsInfo> praeceptaRuleSideCars) {
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
    public void delete(PraeceptaSideCarsInfo praeceptaRuleSideCars) {
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
    public void deleteAll(Collection<PraeceptaSideCarsInfo> praeceptaRuleSideCars) {
        BasicDBList queryList = new BasicDBList();
        for (PraeceptaSideCarsInfo praeceptaRuleSideCar : praeceptaRuleSideCars) {
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
    public PraeceptaSideCarsInfo fetchByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {
        Document searchQuery = PraeceptaMongoDbFactory.prepareSidecarSearchQuery(key);
        searchQuery.put("ruleGroupSideCars.ruleGrpName", ruleGroupName);
        FindIterable<PraeceptaSideCarsInfo> cursor = collection.find(searchQuery);

        try (final MongoCursor<PraeceptaSideCarsInfo> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                return  cursorIterator.next();
            }
        }

        return null;
    }
}

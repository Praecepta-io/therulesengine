package io.praecepta.rules.hub.nosqlbased.service;

import java.util.Collection;
import java.util.List;

import io.praecepta.rules.dto.RuleSpaceInfo;
import io.praecepta.rules.hub.nosqlbased.dao.PraeceptaRuleGroupMongoDbDao;
import io.praecepta.rules.hub.nosqlbased.dao.PraeceptaRuleSpaceMongoDbDao;

public class PraeceptaMongoDbServiceImpl implements  IPraeceptaMongoDbService{

    PraeceptaRuleGroupMongoDbDao praeceptaRuleGroupMongoDbDao;

    PraeceptaRuleSpaceMongoDbDao praeceptaRuleSpaceMongoDbDao;

    public PraeceptaMongoDbServiceImpl(PraeceptaRuleGroupMongoDbDao praeceptaRuleGroupMongoDbDao, PraeceptaRuleSpaceMongoDbDao praeceptaRuleSpaceMongoDbDao){
        this.praeceptaRuleGroupMongoDbDao = praeceptaRuleGroupMongoDbDao;
        this.praeceptaRuleSpaceMongoDbDao = praeceptaRuleSpaceMongoDbDao;
    }


    @Override
    public Collection<RuleSpaceInfo> fetchAll() {
        return null;
    }

    @Override
    public Collection<RuleSpaceInfo> fetchByIds(Collection<RuleSpaceInfo> ids) {
        return null;
    }

    @Override
    public Collection<RuleSpaceInfo> findByExample(RuleSpaceInfo RuleSpaceInfo) {
        return null;
    }

    @Override
    public void insert(RuleSpaceInfo RuleSpaceInfo) {

    }

    @Override
    public void insertAll(Collection<RuleSpaceInfo> RuleSpaceInfos) {

    }

    @Override
    public void update(RuleSpaceInfo RuleSpaceInfo) {

    }

    @Override
    public void updateAll(Collection<RuleSpaceInfo> RuleSpaceInfos) {

    }

    @Override
    public void deleteById(RuleSpaceInfo id) {

    }

    @Override
    public void deleteByIds(Collection<RuleSpaceInfo> ids) {

    }

    @Override
    public void deleteAll(Collection<RuleSpaceInfo> RuleSpaceInfos) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void delete(RuleSpaceInfo ruleSpace) {

    }

    @Override
    public List<RuleSpaceInfo> fetchByKey(RuleSpaceInfo key) {
        return null;
    }

    @Override
    public RuleSpaceInfo fetchByKeyAndVersion(RuleSpaceInfo key, String version) {
        return null;
    }

    @Override
    public boolean deleteByKeyAndVersion(RuleSpaceInfo key, String version) {
        return false;
    }
}

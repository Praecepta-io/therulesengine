package io.praecepta.rules.hub.nosqlbased.service;

import java.util.Collection;
import java.util.List;

import io.praecepta.rules.dto.RuleSpaceInfo;

public interface IPraeceptaMongoDbService {

    public Collection<RuleSpaceInfo> fetchAll();

    public Collection<RuleSpaceInfo> fetchByIds(Collection<RuleSpaceInfo> ids);

    public Collection<RuleSpaceInfo> findByExample(RuleSpaceInfo RuleSpaceInfo);

    public void insert(RuleSpaceInfo RuleSpaceInfo);

    public void insertAll(Collection<RuleSpaceInfo> RuleSpaceInfos);

    public void update(RuleSpaceInfo RuleSpaceInfo);

    public void updateAll(Collection<RuleSpaceInfo> RuleSpaceInfos);

    public void deleteById(RuleSpaceInfo id);

    public void deleteByIds(Collection<RuleSpaceInfo> ids);

    public void deleteAll(Collection<RuleSpaceInfo> RuleSpaceInfos);

    public void clear();

    public void delete(RuleSpaceInfo ruleSpace);

    public List<RuleSpaceInfo> fetchByKey(RuleSpaceInfo key);

    public RuleSpaceInfo fetchByKeyAndVersion(RuleSpaceInfo key, String version);

    public boolean deleteByKeyAndVersion(RuleSpaceInfo key, String version);
}

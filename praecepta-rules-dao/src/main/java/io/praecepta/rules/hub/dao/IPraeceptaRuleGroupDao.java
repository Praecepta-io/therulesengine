package io.praecepta.rules.hub.dao;

import java.util.Collections;
import java.util.List;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.dao.intf.IDAO;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public interface IPraeceptaRuleGroupDao extends IDAO<PraeceptaRuleSpaceCompositeKey, PraeceptaRuleGroup> {

	public static PraeceptaRuleSpaceCompositeKey getKeyFromRuleGroup(PraeceptaRuleGroup ruleGrp) {

		PraeceptaRuleSpaceCompositeKey key = null;

		if (ruleGrp != null && !PraeceptaObjectHelper.isObjectEmpty(ruleGrp.getRuleSpaceKey())) {

			key = ruleGrp.getRuleSpaceKey();
		}
		return key;
	}
	
	PraeceptaRuleGroup fetchByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName);
	
	default boolean deleteByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName){
		
		return true;
	}
	
	default List<PraeceptaRuleGroup> fetchByKey(PraeceptaRuleSpaceCompositeKey key){
		
		return Collections.emptyList();
	}
	
	@Override
	default void deleteById(PraeceptaRuleSpaceCompositeKey key) {

		deleteByKeyAndVersion(key, key.getVersion());
		
	}

	default boolean deleteByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version){
		
		return true;
	}

	@Override
	default PraeceptaRuleGroup fetchById(PraeceptaRuleSpaceCompositeKey id) {
		throw new UnsupportedOperationException("Fetch By Id is not supported. Instead Use fetch by key method");
	}
	
	
	
}

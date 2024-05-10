package io.praecepta.rules.hub.dao;

import java.util.Collections;
import java.util.List;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.dao.intf.IDAO;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public interface IPraeceptaRuleSpaceDao extends IDAO<PraeceptaRuleSpaceCompositeKey, PraeceptaRuleSpace>{
	
	public static PraeceptaRuleSpaceCompositeKey getKeyFromRuleSapce(PraeceptaRuleSpace ruleSpace) {
		
		PraeceptaRuleSpaceCompositeKey key = null;
		
		if(ruleSpace != null && !PraeceptaObjectHelper.isObjectEmpty(ruleSpace.getRuleSpaceKey())) {
			
				key = ruleSpace.getRuleSpaceKey();
		}
		return key;
	}
	
	@Override
	default void delete(PraeceptaRuleSpace ruleSpace) {
		
		PraeceptaRuleSpaceCompositeKey key = getKeyFromRuleSapce(ruleSpace);
		
		if(key != null) {
			deleteById(key);
		}
	}

	default List<PraeceptaRuleSpace> fetchByKey(PraeceptaRuleSpaceCompositeKey key){
		
		return Collections.emptyList();
	}
	
	PraeceptaRuleSpace fetchByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version);

	@Override
	default void deleteById(PraeceptaRuleSpaceCompositeKey key) {

		deleteByKeyAndVersion(key, key.getVersion());
		
	}

	default boolean deleteByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version){
		
		return true;
	}

	@Override
	default PraeceptaRuleSpace fetchById(PraeceptaRuleSpaceCompositeKey id) {
		throw new UnsupportedOperationException("Fetch By Id is not supported. Instead Use fetch by key method");
	}
	
	
	
}

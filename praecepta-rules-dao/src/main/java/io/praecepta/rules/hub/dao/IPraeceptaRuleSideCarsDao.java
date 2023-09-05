package io.praecepta.rules.hub.dao;

import java.util.Collections;
import java.util.List;

import io.praecepta.core.helper.ObjectHelper;
import io.praecepta.dao.intf.IDAO;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSideCars;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public interface IPraeceptaRuleSideCarsDao extends IDAO<PraeceptaRuleSpaceCompositeKey, PraeceptaRuleSideCars> {

	public static PraeceptaRuleSpaceCompositeKey getKeyFromSideCar(PraeceptaRuleSideCars ruleSideCar) {
		
		PraeceptaRuleSpaceCompositeKey key = null;

		if (ruleSideCar != null && !ObjectHelper.isObjectEmpty(ruleSideCar.getRuleSpaceKey())) {

			key = ruleSideCar.getRuleSpaceKey();
		}
		return key;
	}
			
	PraeceptaRuleSideCars fetchByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName);

	default boolean deleteByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {

		return true;
	}

	default List<PraeceptaRuleSideCars> fetchByKey(PraeceptaRuleSpaceCompositeKey key) {

		return Collections.emptyList();
	}

	@Override
	default void deleteById(PraeceptaRuleSpaceCompositeKey key) {

		deleteByKeyAndVersion(key, key.getVersion());

	}

	default boolean deleteByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {

		return true;
	}
}

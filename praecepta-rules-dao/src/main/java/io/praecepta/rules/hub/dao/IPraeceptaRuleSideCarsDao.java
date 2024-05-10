package io.praecepta.rules.hub.dao;

import java.util.Collections;
import java.util.List;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.dao.intf.IDAO;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;

public interface IPraeceptaRuleSideCarsDao extends IDAO<PraeceptaRuleSpaceCompositeKey, PraeceptaSideCarsInfo> {

	public static PraeceptaRuleSpaceCompositeKey getKeyFromSideCar(PraeceptaSideCarsInfo ruleSideCar) {
		
		PraeceptaRuleSpaceCompositeKey key = null;

		if (ruleSideCar != null && !PraeceptaObjectHelper.isObjectEmpty(ruleSideCar.getRuleSpaceKey())) {

			key = ruleSideCar.getRuleSpaceKey();
		}
		return key;
	}
			
	PraeceptaSideCarsInfo fetchByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName);

	default boolean deleteByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {

		return true;
	}

	default List<PraeceptaSideCarsInfo> fetchByKey(PraeceptaRuleSpaceCompositeKey key) {

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

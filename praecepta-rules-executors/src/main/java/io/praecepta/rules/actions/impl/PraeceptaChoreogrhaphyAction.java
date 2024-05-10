package io.praecepta.rules.actions.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.praecepta.data.injestors.common.intf.IPraeceptaDataInjestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.actions.PraeceptaAbstractAction;
import io.praecepta.rules.actions.helper.PraeceptaChoreogrhaphyActionHelper;
import io.praecepta.rules.dto.RuleGroupInfo;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;

/**
 * 
 * @author rajasrikar
 *
 */
public class PraeceptaChoreogrhaphyAction extends PraeceptaAbstractAction {

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaChoreogrhaphyAction.class);

	private List<Map<String, Object>> actionParameters;

	Map<RuleGroupInfo, IPraeceptaDataInjestor> ruleGroupWithDataInjestorMap;
	
	boolean initialized = false;

	@Override
	public String getActionName() {
		return PraeceptaChoreogrhaphyAction.class.getName();
	}

	@Override
	public void performAction(PraeceptaRequestStore input) {

		if (!super.actionRegistered) {
			super.registerPraeceptaActionClazz(this);
			super.actionRegistered = true;
		}

		if (!initialized) {
			ruleGroupWithDataInjestorMap = new HashMap<>();
			initialized = true;
		}

		doAction(input);
	}

	@Override
	protected void doAction(PraeceptaRequestStore input) {

		LOG.info("Inside doAction");

		RuleGroupInfo ruleGroupWithSpaceKey = (RuleGroupInfo) input
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME_WITH_SPACE_KEY);
		
		if (!PraeceptaObjectHelper.isObjectNull(ruleGroupWithSpaceKey)) {
			
			//to upsert dataInjestor to the map with ruleGroupWithSpaceKey
			performInitializeDataInjestor(ruleGroupWithSpaceKey);

			IPraeceptaDataInjestor dataInjestor = ruleGroupWithDataInjestorMap.get(ruleGroupWithSpaceKey);

			if (!PraeceptaObjectHelper.isObjectNull(dataInjestor)) {

				LOG.info("Before injesting data for RuleGroup {}", ruleGroupWithSpaceKey.toString());

				dataInjestor.injestData(PraeceptaChoreogrhaphyActionHelper.buildDataRecord(input));
			}
		}else {
			LOG.info("Rule Group info object found null");
		}
		LOG.info("Exiting doAction");
	}
	
	protected void performInitializeDataInjestor(RuleGroupInfo ruleGroupWithSpaceKey) {
		/*ruleGroupWithDataInjestorMap.putIfAbsent(ruleGroupWithSpaceKey, PraeceptaChoreogrhaphyActionHelper
				.initializeDataInjestor(this.actionParameters, getActionStrategy(),null));*/
	}

	public List<Map<String, Object>> getActionParameters() {
		return actionParameters;
	}

	public void setActionParameters(List<Map<String, Object>> actionParameters) {
		this.actionParameters = actionParameters;
	}
}

package io.praecepta.rules.engine.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.actions.PraeceptaAbstractAction;
import io.praecepta.rules.actions.enums.PraeceptaActionTypeEnum;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaRuleLevelInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.model.projection.PraeceptaActionDetails;

public class PraeceptaPipesAndFiltersRuleExecutionEngine extends PraeceptaBasicRuleExecutionEngine{

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaPipesAndFiltersRuleExecutionEngine.class);

	@Override
	protected void addPostRuleExecutionSideCars(PraeceptaRequestStore ruleStore,
			GenericPraeceptaRuleLevelInfoTrackerSideCarInjector postRuleSideCar) {
//		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector
		
		/*Map<String, List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>> ruleNameToExecutionSideCars =  (Map<String, List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_EXECUTION_SIDE_CARS);
				
		postRuleSideCar.setRuleNameToExecutionSideCars(ruleNameToExecutionSideCars);*/
		
		super.addPostRuleExecutionSideCars(ruleStore, postRuleSideCar);
	}
	
	@Override
	protected void addPreRuleExecutionSideCars(PraeceptaRequestStore ruleStore,
			GenericPraeceptaRuleLevelInfoTrackerSideCarInjector preRuleSideCar) {
		
		/*Map<String, List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>> ruleNameToExecutionSideCars =  (Map<String, List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_EXECUTION_SIDE_CARS);
		
		preRuleSideCar.setRuleNameToExecutionSideCars(ruleNameToExecutionSideCars);*/
		
		super.addPreRuleExecutionSideCars(ruleStore, preRuleSideCar);
		
	}

	@Override
	protected PraeceptaAbstractAction getPraeceptaAction(PraeceptaActionDetails actionDetails) {

		if(!PraeceptaObjectHelper.isObjectEmpty(actionDetails)) {
			
			if(!PraeceptaObjectHelper.isObjectEmpty(actionDetails.getActionType())) {
				PraeceptaAbstractAction action = PraeceptaActionTypeEnum.getActionType(actionDetails.getActionType()).getAction(); 
				
				super.assignActionDetailsToAction(actionDetails, action);
				
				return action;
			}
			
		}
		return super.getPraeceptaAction(actionDetails);
	}
	
	protected void conductRuleGroupExecution(PraeceptaRequestStore ruleStore, PraeceptaRuleGroup ruleGrpToUse) {
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_EXECUTION_ENGINE, this);
		
		super.conductRuleGroupExecution(ruleStore, ruleGrpToUse);
	}
	

}

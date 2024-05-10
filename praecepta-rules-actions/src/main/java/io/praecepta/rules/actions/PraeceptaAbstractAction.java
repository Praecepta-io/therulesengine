package io.praecepta.rules.actions;

import java.util.HashMap;
import java.util.Map;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.actions.enums.PraeceptaExecutionActionType;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.factor.actions.PraeceptaUnderlyingFactorAction;
import io.praecepta.rules.model.projection.IPraeceptaAction;
import io.praecepta.rules.registry.PraeceptaActionRegistry;

/**
 * 
 * @author rajasrikar
 *
 * @param <INPUT>
 * @param <OUTPUT>
 */
public abstract class PraeceptaAbstractAction
		implements IPraeceptaAction<PraeceptaRequestStore> {
	
	private PraeceptaActionStrategyType actionStrategy;
	private String actionAttributeName;
	private String valueToAssign;
	private String sourceValueAttributeName;
	private String actionMessage;
	private PraeceptaExecutionActionType actionTypeExecuted;
	private String actionName;
	
	private Map<PraeceptaActionParametersType, Object> parameters;

	protected volatile boolean actionRegistered;

	@Override
	public void performAction(PraeceptaRequestStore input) {
		
		if (!actionRegistered) {
			registerPraeceptaActionClazz(this);
			actionRegistered = true;
		}
		
		// Action Name to Action Holder
		Map<String, PraeceptaActionHolder> actionHolders = (Map<String, PraeceptaActionHolder>) input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO);
		
		if(PraeceptaObjectHelper.isObjectEmpty(actionHolders)) {
			actionHolders = new HashMap<String, PraeceptaActionHolder>();
		}
		
		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ALL_ACTIONS_INFO, actionHolders);
		
		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_INFO, 
				PraeceptaUnderlyingFactorAction.getActionHolder());

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_STATUS, true);
		
		Boolean multiRuleEvaluationStatus = (Boolean) input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_CONDITION_STATUS);
		
		if(multiRuleEvaluationStatus != null) {
			if(Boolean.valueOf(input.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_CONDITION_STATUS).toString())){
				actionTypeExecuted = PraeceptaExecutionActionType.SUCCESS;
			} else {
				actionTypeExecuted = PraeceptaExecutionActionType.FAIL;
			}
		}
		
		doAction(input);
	}
	
	protected abstract void doAction(PraeceptaRequestStore input);

	public PraeceptaActionStrategyType getActionStrategy() {
		return actionStrategy;
	}

	public void setActionStrategy(PraeceptaActionStrategyType actionStrategy) {
		this.actionStrategy = actionStrategy;
	}

	public String getActionAttributeName() {
		return actionAttributeName;
	}

	public void setActionAttributeName(String actionAttributeName) {
		this.actionAttributeName = actionAttributeName;
	}

	public String getValueToAssign() {
		return valueToAssign;
	}

	public void setValueToAssign(String valueToAssign) {
		this.valueToAssign = valueToAssign;
	}

	public Map<PraeceptaActionParametersType, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<PraeceptaActionParametersType, Object> parameters) {
		this.parameters = parameters;
	}
	
	public String getSourceValueAttributeName() {
		return sourceValueAttributeName;
	}

	public void setSourceValueAttributeName(String sourceValueAttributeName) {
		this.sourceValueAttributeName = sourceValueAttributeName;
	}

	protected void registerPraeceptaActionClazz(IPraeceptaAction<?> actionObj) {
		PraeceptaActionRegistry.registerAnAction(getActionName(), actionObj);
	}

	public String getActionMessage() {
		return actionMessage;
	}

	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}

	public PraeceptaExecutionActionType getActionTypeExecuted() {
		return actionTypeExecuted;
	}

	public void setActionTypeExecuted(PraeceptaExecutionActionType actionTypeExecuted) {
		this.actionTypeExecuted = actionTypeExecuted;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
}

package io.praecepta.rules.actions;

import java.util.Map;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
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
	
	private Map<PraeceptaActionParametersType, Object> parameters;

	protected volatile boolean actionRegistered;

	@Override
	public void performAction(PraeceptaRequestStore input) {
		
		if (!actionRegistered) {
			registerPraeceptaActionClazz(this);
			actionRegistered = true;
		}
		
		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_INFO, 
				PraeceptaUnderlyingFactorAction.getActionHolder());

		input.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_STATUS, true);
		
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
	
}

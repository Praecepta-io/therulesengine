package io.praecepta.rules.actions.enums;

import java.util.HashMap;
import java.util.Map;

import io.praecepta.rules.factor.actions.PraeceptaUnderlyingFactorAction;
import io.praecepta.rules.factor.actions.impl.PraeceptaAddFactorAction;
import io.praecepta.rules.factor.actions.impl.PraeceptaDaysAdditionFactorAction;
import io.praecepta.rules.factor.actions.impl.PraeceptaDaysSubtractionFactorAction;
import io.praecepta.rules.factor.actions.impl.PraeceptaDivisionFactorAction;
import io.praecepta.rules.factor.actions.impl.PraeceptaHoursAdditionFactorAction;
import io.praecepta.rules.factor.actions.impl.PraeceptaHoursSubtractionFactorAction;
import io.praecepta.rules.factor.actions.impl.PraeceptaMinutesAdditionFactorAction;
import io.praecepta.rules.factor.actions.impl.PraeceptaMinutesSubtractionFactorAction;
import io.praecepta.rules.factor.actions.impl.PraeceptaMonthsAdditionFactorAction;
import io.praecepta.rules.factor.actions.impl.PraeceptaMultiplicationFactorAction;
import io.praecepta.rules.factor.actions.impl.PraeceptaSubtractionFactorAction;
import io.praecepta.rules.factor.actions.impl.PraeceptaYearsAdditionFactorAction;
import io.praecepta.rules.factor.actions.impl.PraeceptaYearsSubtractionFactorAction;

// Change the name to PraeceptaFactorActionTypeToAction
public enum PraeceptaActionFactorTypeToAction {

	ADDITION_ACTION(PraeceptaFactorActionType.ADDITION, new PraeceptaAddFactorAction()),
	DIVISION_ACTION(PraeceptaFactorActionType.DIVISION, new PraeceptaDivisionFactorAction()),
	SUBTRACTION_ACTION(PraeceptaFactorActionType.SUBTRACTION, new PraeceptaSubtractionFactorAction()),
	MULTIPLICATION_ACTION(PraeceptaFactorActionType.MULTIPLICATION, new PraeceptaMultiplicationFactorAction()),
	DAYS_ADD_ACTION(PraeceptaFactorActionType.DAYS_ADDITION, new PraeceptaDaysAdditionFactorAction()),
	DAYS_MINUS_ACTION(PraeceptaFactorActionType.DAYS_SUBTRACTION, new PraeceptaDaysSubtractionFactorAction()),
	MONTHS_ADD_ACTION(PraeceptaFactorActionType.MONTHS_ADDDITION, new PraeceptaMonthsAdditionFactorAction()),
	MONTHS_MINUS_ACTION(PraeceptaFactorActionType.MONTHS_SUBTRACTION, new PraeceptaMinutesSubtractionFactorAction()),
	HOURS_ADD_ACTION(PraeceptaFactorActionType.HOURS_ADDITION, new PraeceptaHoursAdditionFactorAction()),
	HOURS_SUBTRACTION_ACTION(PraeceptaFactorActionType.HOURS_SUBTRACTION, new PraeceptaHoursSubtractionFactorAction()),
	MINUTES_ADD_ACTION(PraeceptaFactorActionType.MINUTES_ADDITION, new PraeceptaMinutesAdditionFactorAction()),
	MINUTES_SUBTRACTION_ACTION(PraeceptaFactorActionType.MINUTES_SUBTRACTION, new PraeceptaMinutesSubtractionFactorAction()),
	YEARS_ADD_ACTION(PraeceptaFactorActionType.YEARS_ADDITION, new PraeceptaYearsAdditionFactorAction()),
	YEARS_SUBTRACTION_ACTION(PraeceptaFactorActionType.YEARS_SUBTRACTION, new PraeceptaYearsSubtractionFactorAction()),
	;
	
	private PraeceptaActionFactorTypeToAction(PraeceptaFactorActionType factorActionType, PraeceptaUnderlyingFactorAction factorAction) {
		this.factorActionType = factorActionType;
		this.factorAction = factorAction;
	}
	
	private PraeceptaFactorActionType factorActionType;
	
	private PraeceptaUnderlyingFactorAction factorAction;
	
	public PraeceptaFactorActionType getadditionalActionType() {
		return factorActionType;
	}

	public void setAdditionalActionType(PraeceptaFactorActionType additionalActionType) {
		this.factorActionType = additionalActionType;
	}

	public PraeceptaUnderlyingFactorAction getAdditionalAction() {
		return factorAction;
	}

	public void setadditionalAction(PraeceptaUnderlyingFactorAction additionalAction) {
		this.factorAction = additionalAction;
	}

	private static Map<PraeceptaFactorActionType, PraeceptaUnderlyingFactorAction> factorActionMap = new HashMap<>();
	
	static {
		for(PraeceptaActionFactorTypeToAction additionalActionTypeToAction : PraeceptaActionFactorTypeToAction.values()) {
			factorActionMap.put(additionalActionTypeToAction.factorActionType, additionalActionTypeToAction.factorAction);
		}
	}
	
	public static PraeceptaUnderlyingFactorAction getActionFactor(PraeceptaFactorActionType factorActionType) {
		return factorActionMap.get(factorActionType);
	}
}

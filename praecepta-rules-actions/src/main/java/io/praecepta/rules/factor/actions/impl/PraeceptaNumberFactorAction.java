package io.praecepta.rules.factor.actions.impl;

import io.praecepta.rules.factor.actions.PraeceptaUnderlyingFactorAction;

public abstract class PraeceptaNumberFactorAction implements PraeceptaUnderlyingFactorAction{
	
	public final boolean validateDataType(Object value, Object factorValue) {
		return value instanceof Number && factorValue instanceof Number;
	}

}

package io.praecepta.rules.factor.actions.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import junit.framework.Assert;

public class PraeceptaMultiplicationFactorActionTest {
	
	@Test
	public void testPeformAction() {
		PraeceptaMultiplicationFactorAction additionalMultiplicationAction = new PraeceptaMultiplicationFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE, 4);
		PraeceptaActionHolder value = additionalMultiplicationAction.performAction(24, parametersMap);
		Assert.assertEquals("96", value.getActionedValue().toString());
	}
	
	@Test
	public void testPeformAction_invalid() {
		PraeceptaMultiplicationFactorAction additionalMultiplicationAction = new PraeceptaMultiplicationFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE, 4);
		PraeceptaActionHolder value = additionalMultiplicationAction.performAction("24", parametersMap);
		Assert.assertNull(value);
	}

}

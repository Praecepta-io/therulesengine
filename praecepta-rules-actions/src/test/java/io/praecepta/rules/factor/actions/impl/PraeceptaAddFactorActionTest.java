package io.praecepta.rules.factor.actions.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import junit.framework.Assert;

public class PraeceptaAddFactorActionTest {
	
	@Test
	public void testPeformAction() {
		PraeceptaAddFactorAction additionalAddAction = new PraeceptaAddFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE, 25.4);
		PraeceptaActionHolder value = additionalAddAction.performAction(23.7, parametersMap);
		Assert.assertEquals("49.1", value.getActionedValue().toString());
	}
	
	@Test
	public void testPeformAction_invalid() {
		PraeceptaAddFactorAction additionalAddAction = new PraeceptaAddFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE, "ee");
		Object value = additionalAddAction.performAction(23.7, parametersMap);
		Assert.assertNull(value);
	}

}

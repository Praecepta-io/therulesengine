package io.praecepta.rules.factor.actions.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import junit.framework.Assert;

public class PraeceptaDivisionFactorActionTest {
	
	@Test
	public void testPeformAction() {
		PraeceptaDivisionFactorAction additionalDivisionAction = new PraeceptaDivisionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE, 4);
		PraeceptaActionHolder value = additionalDivisionAction.performAction(24, parametersMap);
		Assert.assertEquals("6", value.getActionedValue().toString());
	}

}

package io.praecepta.rules.factor.actions.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import junit.framework.Assert;

public class PraeceptaSubtractionFactorActionTest {
	
	@Test
	public void testPeformAction() {
		PraeceptaSubtractionFactorAction additionalSubtractionAction = new PraeceptaSubtractionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE, 23.7);
		PraeceptaActionHolder value = additionalSubtractionAction.performAction(25.4, parametersMap);
		Assert.assertEquals("1.7", value.getActionedValue().toString());
	}

}

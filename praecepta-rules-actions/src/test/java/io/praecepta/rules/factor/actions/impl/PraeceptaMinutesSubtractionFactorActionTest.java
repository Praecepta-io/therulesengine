package io.praecepta.rules.factor.actions.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import junit.framework.Assert;

public class PraeceptaMinutesSubtractionFactorActionTest {
	@Test
	public void testPeformAction() {
		PraeceptaMinutesSubtractionFactorAction additionalMinutesSubtractionAction = new PraeceptaMinutesSubtractionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,5);
		PraeceptaActionHolder value = additionalMinutesSubtractionAction.performAction("2023-10-11", parametersMap);
		Assert.assertEquals("2023-10-10", value.getActionedValue());
	}
	
	@Test
	public void testPeformAction_withdateformat1() {
		PraeceptaMinutesSubtractionFactorAction additionalMinutesSubtractionAction = new PraeceptaMinutesSubtractionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,24);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy/MM/dd");
		PraeceptaActionHolder value = additionalMinutesSubtractionAction.performAction("2023/10/11", parametersMap);
		Assert.assertEquals("2023/10/10", value.getActionedValue());
	}
	
	@Test
	public void testPeformAction_withdateformat2() {
		PraeceptaMinutesSubtractionFactorAction additionalMinutesSubtractionAction = new PraeceptaMinutesSubtractionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,3);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy-MM-dd HH:mm:ss");
		PraeceptaActionHolder value = additionalMinutesSubtractionAction.performAction("2023-10-11 21:22:11", parametersMap);
		Assert.assertEquals("2023-10-11 21:19:11", value.getActionedValue());
	}
	
	
	@Test
	public void testPeformAction_withdateformat3() {
		PraeceptaMinutesSubtractionFactorAction additionalMinutesSubtractionAction = new PraeceptaMinutesSubtractionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,3);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy-MM-dd HH:mm:ss");
		parametersMap.put(PraeceptaActionParametersType.TO_DATE_FORMAT,"yyyy/MM/dd HH:mm:ss");
		PraeceptaActionHolder value = additionalMinutesSubtractionAction.performAction("2023-10-11 21:22:11", parametersMap);
		Assert.assertEquals("2023/10/11 21:19:11", value.getActionedValue());
	}
}

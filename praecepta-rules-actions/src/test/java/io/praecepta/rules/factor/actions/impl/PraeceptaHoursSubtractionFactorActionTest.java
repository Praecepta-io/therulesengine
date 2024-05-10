package io.praecepta.rules.factor.actions.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import junit.framework.Assert;

public class PraeceptaHoursSubtractionFactorActionTest {
	@Test
	public void testPeformAction() {
		PraeceptaHoursSubtractionFactorAction additionalHoursSubtractionAction = new PraeceptaHoursSubtractionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,5);
		PraeceptaActionHolder value = additionalHoursSubtractionAction.performAction("2023-10-11", parametersMap);
		Assert.assertEquals("2023-10-10", value.getActionedValue());
	}
	
	@Test
	public void testPeformAction_withdateformat1() {
		PraeceptaHoursSubtractionFactorAction additionalHoursSubtractionAction = new PraeceptaHoursSubtractionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,24);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy/MM/dd");
		PraeceptaActionHolder value = additionalHoursSubtractionAction.performAction("2023/10/11", parametersMap);
		Assert.assertEquals("2023/10/10", value.getActionedValue());
	}
	
	@Test
	public void testPeformAction_withdateformat2() {
		PraeceptaHoursSubtractionFactorAction additionalHoursSubtractionAction = new PraeceptaHoursSubtractionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,3);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy-MM-dd HH:mm:ss");
		PraeceptaActionHolder value = additionalHoursSubtractionAction.performAction("2023-10-11 21:22:11", parametersMap);
		Assert.assertEquals("2023-10-11 18:22:11", value.getActionedValue());
	}
	
	
	@Test
	public void testPeformAction_withdateformat3() {
		PraeceptaHoursSubtractionFactorAction additionalHoursSubtractionAction = new PraeceptaHoursSubtractionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,3);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy-MM-dd HH:mm:ss");
		parametersMap.put(PraeceptaActionParametersType.TO_DATE_FORMAT,"yyyy/MM/dd HH:mm:ss");
		PraeceptaActionHolder value = additionalHoursSubtractionAction.performAction("2023-10-11 21:22:11", parametersMap);
		Assert.assertEquals("2023/10/11 18:22:11", value.getActionedValue());
	}
}

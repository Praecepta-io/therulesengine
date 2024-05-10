package io.praecepta.rules.factor.actions.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import junit.framework.Assert;

public class PraeceptaDaysSubtractionFactorActionTest {
	@Test
	public void testPeformAction() {
		PraeceptaDaysSubtractionFactorAction AdditionalDaysSubtractionAction = new PraeceptaDaysSubtractionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,5);
		PraeceptaActionHolder value = AdditionalDaysSubtractionAction.performAction("2023-10-11", parametersMap);
		Assert.assertEquals("2023-10-06", value.getActionedValue());
	}
	
	@Test
	public void testPeformAction_withdateformat1() {
		PraeceptaDaysSubtractionFactorAction additionalDaysSubtractionAction = new PraeceptaDaysSubtractionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,3);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy/MM/dd");
		PraeceptaActionHolder value = additionalDaysSubtractionAction.performAction("2023/10/11", parametersMap);
		Assert.assertEquals("2023/10/08", value.getActionedValue());
	}
	
	@Test
	public void testPeformAction_withdateformat2() {
		PraeceptaDaysSubtractionFactorAction additionalDaysSubtractionAction = new PraeceptaDaysSubtractionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,3);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy-MM-dd HH:mm:ss");
		PraeceptaActionHolder value = additionalDaysSubtractionAction.performAction("2023-10-11 21:22:11", parametersMap);
		Assert.assertEquals("2023-10-08 21:22:11", value.getActionedValue());
	}
	
	
	@Test
	public void testPeformAction_withdateformat3() {
		PraeceptaDaysSubtractionFactorAction additionalDaysSubtractionAction = new PraeceptaDaysSubtractionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,3);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy-MM-dd HH:mm:ss");
		parametersMap.put(PraeceptaActionParametersType.TO_DATE_FORMAT,"yyyy/MM/dd HH:mm:ss");
		PraeceptaActionHolder value = additionalDaysSubtractionAction.performAction("2023-10-11 21:22:11", parametersMap);
		Assert.assertEquals("2023/10/08 21:22:11", value.getActionedValue());
	}
}

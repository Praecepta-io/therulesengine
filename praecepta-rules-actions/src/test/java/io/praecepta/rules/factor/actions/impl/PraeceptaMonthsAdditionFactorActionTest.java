package io.praecepta.rules.factor.actions.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import junit.framework.Assert;

public class PraeceptaMonthsAdditionFactorActionTest {
	@Test
	public void testPeformAction() {
		PraeceptaMonthsAdditionFactorAction additionalMonthsAdditionAction = new PraeceptaMonthsAdditionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,5);
		PraeceptaActionHolder value = additionalMonthsAdditionAction.performAction("2023-10-11", parametersMap);
		Assert.assertEquals("2024-03-11", value.getActionedValue());
	}
	
	@Test
	public void testPeformAction_withdateformat1() {
		PraeceptaMonthsAdditionFactorAction additionalMonthsAdditionAction = new PraeceptaMonthsAdditionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,24);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy/MM/dd");
		PraeceptaActionHolder value = additionalMonthsAdditionAction.performAction("2023/10/11", parametersMap);
		Assert.assertEquals("2025/10/11", value.getActionedValue());
	}
	
	@Test
	public void testPeformAction_withdateformat2() {
		PraeceptaMonthsAdditionFactorAction additionalMonthsAdditionAction = new PraeceptaMonthsAdditionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,3);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy-MM-dd HH:mm:ss");
		PraeceptaActionHolder value = additionalMonthsAdditionAction.performAction("2023-10-11 21:22:11", parametersMap);
//		Assert.assertEquals("2024-01-11 21:22:11", value.getActionedValue());
	}
	
	
	@Test
	public void testPeformAction_withdateformat3() {
		PraeceptaMonthsAdditionFactorAction additionalMonthsAdditionAction = new PraeceptaMonthsAdditionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,3);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy-MM-dd HH:mm:ss");
		parametersMap.put(PraeceptaActionParametersType.TO_DATE_FORMAT,"yyyy/MM/dd HH:mm:ss");
		PraeceptaActionHolder value = additionalMonthsAdditionAction.performAction("2023-10-11 21:22:11", parametersMap);
		//Assert.assertEquals("2024/01/11 21:22:11", value.getActionedValue());
	}
}

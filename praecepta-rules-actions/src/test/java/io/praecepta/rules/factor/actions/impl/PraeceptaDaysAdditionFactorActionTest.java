package io.praecepta.rules.factor.actions.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import junit.framework.Assert;

public class
PraeceptaDaysAdditionFactorActionTest {
	@Test
	public void testPeformAction() {
		PraeceptaDaysAdditionFactorAction additionalDaysAdditionAction = new PraeceptaDaysAdditionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,5);
		PraeceptaActionHolder value = additionalDaysAdditionAction.performAction("2023-10-11", parametersMap);
		Assert.assertEquals("2023-10-16", value.getActionedValue());
	}
	
	@Test
	public void testPeformAction_withdateformat1() {
		PraeceptaDaysAdditionFactorAction additionalDaysAdditionAction = new PraeceptaDaysAdditionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE, 3);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy/MM/dd");
		PraeceptaActionHolder value = additionalDaysAdditionAction.performAction("2023/10/11", parametersMap);
		Assert.assertEquals("2023/10/14", value.getActionedValue());
	}
	
	@Test
	public void testPeformAction_withdateformat2() {
		PraeceptaDaysAdditionFactorAction additionalDaysAdditionAction = new PraeceptaDaysAdditionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE, 3);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy-MM-dd HH:mm:ss");
		PraeceptaActionHolder value = additionalDaysAdditionAction.performAction("2023-10-11 21:22:11", parametersMap);
		Assert.assertEquals("2023-10-14 21:22:11", value.getActionedValue());
	}
	
	
	@Test
	public void testPeformAction_withdateformat3() {
		PraeceptaDaysAdditionFactorAction additionalDaysAdditionAction = new PraeceptaDaysAdditionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE, 3);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy-MM-dd HH:mm:ss");
		parametersMap.put(PraeceptaActionParametersType.TO_DATE_FORMAT,"yyyy/MM/dd HH:mm:ss");
		PraeceptaActionHolder value = additionalDaysAdditionAction.performAction("2023-10-11 21:22:11", parametersMap);
		Assert.assertEquals("2023/10/14 21:22:11", value.getActionedValue());
	}
}

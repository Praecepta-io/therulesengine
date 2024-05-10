package io.praecepta.rules.factor.actions.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.actions.enums.PraeceptaActionParametersType;
import junit.framework.Assert;

public class PraeceptaHoursAdditionFactorActionTest {
	@Test
	public void testPeformAction() {
		PraeceptaHoursAdditionFactorAction additionalHoursAdditionAction = new PraeceptaHoursAdditionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,5);
		PraeceptaActionHolder value = additionalHoursAdditionAction.performAction("2023-10-11", parametersMap);
		Assert.assertEquals("2023-10-11", value.getActionedValue());
	}
	
	@Test
	public void testPeformAction_withdateformat1() {
		PraeceptaHoursAdditionFactorAction additionalHoursAdditionAction = new PraeceptaHoursAdditionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,24);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy/MM/dd");
		PraeceptaActionHolder value = additionalHoursAdditionAction.performAction("2023/10/11", parametersMap);
		Assert.assertEquals("2023/10/12", value.getActionedValue());
	}
	
	@Test
	public void testPeformAction_withdateformat2() {
		PraeceptaHoursAdditionFactorAction additionalHoursAdditionAction = new PraeceptaHoursAdditionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,3);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy-MM-dd HH:mm:ss");
		PraeceptaActionHolder value = additionalHoursAdditionAction.performAction("2023-10-11 21:22:11", parametersMap);
		Assert.assertEquals("2023-10-12 00:22:11", value.getActionedValue());
	}
	
	
	@Test
	public void testPeformAction_withdateformat3() {
		PraeceptaHoursAdditionFactorAction additionalHoursAdditionAction = new PraeceptaHoursAdditionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,3);
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy-MM-dd HH:mm:ss");
		parametersMap.put(PraeceptaActionParametersType.TO_DATE_FORMAT,"yyyy/MM/dd HH:mm:ss");
		PraeceptaActionHolder value = additionalHoursAdditionAction.performAction("2023-10-11 21:22:11", parametersMap);
		Assert.assertEquals("2023/10/12 00:22:11", value.getActionedValue());
	}
	
	
	@Test
	public void testPeformAction_withdateformat4() {
		PraeceptaHoursAdditionFactorAction additionalHoursAdditionAction = new PraeceptaHoursAdditionFactorAction();
		Map<PraeceptaActionParametersType,Object> parametersMap = new HashMap<>();
		parametersMap.put(PraeceptaActionParametersType.FACTOR_VALUE,"a");
		parametersMap.put(PraeceptaActionParametersType.FROM_DATE_FORMAT,"yyyy-MM-dd HH:mm:ss");
		parametersMap.put(PraeceptaActionParametersType.TO_DATE_FORMAT,"yyyy/MM/dd HH:mm:ss");
		PraeceptaActionHolder value = additionalHoursAdditionAction.performAction("2023-10-11 21:22:11", parametersMap);
		Assert.assertNull( value);
	}
}

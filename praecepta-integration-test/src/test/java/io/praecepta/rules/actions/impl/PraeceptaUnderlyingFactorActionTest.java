package io.praecepta.rules.actions.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.rules.actions.PraeceptaActionHolder;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.factor.actions.PraeceptaUnderlyingFactorAction;
import io.praecepta.rules.model.PraeceptaActionResult.ACTION_EXECUTION_STATUS;

public class PraeceptaUnderlyingFactorActionTest {

	@Test
	public void test() {

		PraeceptaActionHolder actionHolder = PraeceptaUnderlyingFactorAction.getActionHolder();
		
		assertNotNull(actionHolder);
	}
	
	@Test
	public void testHolderStatus() {
		
		PraeceptaActionHolder actionHolder = PraeceptaUnderlyingFactorAction.getActionHolder();
		
		PraeceptaUnderlyingFactorAction.addStatusAndMessageToActonHolder(actionHolder, ACTION_EXECUTION_STATUS.SUCCESS, PraeceptaValueAssignAction.DEFAULT_ACTION_MESSAGE);
		
		assertNotNull(actionHolder);
		
		assertNotNull(actionHolder.getActionResult());
		
		assertNotNull(actionHolder.getActionResult().getActionResultStatusMessage());
		
		assertEquals(PraeceptaValueAssignAction.DEFAULT_ACTION_MESSAGE, actionHolder.getActionResult().getActionResultStatusMessage());
	}
	
	@Test
	public void testHolderStatusFromStore() {
		
		PraeceptaRequestStore requestStore = new PraeceptaRequestStore();
		
		requestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_INFO, 
				PraeceptaUnderlyingFactorAction.getActionHolder());
		
		PraeceptaActionHolder actionHolder = (PraeceptaActionHolder) requestStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_ACTION_INFO);
		
		PraeceptaUnderlyingFactorAction.addStatusAndMessageToActonHolder(actionHolder, ACTION_EXECUTION_STATUS.SUCCESS, PraeceptaValueAssignAction.DEFAULT_ACTION_MESSAGE);
		
		assertNotNull(actionHolder);
		
		assertNotNull(actionHolder.getActionResult());
		
		assertNotNull(actionHolder.getActionResult().getActionResultStatusMessage());
		
		assertEquals(PraeceptaValueAssignAction.DEFAULT_ACTION_MESSAGE, actionHolder.getActionResult().getActionResultStatusMessage());
	}

}

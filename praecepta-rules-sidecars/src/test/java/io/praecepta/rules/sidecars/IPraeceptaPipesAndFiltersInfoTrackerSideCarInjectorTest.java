package io.praecepta.rules.sidecars;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.rules.sidecars.enums.PraeceptaSideCarStoreType;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class IPraeceptaPipesAndFiltersInfoTrackerSideCarInjectorTest {

	@Test
	public void testAddParentHolderToCurrent() {

		PraeceptaSideCarDataHolder<String , String> parentSideCarHolder = new PraeceptaSideCarDataHolder<>();
		
		parentSideCarHolder.addInput("parentInput");
		parentSideCarHolder.addOutput("parentOutput");
		
		
		PraeceptaSideCarDataHolder<Integer , Integer> childSideCarHolder = new PraeceptaSideCarDataHolder<>();
		
		childSideCarHolder.addInput(10);
		childSideCarHolder.addOutput(20);
		
		PraeceptaRequestStore ruleStore = new PraeceptaRequestStore();
		
		ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER, parentSideCarHolder);
		ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.CURRENT_SIDECAR_HOLDER, childSideCarHolder);
		
		System.out.println(" Child Info from Parent - " +parentSideCarHolder.getNextSideCarDataHolder());
		System.out.println(" Parent Info from Child - " +childSideCarHolder.getParentSideCarDataHolder());
		
		assertNull(parentSideCarHolder.getNextSideCarDataHolder());
		assertNull(childSideCarHolder.getParentSideCarDataHolder());
		
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.addParentHolderToCurrent(ruleStore);
		
		System.out.println(" Child Info from Parent - " +parentSideCarHolder.getNextSideCarDataHolder());
		System.out.println(" Parent Info from Child - " +childSideCarHolder.getParentSideCarDataHolder());
		
		assertNotNull(parentSideCarHolder.getNextSideCarDataHolder());
		assertNotNull(childSideCarHolder.getParentSideCarDataHolder());
	}
	
	@Test
	public void testNextHolderToCurrent() {
		
		PraeceptaSideCarDataHolder<String , String> currentSideCarHolder = new PraeceptaSideCarDataHolder<>();
		
		currentSideCarHolder.addInput("currentInput");
		currentSideCarHolder.addOutput("currentOutput");
		
		
		PraeceptaSideCarDataHolder<Integer , Integer> nextSideCarHolder = new PraeceptaSideCarDataHolder<>();
		
		nextSideCarHolder.addInput(10);
		nextSideCarHolder.addOutput(20);
		
		PraeceptaRequestStore ruleStore = new PraeceptaRequestStore();
		
		ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.CURRENT_SIDECAR_HOLDER, currentSideCarHolder);
		ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.NEXT_SIDECAR_HOLDER, nextSideCarHolder);
		
		System.out.println(" Grand Child Info from Child - " +currentSideCarHolder.getNextSideCarDataHolder());
		System.out.println(" Child Info from Grand Child - " +nextSideCarHolder.getParentSideCarDataHolder());
		
		assertNull(currentSideCarHolder.getNextSideCarDataHolder());
		assertNull(nextSideCarHolder.getParentSideCarDataHolder());
		
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.addNextHolderToCurrent(ruleStore);
		
		System.out.println(" Grand Child Info from Child -  " +currentSideCarHolder.getNextSideCarDataHolder());
		System.out.println(" Child Info from Grand Child - " +nextSideCarHolder.getParentSideCarDataHolder());
		
		assertNotNull(currentSideCarHolder.getNextSideCarDataHolder());
		assertNotNull(nextSideCarHolder.getParentSideCarDataHolder());
	}

	@Test
	public void testParentAndNextHolderToCurrent() {
		
		PraeceptaSideCarDataHolder<String , String> parentSideCarHolder = new PraeceptaSideCarDataHolder<>();
		
		parentSideCarHolder.addInput("parentInput");
		parentSideCarHolder.addOutput("parentOutput");
		
		PraeceptaSideCarDataHolder<String , String> currentSideCarHolder = new PraeceptaSideCarDataHolder<>();
		
		currentSideCarHolder.addInput("currentInput");
		currentSideCarHolder.addOutput("currentOutput");
		
		
		PraeceptaSideCarDataHolder<Integer , Integer> nextSideCarHolder = new PraeceptaSideCarDataHolder<>();
		
		nextSideCarHolder.addInput(10);
		nextSideCarHolder.addOutput(20);
		
		PraeceptaRequestStore ruleStore = new PraeceptaRequestStore();
		
		ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.PARENT_SIDECAR_HOLDER, parentSideCarHolder);
		ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.CURRENT_SIDECAR_HOLDER, currentSideCarHolder);
		ruleStore.upsertToPraeceptaStore(PraeceptaSideCarStoreType.NEXT_SIDECAR_HOLDER, nextSideCarHolder);
		
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.addParentHolderToCurrent(ruleStore);
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.addNextHolderToCurrent(ruleStore);
		
		System.out.println(" --------------------------------------------- ");

		System.out.println(" Parent Info -  " +parentSideCarHolder);
		System.out.println(" Child Info - " +parentSideCarHolder.getNextSideCarDataHolder());
		System.out.println(" Grand Child Info - " +parentSideCarHolder.getNextSideCarDataHolder().getNextSideCarDataHolder());
		
		assertNotNull(parentSideCarHolder.getNextSideCarDataHolder());
		assertNotNull(parentSideCarHolder.getNextSideCarDataHolder().getNextSideCarDataHolder());
		
		System.out.println(" --------------------------------------------- ");
		
		System.out.println(" Child Info -  " +currentSideCarHolder);
		System.out.println(" Parent Info - " +currentSideCarHolder.getParentSideCarDataHolder());
		System.out.println(" Grand Child Info - " +parentSideCarHolder.getNextSideCarDataHolder());
		
		assertNotNull(currentSideCarHolder.getParentSideCarDataHolder());
		assertNotNull(parentSideCarHolder.getNextSideCarDataHolder());

		System.out.println(" --------------------------------------------- ");

		System.out.println(" Grand Child Info -  " +nextSideCarHolder);
		System.out.println(" Child Info - " +nextSideCarHolder.getParentSideCarDataHolder());
		System.out.println(" Parent Info - " +nextSideCarHolder.getParentSideCarDataHolder().getParentSideCarDataHolder());
		
		assertNotNull(nextSideCarHolder.getParentSideCarDataHolder());
		assertNotNull(nextSideCarHolder.getParentSideCarDataHolder().getParentSideCarDataHolder());
	}
	
}

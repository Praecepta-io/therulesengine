package io.praecepta.rules.sidecars.defaultsidecars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.rules.engine.rulegrp.sidecars.PraeceptaDefaultPreRuleGrpSideCarInjector;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.engine.sidecars.IPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.model.PraeceptaCriteria;

public class PraeceptaDefaultPreRuleGrpSideCarInjectorTest {

	@Test
	public void test() {
		
		String ruleGrpName = "testRuleGrpName";
		
		PraeceptaRequestStore ruleStore = new  PraeceptaRequestStore();
		
		GenericPraeceptaInfoTrackerSideCarInjector preRuleGrpSideCarInjector = new PraeceptaDefaultPreRuleGrpSideCarInjector();
		
		List<IPraeceptaInfoTrackerSideCarInjector> beforeExecutionSideCars = Arrays.asList(new GenericPraeceptaInfoTrackerSideCarInjector(), new GenericPraeceptaInfoTrackerSideCarInjector());

		List<IPraeceptaInfoTrackerSideCarInjector> afterExecutionSideCars = Arrays.asList(new GenericPraeceptaInfoTrackerSideCarInjector(), new GenericPraeceptaInfoTrackerSideCarInjector());
	
		/*preRuleGrpSideCarInjector.setBeforeExecutionSideCars(beforeExecutionSideCars);
		preRuleGrpSideCarInjector.setAfterExecutionSideCars(afterExecutionSideCars);*/
		
		PraeceptaRuleGroup ruleGrpToUse = new PraeceptaRuleGroup("Space1","ClNT1", "APP1");
		
		List<PraeceptaCriteria> praeceptaCriterias = new ArrayList<>();
		
		PraeceptaCriteria criteria1 = new PraeceptaCriteria();
		
		PraeceptaCriteria criteria2 = new PraeceptaCriteria();
		
		praeceptaCriterias.add(criteria1);
		praeceptaCriterias.add(criteria2);
		
		ruleGrpToUse.setRuleGroupName(ruleGrpName);
		ruleGrpToUse.setPraeceptaCriterias(praeceptaCriterias);
		
		PraeceptaRuleSpace ruleSpace = new PraeceptaRuleSpace();
		
		List<PraeceptaRuleGroup> praeceptaRuleGrps = new ArrayList<>();
		praeceptaRuleGrps.add(ruleGrpToUse);
		
		ruleSpace.setPraeceptaRuleGrps(praeceptaRuleGrps);
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_SPACE, ruleSpace);
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_NAME, ruleGrpName);
		
		preRuleGrpSideCarInjector.trackAndCaptureInitialInfo(ruleStore);
		
		preRuleGrpSideCarInjector.trackAndCaptureExitInfo(ruleStore);
		
		Map<String, Object> ruleGroupMetaData =  (Map<String, Object>) ruleStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_METADATA);
		
		System.out.println(ruleGroupMetaData);
	}

}

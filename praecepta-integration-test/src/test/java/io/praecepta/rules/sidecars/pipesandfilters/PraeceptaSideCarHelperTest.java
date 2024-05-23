package io.praecepta.rules.sidecars.pipesandfilters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.sidecars.IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.enricher.PraeceptaEnricherSideCarInjector;
import io.praecepta.rules.sidecars.enricher.PraeceptaEnricherTypeRegistry.PraeceptaEnricherType;
import io.praecepta.rules.sidecars.enums.PraeceptaDefaultSideCarClazzType;
import io.praecepta.rules.sidecars.formatter.PraeceptaFormatterTypeRegistry.PraeceptaFormatterType;
import io.praecepta.rules.sidecars.helper.PraeceptaSideCarHelper;
import io.praecepta.rules.sidecars.model.PraeceptaSideCarMetadata;
import io.praecepta.rules.sidecars.parser.PraeceptaParserSideCarInjector;
import io.praecepta.rules.sidecars.parser.PraeceptaParserTypeRegistry.PraeceptaParserType;

public class PraeceptaSideCarHelperTest {

	@Test
	public void testConvertSideCarMetadataToSideCarsForAStore() {

		PraeceptaRequestStore ruleRequestStore = new PraeceptaRequestStore();
		
		// Pre Rule Grp SideCar Meta Data
		PraeceptaSideCarMetadata preRuleGrpSideCarMetaData = getPreRuleGrpSideCarMetaData();
		
		ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_GROUP_EXECUTION_SIDE_CARS_META_DATA, 
				Arrays.asList(preRuleGrpSideCarMetaData));
		
		// Post Rule SideCar Mata Data
		PraeceptaSideCarMetadata postRuleGrpSideCarMetaData = getPostRuleGrpSideCarMetaData();
		
		ruleRequestStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_GROUP_EXECUTION_SIDE_CARS, 
				Arrays.asList(postRuleGrpSideCarMetaData));
		
		// Pre Rule Side Car Meta Data and Rule Name
		
		// Rule Level Sidecars Start here
			String ruleName = "restrictedTransfer";
			
			//Pre rule side car
			PraeceptaSideCarMetadata preRuleSideCarMetaData = getPreRuleSideCarMetaData();
			
			Map<String, List<PraeceptaSideCarMetadata>> ruleNameToPreRuleSideCarsMetaData = new HashMap<>(1);
			
			ruleNameToPreRuleSideCarsMetaData.put(ruleName, Arrays.asList(preRuleSideCarMetaData));
			
			ruleRequestStore
			.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_EXECUTION_SIDE_CARS_META_DATA, ruleNameToPreRuleSideCarsMetaData);
			
			// Post rule side car
			PraeceptaSideCarMetadata postRuleSideCar = getPostRuleSideCarMetaData();

			Map<String, List<PraeceptaSideCarMetadata>> ruleNameToPostRuleSideCarsMetaData = new HashMap<>(1);
			
			ruleNameToPostRuleSideCarsMetaData.put(ruleName, Arrays.asList(postRuleSideCar));
			
			ruleRequestStore
			.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_EXECUTION_SIDE_CARS_META_DATA, ruleNameToPreRuleSideCarsMetaData);
			
		// Rule Level Sidecars End here
		
		PraeceptaSideCarHelper.convertSideCarMetadataToSideCarsForAStore(ruleRequestStore);
		
		// Pre Rule Grp Side Cars Start Here
		List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector> preRuleGrpSideCars = 
				(List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>) 
				ruleRequestStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_GROUP_EXECUTION_SIDE_CARS);
		
		assertNotNull(preRuleGrpSideCars);
		
		assertEquals(1, preRuleGrpSideCars.size());
		// Pre Rule Grp Side Cars End here 
		
		
		// Pre Rule Side Cars Start Here
		Map<String, List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>> preRuleSideCars = 
				(Map<String, List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>>) 
				ruleRequestStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_EXECUTION_SIDE_CARS);
		
		assertNotNull(preRuleSideCars);
		
		assertEquals(1, preRuleSideCars.size());

		assertNotNull(preRuleSideCars.get(ruleName));
		// Pre Rule Side Cars End here 
		
		// Post Rule Side Cars Start Here
		Map<String, List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>> postRuleSideCars = 
				(Map<String, List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>>) 
				ruleRequestStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_EXECUTION_SIDE_CARS);
		
		assertNotNull(postRuleSideCars);

		assertEquals(1, postRuleSideCars.size());

		assertNotNull(postRuleSideCars.get(ruleName));
		// Post Rule Side Cars End here 
		
		// Post Rule Grp Side Cars Start Here
		List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector> postRuleGrpSideCars = 
				(List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector>) 
				ruleRequestStore.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_GROUP_EXECUTION_SIDE_CARS);
				
		assertNotNull(postRuleGrpSideCars);
		
		assertEquals(1, postRuleGrpSideCars.size());
		// Post Rule Grp Side Cars End here 
		
	}
	
	@Test
	public void testConvertSideCarMetadataToSideCars() {

		List<PraeceptaSideCarMetadata> ruleGrpSideCarMetaData = new ArrayList<>();
		
		PraeceptaSideCarMetadata preRuleGrpSideCarMetaData = getPreRuleGrpSideCarMetaData();
		
		ruleGrpSideCarMetaData.add(preRuleGrpSideCarMetaData);
		
		PraeceptaSideCarMetadata postRuleGrpSideCarMetaData = getPostRuleGrpSideCarMetaData();
		
		ruleGrpSideCarMetaData.add(postRuleGrpSideCarMetaData);
		
		List<IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector> pipesAndFiltersSideCars =
			PraeceptaSideCarHelper.convertSideCarMetadataToSideCars(ruleGrpSideCarMetaData);
		
		assertEquals(2, pipesAndFiltersSideCars.size());

		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector parserSideCar = pipesAndFiltersSideCars.get(0);
		
		assertTrue(parserSideCar instanceof PraeceptaParserSideCarInjector);
		
		IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector enricherSideCar = pipesAndFiltersSideCars.get(1);
		
		assertTrue(enricherSideCar instanceof PraeceptaEnricherSideCarInjector);
	}


	private PraeceptaSideCarMetadata getPostRuleGrpSideCarMetaData() {
		PraeceptaSideCarMetadata postRuleGrpSideCar = new PraeceptaSideCarMetadata();
		postRuleGrpSideCar.setOrder(1);
		postRuleGrpSideCar.setType(PraeceptaDefaultSideCarClazzType.ENRICHER.name());
		postRuleGrpSideCar.setSideCarType(PraeceptaEnricherType.SIMPLE_REST_API.name());
		
		Map<String,Object> enrSideCarMetadataMap=new HashMap<>();
		postRuleGrpSideCar.setSideCarConfigs(enrSideCarMetadataMap);
		return postRuleGrpSideCar;
	}


	private PraeceptaSideCarMetadata getPreRuleGrpSideCarMetaData() {
		PraeceptaSideCarMetadata preRuleGrpSideCar = new PraeceptaSideCarMetadata();
		preRuleGrpSideCar.setOrder(1);
		preRuleGrpSideCar.setType(PraeceptaDefaultSideCarClazzType.PARSER.name());
		preRuleGrpSideCar.setSideCarType(PraeceptaParserType.JSON.name());
		
		Map<String, Object> sideCarMetadataMap=new HashMap<>();
		preRuleGrpSideCar.setSideCarConfigs(sideCarMetadataMap);
		return preRuleGrpSideCar;
	}
	

	private PraeceptaSideCarMetadata getPreRuleSideCarMetaData() {
		
		PraeceptaSideCarMetadata preRuleSideCarMetaData = new PraeceptaSideCarMetadata();
		preRuleSideCarMetaData.setOrder(1);
		preRuleSideCarMetaData.setType(PraeceptaDefaultSideCarClazzType.PARSER.name());
		preRuleSideCarMetaData.setSideCarType(PraeceptaFormatterType.YAML.name());
		
		Map<String,Object> preRuleSideCarInfo=new HashMap<>();
		preRuleSideCarMetaData.setSideCarConfigs(preRuleSideCarInfo);
		
		return preRuleSideCarMetaData;
	}
	
	private PraeceptaSideCarMetadata getPostRuleSideCarMetaData() {
		
		PraeceptaSideCarMetadata preRuleSideCarMetaData = new PraeceptaSideCarMetadata();
		preRuleSideCarMetaData.setOrder(1);
		preRuleSideCarMetaData.setType(PraeceptaDefaultSideCarClazzType.FORMATTER.name());
		preRuleSideCarMetaData.setSideCarType(PraeceptaFormatterType.YAML.name());
		
		Map<String,Object> preRuleSideCarInfo=new HashMap<>();
		preRuleSideCarMetaData.setSideCarConfigs(preRuleSideCarInfo);
		
		return preRuleSideCarMetaData;
	}
	


}

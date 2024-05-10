package io.praecepta.rules.hub.datastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;
import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;

public class PraeceptaPivotalRulesHubStoreTest {

	private Map<PraeceptaRuleSpaceCompositeKey, Map<String, PraeceptaRuleSpace>> ruleSpacesData;

	private Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaRuleGroup>>> ruleGrpData;

	private Map<PraeceptaRuleSpaceCompositeKey, Map<String, Collection<PraeceptaSideCarsInfo>>> ruleSideCarData;

	private PraeceptaPivotalRulesHubStore hubStore;

	@Before
	public void init() {
		// <PraeceptaRuleSpaceCompositeKey, <Version, Space>>
		ruleSpacesData = new HashMap<>();

		// <PraeceptaRuleSpaceCompositeKey, <Version, Rule Grps>>
		ruleGrpData = new HashMap<>();

		// <PraeceptaRuleSpaceCompositeKey, <Version, Rule Side Cars>>
		ruleSideCarData = new HashMap<>();

		hubStore = new PraeceptaPivotalRulesHubStore(ruleSpacesData, ruleGrpData, ruleSideCarData);
	}

	@Test
	public void testGetAllActiveObjects() {

		buildInput();
		
		Collection<PraeceptaRuleSpace> spaces = hubStore.getAllActiveRuleSpaces();
		
		assertTrue( spaces.size() > 0 ? true : false);
		
		Collection<PraeceptaRuleGroup> grps = hubStore.getAllActiveRuleGrps();
		
		assertTrue( grps.size() > 0 ? true : false);
		
		Collection<PraeceptaSideCarsInfo> sideCars = hubStore.getAllActiveRuleSideCars();
		
		assertTrue( sideCars.size() > 0 ? true : false);

	}
	
	@Test
	public void testActiveSpacesWithVerison() {
		
		buildInput();
		
		PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey("ABC Tech", "Branch 1",
				"App2");
		
		PraeceptaRuleSpace ruleSpace = hubStore.getActiveRuleSpacesWithVersion(compositeKey, "2V2");
		
		assertNotNull(ruleSpace);

		PraeceptaRuleSpace ruleSpace1 = hubStore.getActiveRuleSpacesWithVersion(compositeKey, "VVV");
		
		assertNull(ruleSpace1);
	}
	
	@Test
	public void testActiveSpacesWithRuleGrpForAVerison() {
		
		buildInput();
		
		PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey("ABC Tech", "Branch 1",
				"App2");
		
		PraeceptaRuleSpace ruleSpace = hubStore.getActiveRuleSpacesWithRuleGrps(compositeKey, "2V2");
		
		assertNotNull(ruleSpace);
		assertNotNull(ruleSpace.getPraeceptaRuleGrps());
		
		List<String> ruleGrpNames = ruleSpace.getPraeceptaRuleGrps().stream().map( (each) -> each.getRuleGroupName()).collect(Collectors.toList());
		
		assertTrue(ruleGrpNames.contains("21RuleGroupName"));
		assertTrue(ruleGrpNames.contains("22RuleGroupName"));

		assertFalse(ruleGrpNames.contains("XXX"));
		
		ruleSpace.getPraeceptaRuleGrps().forEach( (eachRuleGrp) -> System.out.println(eachRuleGrp.getRuleGroupName()));
		
		PraeceptaRuleSpace ruleSpace1 = hubStore.getActiveRuleSpacesWithRuleGrps(compositeKey, "2V4");
		
		assertNotNull(ruleSpace1);
		assertNull(ruleSpace1.getPraeceptaRuleGrps());
	}
	
	@Test
	public void testActiveSpacesWithRuleGrpForAVerisonAndRuleGroupName() {
		
		buildInput();
		
		PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey("ABC Tech", "Branch 1",
				"App2");
		
		PraeceptaRuleGroup ruleGrp = hubStore.getRuleGrpForAndSpaceVersionAndGroupName(compositeKey, "2V2", "21RuleGroupName");
		
		assertNotNull(ruleGrp);
		assertNotNull(ruleGrp.getRuleGroupName());
		assertEquals( "21RuleGroupName",ruleGrp.getRuleGroupName());
		
		
		PraeceptaRuleGroup ruleGrp1 = hubStore.getRuleGrpForAndSpaceVersionAndGroupName(compositeKey, "2V2", "DummyGrpName");
		
		assertNull(ruleGrp1);
	}
	
	@Test
	public void testActiveSpacesWithRuleSideCarsForAVerison() {
		
		buildInput();
		
		PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey("ABC Tech", "Branch 1",
				"App2");
		
		Collection<PraeceptaSideCarsInfo> sideCarsForARuleSpace = hubStore.getActiveSidecarsForARuleSpaces(compositeKey, "2V2");
		
		assertNotNull(sideCarsForARuleSpace);
		
		assertTrue(sideCarsForARuleSpace.size() > 0 ? true : false);
		
		Collection<PraeceptaSideCarsInfo> sideCarsForARuleSpace1 = hubStore.getActiveSidecarsForARuleSpaces(compositeKey, "DummyVersion");
		
		assertTrue(sideCarsForARuleSpace1.size() ==  0 ? true : false);
	}
	
	@Test
	public void testActiveSpacesWithRuleSideCarsForAVerisonAndGroupName() {
		
		buildInput();
		
		PraeceptaRuleSpaceCompositeKey compositeKey = new PraeceptaRuleSpaceCompositeKey("ABC Tech", "Branch 1",
				"App2");
		
		PraeceptaSideCarsInfo sideCarsForARuleSpaceAndGrpName = hubStore.getSideCarsForSpaceAndVersionAndGroupName(compositeKey, "2V2", "21RuleGroupName");
		
		assertNotNull(sideCarsForARuleSpaceAndGrpName);
		
		PraeceptaSideCarsInfo sideCarsForARuleSpaceAndGrpName1 = hubStore.getSideCarsForSpaceAndVersionAndGroupName(compositeKey, "2V2", "DummyVersion");
		
		assertNull(sideCarsForARuleSpaceAndGrpName1);
	}

	private void buildInput() {
		
		// Composite Key 1 Starts Here 
		PraeceptaRuleSpaceCompositeKey compositeKey1 = new PraeceptaRuleSpaceCompositeKey("ABC Tech", "Branch 1",
				"App1");

		Map<String, PraeceptaRuleSpace> versionToSpaceMapForCompositeKey1 = new HashMap<>();

		versionToSpaceMapForCompositeKey1.put("V1", new PraeceptaRuleSpace());
		versionToSpaceMapForCompositeKey1.put("V2", new PraeceptaRuleSpace());
		versionToSpaceMapForCompositeKey1.put("V3", new PraeceptaRuleSpace());

		ruleSpacesData.put(compositeKey1, versionToSpaceMapForCompositeKey1);

		Map<String, Collection<PraeceptaRuleGroup>> versionToRuleGrpsMapForCompositeKey1 = new HashMap<>();

		versionToRuleGrpsMapForCompositeKey1.put("V1", Arrays.asList(buildRuleGroupWithGroupName(compositeKey1, "1RuleGroupName"), buildRuleGroupWithGroupName(compositeKey1, "2RuleGroupName")));
		versionToRuleGrpsMapForCompositeKey1.put("V2", Arrays.asList(buildRuleGroupWithGroupName(compositeKey1, "1RuleGroupName"), buildRuleGroupWithGroupName(compositeKey1, "2RuleGroupName")));
		versionToRuleGrpsMapForCompositeKey1.put("V3", Arrays.asList(buildRuleGroupWithGroupName(compositeKey1, "1RuleGroupNameFor"), buildRuleGroupWithGroupName(compositeKey1, "2RuleGroupNameFor")));

		ruleGrpData.put(compositeKey1, versionToRuleGrpsMapForCompositeKey1);

		Map<String, Collection<PraeceptaSideCarsInfo>> versionToSideCarsMapForCompositeKey1 = new HashMap<>();

		versionToSideCarsMapForCompositeKey1.put("V1", Arrays.asList(buildSideCarwithGrpName(compositeKey1, "1RuleGroupName"), buildSideCarwithGrpName(compositeKey1, "2RuleGroupName")));
		versionToSideCarsMapForCompositeKey1.put("V2", Arrays.asList(buildSideCarwithGrpName(compositeKey1, "1RuleGroupName"), buildSideCarwithGrpName(compositeKey1, "2RuleGroupName")));
		versionToSideCarsMapForCompositeKey1.put("V3", Arrays.asList(buildSideCarwithGrpName(compositeKey1, "1RuleGroupName"), buildSideCarwithGrpName(compositeKey1, "2RuleGroupName")));

		ruleSideCarData.put(compositeKey1, versionToSideCarsMapForCompositeKey1);
		// Composite Key 1 Ends Here 
		
		// Composite Key 2 Starts Here 
		PraeceptaRuleSpaceCompositeKey compositeKey2 = new PraeceptaRuleSpaceCompositeKey("ABC Tech", "Branch 1",
				"App2");
		
		Map<String, PraeceptaRuleSpace> versionToSpaceMapForCompositeKey2 = new HashMap<>();

		versionToSpaceMapForCompositeKey2.put("2V1", new PraeceptaRuleSpace());
		versionToSpaceMapForCompositeKey2.put("2V2", new PraeceptaRuleSpace());
		versionToSpaceMapForCompositeKey2.put("2V3", new PraeceptaRuleSpace());
		versionToSpaceMapForCompositeKey2.put("2V4", new PraeceptaRuleSpace());

		ruleSpacesData.put(compositeKey2, versionToSpaceMapForCompositeKey2);
		
		Map<String, Collection<PraeceptaRuleGroup>> versionToRuleGrpsMapForCompositeKey2 = new HashMap<>();
		
		versionToRuleGrpsMapForCompositeKey2.put("2V1", Arrays.asList(buildRuleGroupWithGroupName(compositeKey2, "21RuleGroupName"), buildRuleGroupWithGroupName(compositeKey2, "22RuleGroupName")));
		versionToRuleGrpsMapForCompositeKey2.put("2V2", Arrays.asList(buildRuleGroupWithGroupName(compositeKey2, "21RuleGroupName"), buildRuleGroupWithGroupName(compositeKey2, "22RuleGroupName")));
		versionToRuleGrpsMapForCompositeKey2.put("2V3", Arrays.asList(buildRuleGroupWithGroupName(compositeKey2, "21RuleGroupNameFor"), buildRuleGroupWithGroupName(compositeKey2, "22RuleGroupNameFor")));
		
		ruleGrpData.put(compositeKey2, versionToRuleGrpsMapForCompositeKey2);

		Map<String, Collection<PraeceptaSideCarsInfo>> versionToSideCarsMapForCompositeKey2 = new HashMap<>();

		versionToSideCarsMapForCompositeKey2.put("2V1", Arrays.asList(buildSideCarwithGrpName(compositeKey2, "21RuleGroupName"), buildSideCarwithGrpName(compositeKey2, "22RuleGroupName")));
		versionToSideCarsMapForCompositeKey2.put("2V2", Arrays.asList(buildSideCarwithGrpName(compositeKey2, "21RuleGroupName"), buildSideCarwithGrpName(compositeKey2, "22RuleGroupName")));
		versionToSideCarsMapForCompositeKey2.put("2V3", Arrays.asList(buildSideCarwithGrpName(compositeKey2, "21RuleGroupName"), buildSideCarwithGrpName(compositeKey2, "22RuleGroupName")));

		ruleSideCarData.put(compositeKey2, versionToSideCarsMapForCompositeKey2);
		// Composite Key 2 Ends Here 
		
		
		// Composite Key 3 Starts Here 
		PraeceptaRuleSpaceCompositeKey compositeKey3 = new PraeceptaRuleSpaceCompositeKey("ABC Tech", "Branch 2",
				"App1");
				
		Map<String, PraeceptaRuleSpace> versionToSpaceMapForCompositeKey3 = new HashMap<>();

		versionToSpaceMapForCompositeKey3.put("3V1", new PraeceptaRuleSpace());
		versionToSpaceMapForCompositeKey3.put("3V2", new PraeceptaRuleSpace());
		versionToSpaceMapForCompositeKey3.put("3V3", new PraeceptaRuleSpace());

		ruleSpacesData.put(compositeKey3, versionToSpaceMapForCompositeKey3);
		
		Map<String, Collection<PraeceptaRuleGroup>> versionToRuleGrpsMapForCompositeKey3 = new HashMap<>();
		
		versionToRuleGrpsMapForCompositeKey3.put("3V1", Arrays.asList(buildRuleGroupWithGroupName(compositeKey3, "31RuleGroupName"), buildRuleGroupWithGroupName(compositeKey3, "32RuleGroupName")));
		versionToRuleGrpsMapForCompositeKey3.put("3V2", Arrays.asList(buildRuleGroupWithGroupName(compositeKey3, "31RuleGroupName"), buildRuleGroupWithGroupName(compositeKey3, "32RuleGroupName")));
		versionToRuleGrpsMapForCompositeKey3.put("3V3", Arrays.asList(buildRuleGroupWithGroupName(compositeKey3, "31RuleGroupNameFor"), buildRuleGroupWithGroupName(compositeKey3, "32RuleGroupNameFor")));
		
		ruleGrpData.put(compositeKey3, versionToRuleGrpsMapForCompositeKey3);

		Map<String, Collection<PraeceptaSideCarsInfo>> versionToSideCarsMapForCompositeKey3 = new HashMap<>();

		versionToSideCarsMapForCompositeKey3.put("3V1", Arrays.asList(buildSideCarwithGrpName(compositeKey3, "31RuleGroupName"), buildSideCarwithGrpName(compositeKey3, "32RuleGroupName")));
		versionToSideCarsMapForCompositeKey3.put("3V2", Arrays.asList(buildSideCarwithGrpName(compositeKey3, "31RuleGroupName"), buildSideCarwithGrpName(compositeKey3, "32RuleGroupName")));
		versionToSideCarsMapForCompositeKey3.put("3V3", Arrays.asList(buildSideCarwithGrpName(compositeKey3, "31RuleGroupName"), buildSideCarwithGrpName(compositeKey3, "32RuleGroupName")));

		ruleSideCarData.put(compositeKey3, versionToSideCarsMapForCompositeKey3);
				// Composite Key 3 Ends Here 		
	}

	private PraeceptaSideCarsInfo buildSideCarwithGrpName(PraeceptaRuleSpaceCompositeKey compositeKey1, String grpName) {
		return new PraeceptaSideCarsInfo(compositeKey1) {

			@Override
			public PraeceptaRuleGroupSideCarsInfo getRuleGroupSideCars() {
				return new PraeceptaRuleGroupSideCarsInfo() {

					@Override
					public String getRuleGrpName() {
						return grpName;
					}

				};
			}

		};
	}

	private PraeceptaRuleGroup buildRuleGroupWithGroupName(PraeceptaRuleSpaceCompositeKey compositeKey1, String grpName) {
		return new PraeceptaRuleGroup(compositeKey1) {

			@Override
			public String getRuleGroupName() {
				return grpName;
			}

		};
	}
	
}

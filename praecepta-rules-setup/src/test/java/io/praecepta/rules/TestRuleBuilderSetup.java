package io.praecepta.rules;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.actions.enums.PraeceptaActionStrategyType;
import io.praecepta.rules.actions.impl.PraeceptaValueAssignAction;
import io.praecepta.rules.builders.PraeceptaRuleBuilder;
import io.praecepta.rules.hub.IPraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.PraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.projection.IPraeceptaAction;
import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;
import io.praecepta.rules.sidecars.model.PraeceptaRuleLevelSideCarsInfo;
import io.praecepta.rules.sidecars.model.PraeceptaSideCarMetadata;

public class TestRuleBuilderSetup {
	
	private static ApplicationContext context =null;
	private static IPraeceptaPivotalRulesHubManager hubManager =null;
	
	@BeforeClass
	public static void init() {
		PraeceptaRuleBuilder.buildWithDefaultProps().buildAll();
		
		context = PraeceptaPivotalRulesHubContextHolder.getHubContext();//new AnnotationConfigApplicationContext(TestRuleBuilderAppConfig.class);

		hubManager = (IPraeceptaPivotalRulesHubManager)context.getBean("pivotalRuleHubManager");
		
	}
	
	boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	
	@Test
	public void testRuleSetup() {
		
		//File fileToDel=new File("D:\\Suri\\test1\\clnt1\\praecepta\\V1");
         
		
		//deleteDirectory(fileToDel);
		
		
		PraeceptaRuleSpace ruleSpace = new PraeceptaRuleSpace();
		ruleSpace.setActive(true);
		PraeceptaRuleSpaceCompositeKey key = new PraeceptaRuleSpaceCompositeKey("test1", "CLNT1", "praecepta");
		ruleSpace.setRuleSpaceKey(key);

		Collection ruleGroups = new ArrayList();
		ruleGroups.add(getRuleGroup("test1", "CLNT1", "praecepta", "RuleGroup1", null));
		ruleGroups.add(getRuleGroup("test1", "CLNT1", "praecepta", "RuleGroup2", null));
		ruleSpace.setPraeceptaRuleGrps(ruleGroups);

		hubManager.createRuleSpace(ruleSpace);

//		PraeceptaRuleSpaceCompositeKey key1 = new PraeceptaRuleSpaceCompositeKey("test1", "CLNT1", "test2");
//		hubManager.createRuleSpace(key1);
//		 
		Collection ruleGroups2 = new ArrayList();
		ruleGroups2.add(getRuleGroup("test1", "CLNT1", "praecepta", "RuleGroup3","V2"));
		ruleGroups2.add(getRuleGroup("test1", "CLNT1", "praecepta", "RuleGroup4","V2"));
		ruleSpace.setPraeceptaRuleGrps(ruleGroups2);
//		
		hubManager.createRuleSpace(ruleSpace, "V2");
//		
//		
		PraeceptaRuleSpace objRuleSpace = hubManager.fetchRuleSpace(key, "V1");

		List<PraeceptaRuleSpace> objRuleSpaces = hubManager.fetchRuleSpace(key);
//		
		assertNotNull(objRuleSpace);
		assertNotNull(objRuleSpaces);
		
		PraeceptaRuleSpaceCompositeKey keyToUpdate=new PraeceptaRuleSpaceCompositeKey("test1", "CLNT1", "praecepta");
		keyToUpdate.setVersion("V3");
		
		objRuleSpace.getPraeceptaRuleGrps().forEach(ruleGrp->addCriteria(ruleGrp));
		
		hubManager.updateRuleSpace(keyToUpdate, objRuleSpace);
		
		PraeceptaRuleSpace objRuleSpaceToUpdate=hubManager.fetchRuleSpace(key, "V3");
		hubManager.updateRuleSpace(objRuleSpaceToUpdate, true);
	
		hubManager.deleteRuleSpace(objRuleSpaceToUpdate);
		
		PraeceptaRuleSpaceCompositeKey testSidecarKey=new PraeceptaRuleSpaceCompositeKey("test1", "CLNT1", "praecepta");
		testSidecarKey.setVersion("V1");
		
		PraeceptaSideCarsInfo ruleSideCarObj=	getSideCarsObject(testSidecarKey);
		
		hubManager.createRuleSideCars(ruleSideCarObj);
		
		
		hubManager.deleteRuleSideCars(ruleSideCarObj);
		//ruleBuilder.createRuleGrp(null);
		
		
		//ruleBuilder.deleteRuleSpace(key, objRuleSpace);
		
		//System.out.println(ruleSpaces);
        //System.out.println(objRuleSpace);
		System.out.println("done");
	}
	
	//@Test
	public void testPerformRuleSpaceUpsert() {
		try {
			PraeceptaRuleSpaceCompositeKey key = new PraeceptaRuleSpaceCompositeKey("test1", "005", "FXL");
			PraeceptaRuleSpace objRuleSpace=hubManager.fetchRuleSpace(key, "V2");
			Method methodToUpdate=PraeceptaPivotalRulesHubManager.class.getDeclaredMethod("performRuleSpaceUpsert", PraeceptaRuleSpace.class,boolean.class);
			methodToUpdate.setAccessible(true);
			methodToUpdate.invoke(hubManager,objRuleSpace, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static PraeceptaRuleGroup getRuleGroup(String spaceName,String clientId,String appName,String ruleGroupName,String version) {
		PraeceptaRuleGroup ruleGroup2 = new PraeceptaRuleGroup(spaceName, clientId, appName);
		ruleGroup2.setActive(true);
		if(!PraeceptaObjectHelper.isStringEmpty(version)) {
			ruleGroup2.getRuleSpaceKey().setVersion(version);
		}
		ruleGroup2.setRuleGroupName(ruleGroupName);
		Collection criterias1 = new ArrayList<>();
		criterias1.add(getCriteria("Rule1"));
		criterias1.add(getCriteria("Rule2"));
		ruleGroup2.setPraeceptaCriterias(criterias1);
		return ruleGroup2;
	}
	
	private static PraeceptaCriteria getCriteria(String ruleName) {
		PraeceptaCriteria criteria1 = new PraeceptaCriteria();
		criteria1.setRuleName(ruleName);
		return criteria1;
	}
	
	//@Test
	public void testRuleSideCarsSetup() {
		
		PraeceptaSideCarsInfo ruleSideCars=new PraeceptaSideCarsInfo("test1", "CLNT1", "praecepta");
		ruleSideCars.setActive(true);
		//ruleSideCars.getRuleGroupSideCars().setRuleGrpName("RuleGroup2");
		ruleSideCars.setRuleGroupSideCars(getRuleGroupSideCars("RuleGroup2"));
		
		hubManager.createRuleSideCars(ruleSideCars);
		
		PraeceptaRuleSpaceCompositeKey key = new PraeceptaRuleSpaceCompositeKey("test1", "CLNT1", "praecepta");
		
		List<PraeceptaSideCarsInfo> ruleSideCarsFrmDB=hubManager.fetchRuleSideCars(key, key.getVersion());
		
		Assert.assertNotNull(ruleSideCarsFrmDB);
		
	}
	
	private PraeceptaRuleGroupSideCarsInfo getRuleGroupSideCars(String ruleGroupName) {
		PraeceptaRuleGroupSideCarsInfo ruleGrpSidecras=new PraeceptaRuleGroupSideCarsInfo();
		ruleGrpSidecras.setRuleGrpName(ruleGroupName);
		
		//Pre RuleGroup Side car
		PraeceptaSideCarMetadata preRuleGrpSideCar1=new PraeceptaSideCarMetadata();
		preRuleGrpSideCar1.setOrder(1);
		preRuleGrpSideCar1.setSideCarType("PARSER");
		preRuleGrpSideCar1.setType("JSON");
		
		Map<String,Object> sideCarMetadataMap=new HashMap<>();
		preRuleGrpSideCar1.setSideCarConfigs(sideCarMetadataMap);
		
		ruleGrpSidecras.setPreRuleGrpSideCars(Arrays.asList(preRuleGrpSideCar1));
		
		//Post rule grp side car
		PraeceptaSideCarMetadata postRuleGrpSideCar1=new PraeceptaSideCarMetadata();
		postRuleGrpSideCar1.setOrder(1);
		postRuleGrpSideCar1.setSideCarType("ENRICHER");
		postRuleGrpSideCar1.setType("SIMPLE_REST");
		
		Map<String,Object> enrSideCarMetadataMap=new HashMap<>();
		postRuleGrpSideCar1.setSideCarConfigs(enrSideCarMetadataMap);
		
		ruleGrpSidecras.setPostRuleGrpSideCars(Arrays.asList(postRuleGrpSideCar1));
		
		//Rule level Side cars
		PraeceptaRuleLevelSideCarsInfo ruleLevelSideCars=new PraeceptaRuleLevelSideCarsInfo();
		ruleLevelSideCars.setRuleName("ruleName1");
		
		//Pre rule side car
		PraeceptaSideCarMetadata preRuleSideCar1=new PraeceptaSideCarMetadata();
		preRuleSideCar1.setOrder(1);
		preRuleSideCar1.setSideCarType("ENRICHER");
		preRuleSideCar1.setType("SIMPLE_REST");
		
		Map<String,Object> preRuleSideCarInfo=new HashMap<>();
		preRuleSideCar1.setSideCarConfigs(preRuleSideCarInfo);
		ruleLevelSideCars.setPreRuleSideCars(Arrays.asList(preRuleSideCar1));
		
		// Post rule side car
		PraeceptaSideCarMetadata postRuleSideCar1=new PraeceptaSideCarMetadata();
		postRuleSideCar1.setOrder(1);
		postRuleSideCar1.setSideCarType("PARSER");
		postRuleSideCar1.setType("XML");
		
		Map<String,Object> postRuleSideCarInfo=new HashMap<>();
		postRuleSideCar1.setSideCarConfigs(postRuleSideCarInfo);
		ruleLevelSideCars.setPostRuleSideCars(Arrays.asList(postRuleSideCar1));
		
		ruleGrpSidecras.setRuleLevelSideCars(Arrays.asList(ruleLevelSideCars));
		
		return ruleGrpSidecras;
	}
	private void addCriteria(PraeceptaRuleGroup praeceptaRuleGroup) {
		Collection<PraeceptaCriteria> praeceptaCriteriaCollection = new ArrayList<>();
        PraeceptaCriteria praeceptaCriteria = new PraeceptaCriteria();
        Collection<IPraeceptaAction> actions = new ArrayList<>();

        PraeceptaValueAssignAction praeceptaValueAssignAction = new PraeceptaValueAssignAction();
        praeceptaValueAssignAction.setValueToAssign("100");
        praeceptaValueAssignAction.setActionAttributeName("attribute1");
        praeceptaValueAssignAction.setActionStrategy(PraeceptaActionStrategyType.ADD_TO_PAYLOAD);
        actions.add(praeceptaValueAssignAction);

     //   praeceptaCriteria.setActionToPerform(actions);

        //praeceptaCriteria.setPredicates();
        praeceptaCriteria.setOrderNumber(1);
        praeceptaCriteria.setRuleName("Rule1");
        praeceptaCriteriaCollection.add(praeceptaCriteria);
        praeceptaRuleGroup.setPraeceptaCriterias(praeceptaCriteriaCollection);
	}
	
	private PraeceptaSideCarsInfo getSideCarsObject(PraeceptaRuleSpaceCompositeKey keyToUpdate) {
		PraeceptaSideCarsInfo ruleSideCars=new PraeceptaSideCarsInfo();
		ruleSideCars.setRuleSpaceKey(keyToUpdate);
		ruleSideCars.setActive(true);
		PraeceptaRuleGroupSideCarsInfo ruleGrpSideCar=new PraeceptaRuleGroupSideCarsInfo();
		ruleGrpSideCar.setRuleGrpName("testRuleGrp");
		List<PraeceptaSideCarMetadata> preRuleGrpSideCars =new ArrayList<>();
		PraeceptaSideCarMetadata sideCarMetaData=new PraeceptaSideCarMetadata();
		sideCarMetaData.setType("ENRICHER");
		sideCarMetaData.setSideCarType("SIMPLE_REST_API");
		preRuleGrpSideCars.add(sideCarMetaData);
		
		ruleGrpSideCar.setPreRuleGrpSideCars(preRuleGrpSideCars);
		ruleSideCars.setRuleGroupSideCars(ruleGrpSideCar);
		
		return ruleSideCars;
	}
}

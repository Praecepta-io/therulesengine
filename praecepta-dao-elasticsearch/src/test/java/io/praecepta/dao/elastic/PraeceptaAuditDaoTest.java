package io.praecepta.dao.elastic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.dao.elastic.config.spring.PraeceptaAuditDaoSpringConfig;
import io.praecepta.dao.elastic.enums.AUDIT_ELEMENT_TYPE;
import io.praecepta.dao.elastic.enums.AUDIT_OPERATION_TYPE;
import io.praecepta.dao.elastic.enums.AUDIT_POINT_TYPE;
import io.praecepta.dao.elastic.model.PraeceptaAuditElement;
import io.praecepta.dao.elastic.model.PraeceptaAuditElement.ValueHolder;
import io.praecepta.dao.elastic.model.PraeceptaAuditEntity;
import io.praecepta.dao.elastic.model.PraeceptaRuleAttributeAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleGroupAuditPoint;

/**
 * 
 * @author rajas
 * https://github.com/spring-projects/spring-data-elasticsearch/tree/main/src/test/java/org/springframework/data/elasticsearch/core/query
 *
 */
public class PraeceptaAuditDaoTest {
	
	ApplicationContext context = null;
	PraeceptaAuditDao dao = null;
	
	@Before
	public void setup() {
		context = new AnnotationConfigApplicationContext(PraeceptaAuditDaoSpringConfig.class);
		dao = (PraeceptaAuditDao) context.getBean("praeceptaAuditDao");
	}

//	@Test
	public void test() {
		
		System.setProperty("LOG_LEVEL", "INFO");
		
		PraeceptaAuditEntity entity = getEntityInfo();
		
		dao.insert(entity);
		
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(entity.getId());
		
		/*PraeceptaElasticConfiguration esConfig = new PraeceptaElasticConfiguration();
		
		esConfig.setHostName("https://search-praecepta-eastic-dev-w7ocogmmjf2lupt4u545du3ubi.us-east-1.es.amazonaws.com:443");
		esConfig.setUseSSL(true);
		esConfig.setUseSubject(true);
		esConfig.setConnectionSubjectName("PraeceptaES");
		esConfig.setConnectionSubjectSupport("Praecep1@E$");
		
		
		esConfig.elasticsearchClient();*/
	}
	
	@Test
	public void testInsertWithAdutiEntry() {
		
		System.setProperty("LOG_LEVEL", "INFO");
		
		PraeceptaAuditEntity entity = getEntityInfo();
		
		// Rule Group Changes
		PraeceptaRuleGroupAuditPoint rulGrpAuditPoint = new PraeceptaRuleGroupAuditPoint("Credit_Check");
		
		// Define Add/ Update and Delete Rules changes 
		Map<AUDIT_OPERATION_TYPE, List<PraeceptaRuleAuditPoint>> ruleOperationAuditPoints = new HashMap<>();
		
//---------------------------------------------------------------------------------------------------------------------------------------------------
		
	// Condition Changes start here	
		// New Rule Add Audit Info
		PraeceptaRuleAuditPoint scoreCheckRuleAuditPoint = new PraeceptaRuleAuditPoint("Score_Check");
		
		Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleAddAuditInfo = new HashMap<>();
		
		// Condition Attribute Change
		List<PraeceptaRuleAttributeAuditPoint> attributeNameToValueChange = new ArrayList<>();
		
		// New Condition Add - Element Changes like Value add or a new join operator add etc
		PraeceptaAuditElement scoreCheckEmployedValueChange = new PraeceptaAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE ,new ValueHolder(null, "yes"));
		PraeceptaAuditElement scoreCheckEmployedConditionOperatorChange = new PraeceptaAuditElement(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE ,new ValueHolder(null, "EQUALS"));
		
		// Defining Attribute for Which the above element changes belongs to
		PraeceptaRuleAttributeAuditPoint employedAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint("employed");
		// Adding all Element Changes to Attribute Audit Point
		employedAttributeAuditPoint.setAuditElements(Arrays.asList( new PraeceptaAuditElement[] {scoreCheckEmployedValueChange, scoreCheckEmployedConditionOperatorChange}));
		
		// Capture all Audit points for An Attribute
		attributeNameToValueChange.add(employedAttributeAuditPoint);
		
		PraeceptaAuditElement scoreCheckScoreValueChange = new PraeceptaAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE ,new ValueHolder(null, "800"));
		
		PraeceptaRuleAttributeAuditPoint scoreAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint("score");
		scoreAttributeAuditPoint.setAuditElements(Arrays.asList( new PraeceptaAuditElement[] { scoreCheckScoreValueChange }));
		
		// Capture all Audit points for An Attribute
		attributeNameToValueChange.add(scoreAttributeAuditPoint);
		
		// Adding Condition Changes to Audit info
		ruleAddAuditInfo.put(AUDIT_POINT_TYPE.CONDITION, attributeNameToValueChange);
	
	// Action Changes start here	
		// Action Change
		List<PraeceptaRuleAttributeAuditPoint>  actionAttributeNameToValueChange = new ArrayList<>();
		
		//  New Action Add - Action Element changes like Value add
		PraeceptaAuditElement scoreCheckEmployedActionChange = new PraeceptaAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE ,new ValueHolder(null, "Excellent"));
		
		// Defining Action Attribute for Which the above element changes belongs to
		PraeceptaRuleAttributeAuditPoint scoreLevelActionAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint("score_level");
		
		// Adding all Element Changes to Action Attribute Audit Point
		scoreLevelActionAttributeAuditPoint.setAuditElements(Arrays.asList( new PraeceptaAuditElement[] { scoreCheckEmployedActionChange }));
		
		// Capture all Audit points for An Action Attribute
		actionAttributeNameToValueChange.add(scoreLevelActionAttributeAuditPoint);
		
		// Adding Action Changes to Audit info
		ruleAddAuditInfo.put(AUDIT_POINT_TYPE.ACTION, actionAttributeNameToValueChange);
		
		// Adding both Condition and Action changes to a Rule 
		scoreCheckRuleAuditPoint.setRuleAuditInfo(ruleAddAuditInfo);
		
		ruleOperationAuditPoints.put(AUDIT_OPERATION_TYPE.ADD, Arrays.asList( new PraeceptaRuleAuditPoint[] {scoreCheckRuleAuditPoint} ) );
		
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------
		
	// Condition Changes start here	
		// Update Rule Add Audit Info
		PraeceptaRuleAuditPoint countryCheckRuleAuditPoint = new PraeceptaRuleAuditPoint("Country_Check");
		
		Map<AUDIT_POINT_TYPE, List<PraeceptaRuleAttributeAuditPoint>> ruleUpdateAuditInfo = new HashMap<>();
		
		// Condition Attribute Change
		List<PraeceptaRuleAttributeAuditPoint> updateRuleAttributeNameToValueChange = new ArrayList<>();
		
		// Update Condition Add - Element Changes like Value add or a new join operator add etc
		PraeceptaAuditElement regionElegibilityValueChange = new PraeceptaAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE , new ValueHolder("ASIA", "NOARTH_AMERICA"));
		PraeceptaAuditElement regionElegibilityOperatorChange = new PraeceptaAuditElement(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE , new ValueHolder("NOT_EQUALS", "EQUALS"));
		
		// Defining Attribute for Which the above element changes belongs to
		PraeceptaRuleAttributeAuditPoint updateRegionEligibleValueAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint("region_eligible");
		updateRegionEligibleValueAttributeAuditPoint.setAuditElements(Arrays.asList( new PraeceptaAuditElement[] {regionElegibilityValueChange, regionElegibilityOperatorChange} ));
		
		// Capture all Audit points for An Attribute
		updateRuleAttributeNameToValueChange.add(updateRegionEligibleValueAttributeAuditPoint);
		
		ruleUpdateAuditInfo.put(AUDIT_POINT_TYPE.CONDITION, updateRuleAttributeNameToValueChange);
		
	// Action Changes start here
		// Update Action Change
		List<PraeceptaRuleAttributeAuditPoint> updateRuleActionAttributeNameToValueChange = new ArrayList<>();
		
		// New Action Add - Action Element changes like Value add
		PraeceptaAuditElement regionElegibilityActionValueChange  = new PraeceptaAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHNAGE , new ValueHolder("SUCCESS", "SATISFIED"));
		
		// Defining Action Attribute for Which the above element changes belongs to
		PraeceptaRuleAttributeAuditPoint updateRegionEligibleActionAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint("region_eligibility");
		
		// Adding all Element Changes to Action Attribute Audit Point
		updateRegionEligibleActionAttributeAuditPoint.setAuditElements(Arrays.asList( new PraeceptaAuditElement[] {regionElegibilityActionValueChange}));
		
		// Capture all Audit points for An Action Attribute
		updateRuleActionAttributeNameToValueChange.add(updateRegionEligibleActionAttributeAuditPoint);
		
		// Adding Action Changes to Audit info
		ruleUpdateAuditInfo.put(AUDIT_POINT_TYPE.ACTION, actionAttributeNameToValueChange);
		
		// Adding both Condition and Action changes to a Rule 
		countryCheckRuleAuditPoint.setRuleAuditInfo(ruleUpdateAuditInfo);
		
		ruleOperationAuditPoints.put(AUDIT_OPERATION_TYPE.UPDATE, Arrays.asList( new PraeceptaRuleAuditPoint[] {countryCheckRuleAuditPoint} ) );
		
		//---------------------------------------------------------------------------------------------------------------------------------------------------
		
		// Adding the Rule Audits to Rule Group Audit Entry
		rulGrpAuditPoint.setRuleOperationAuditPoints(ruleOperationAuditPoints);
		
		entity.setRuleGroupAuditPoint(rulGrpAuditPoint);
		
		dao.insert(entity);
		
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(entity.getId());
		
	}
	
//	@Test
	public void testFetchById() {
		
		PraeceptaAuditEntity entity = getEntityInfo();
		entity.setId("k59ZrpABfzeprY7VQgTY");
		
		PraeceptaAuditEntity entityFromES = dao.fetchById(entity.getId());
		
		if(entityFromES != null) {
			System.out.println("Entity info "+entityFromES);
		}
		
	}
	
//	@Test
	public void testDeleteById() {
		
		PraeceptaAuditEntity entity = getEntityInfo();
		entity.setId("k59ZrpABfzeprY7VQgTY");
		
		dao.deleteById(entity.getId());
		
		System.out.println("Delete by Id is done");
		
		PraeceptaAuditEntity entity1 = getEntityInfo();
		entity1.setId("jZ9DlJABfzeprY7VNwTY");
		
		dao.delete(entity1);
		
		System.out.println("Delete by Entity is done");
		
	}
	
//	@Test
	public void testByCriteria() {
		
		Criteria esCriteria = new Criteria();
		
//		esCriteria = new Criteria("clientId").contains("aaa");
		esCriteria = new Criteria("spaceName").contains("XYZ");
		
		esCriteria.and(new Criteria("clientId").contains("Risk"));
		esCriteria.and(new Criteria("appName").contains("Creadit"));
		esCriteria.and(new Criteria("version").contains("V1"));
//		esCriteria.and("clientId").contains("aaa");
//		esCriteria.and("appName").contains("Credit");
//		esCriteria.and("version").contains("V6");
		
		System.out.println("ES Criteria "+esCriteria);
		
		CriteriaQuery criteriaQuery = new CriteriaQuery(
//				new Criteria("clientId").contains("Risk").and("appName").contains("Creadit"));
//		new Criteria("clientId").contains("aaa").and("appName").contains("Creadit")
				esCriteria
		);
		
		Page<PraeceptaAuditEntity> results = dao.searchByCriteria(criteriaQuery);
		
		System.out.println(results);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(results)) {
			
			System.out.println("Results Not null");
			
//			while(results.hasNext()) {
				List<PraeceptaAuditEntity> dataContent = results.getContent();
				
				System.out.println("Data Content Size - "+dataContent.size());
				
				dataContent.forEach( eachEntity -> System.out.println(eachEntity));
//			}
		}
	}
	
//	@Test
	public void testFetchByExample() {
		
		PraeceptaAuditEntity entity = new PraeceptaAuditEntity();//getEntityInfo();
		entity.setSpaceName("XYZ");
		entity.setClientId("Risk");
		entity.setAppName("Credit");
		entity.setVersion("V1");
//		entity.setId("jJ8-lJABfzeprY7VkwTC");
		
		Collection<PraeceptaAuditEntity> entitiesFromES = dao.findByExample(entity);
		
		System.out.println("Entities Size - "+entitiesFromES.size());
		
		entitiesFromES.forEach( eachEntity -> System.out.println("Id "+eachEntity.getId()));
	}

	private PraeceptaAuditEntity getEntityInfo() {
		PraeceptaAuditEntity entity = new PraeceptaAuditEntity();
		
		entity.setSpaceName("XYZ Bank");
		entity.setClientId("Risk Management");
		entity.setAppName("Creadit Risk");
		entity.setVersion("V1");
		return entity;
	}
}

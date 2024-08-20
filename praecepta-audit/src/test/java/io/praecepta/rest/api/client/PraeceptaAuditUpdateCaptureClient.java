package io.praecepta.rest.api.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.dao.elastic.enums.AUDIT_ELEMENT_TYPE;
import io.praecepta.dao.elastic.enums.AUDIT_OPERATION_TYPE;
import io.praecepta.dao.elastic.enums.AUDIT_POINT_TYPE;
import io.praecepta.dao.elastic.model.PraeceptaAuditElement;
import io.praecepta.dao.elastic.model.PraeceptaRuleAttributeAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleGroupAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleSpaceAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaAuditElement.ValueHolder;

public class PraeceptaAuditUpdateCaptureClient {

	public static void main(String[] args) throws Exception {

		CloseableHttpClient client = HttpClients.createDefault();

//		HttpPost httpPost = new HttpPost("http://localhost:8080/audit/addRuleGroupAudit/ABC/Risk/Creadit/V2/Credit");
//		HttpPut httpPost = new HttpPut("http://localhost:8080/audit/addRuleGroupAudit/ABC/Risk/Creadit/V2/CreditCheck1");
//		HttpPut httpPost = new HttpPut("http://localhost:4567/audit/ruleGroupAudit/ABC/Risk/Creadit/V2/CreditCheck1");
		HttpPost httpPost = new HttpPost("http://localhost:4567/audit/updateRuleGroupAudit/xxxxx");
//		HttpPost httpPost = new HttpPost("http://localhost:4567/audit/ruleGroupAudit/ABC/Risk/Creadit/V2/CreditCheck1");

		PraeceptaRuleGroupAuditPoint ruleGrpAuditPoint = getRuleGrp();
		
		PraeceptaRuleSpaceAuditPoint ruleGroupAuditPointToRefurbish = new PraeceptaRuleSpaceAuditPoint();
		
		ruleGroupAuditPointToRefurbish.setSpaceName("ABC");
		ruleGroupAuditPointToRefurbish.setClientId("Risk");
		ruleGroupAuditPointToRefurbish.setAppName("Creadit");
		ruleGroupAuditPointToRefurbish.setVersion("V1");
		ruleGroupAuditPointToRefurbish.setRuleGroupName("CreditCheck1");
		
		ruleGroupAuditPointToRefurbish.setRuleGroupAuditPoint(ruleGrpAuditPoint);
		
		String json = GsonHelper.toJson(ruleGrpAuditPoint);
		
		System.out.println("JSON to Insert --> "+json);
		StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
		httpPost.setEntity(entity);

		httpPost.setHeader("accpet", ContentType.APPLICATION_JSON.getMimeType());

		HttpResponse httpResponse = client.execute(httpPost);

		System.out.println(httpResponse.getStatusLine().getStatusCode());
		System.out.println(httpResponse.getEntity().getContent().toString());// toString());

		if (httpResponse != null && httpResponse.getEntity() != null) {
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}

			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		client.close();
	}
	
	public static PraeceptaRuleGroupAuditPoint getRuleGrp() {
		
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
				PraeceptaAuditElement scoreCheckEmployedValueChange = new PraeceptaAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHANGE ,new ValueHolder(null, "yes"));
				PraeceptaAuditElement scoreCheckEmployedConditionOperatorChange = new PraeceptaAuditElement(AUDIT_ELEMENT_TYPE.CONDITION_OPERATOR_CHANGE ,new ValueHolder(null, "EQUALS"));
				
				// Defining Attribute for Which the above element changes belongs to
				PraeceptaRuleAttributeAuditPoint employedAttributeAuditPoint = new PraeceptaRuleAttributeAuditPoint("employed");
				// Adding all Element Changes to Attribute Audit Point
				employedAttributeAuditPoint.setAuditElements(Arrays.asList( new PraeceptaAuditElement[] {scoreCheckEmployedValueChange, scoreCheckEmployedConditionOperatorChange}));
				
				// Capture all Audit points for An Attribute
				attributeNameToValueChange.add(employedAttributeAuditPoint);
				
				PraeceptaAuditElement scoreCheckScoreValueChange = new PraeceptaAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHANGE ,new ValueHolder(null, "800"));
				
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
				PraeceptaAuditElement scoreCheckEmployedActionChange = new PraeceptaAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHANGE ,new ValueHolder(null, "Excellent"));
				
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
				PraeceptaAuditElement regionElegibilityValueChange = new PraeceptaAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHANGE , new ValueHolder("ASIA", "NOARTH_AMERICA"));
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
				PraeceptaAuditElement regionElegibilityActionValueChange  = new PraeceptaAuditElement(AUDIT_ELEMENT_TYPE.VALUE_CHANGE , new ValueHolder("SUCCESS", "SATISFIED"));
				
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
				
				return rulGrpAuditPoint;
	}
}

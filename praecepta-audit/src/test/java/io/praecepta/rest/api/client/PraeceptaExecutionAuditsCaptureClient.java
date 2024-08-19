package io.praecepta.rest.api.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.dao.elastic.model.execution.PraeceptaExecutionAuditPoint;
import io.praecepta.dao.elastic.model.execution.PraeceptaExecutionAuditPoints;
import io.praecepta.dao.elastic.model.execution.PraeceptaRuleExecutionAuditPoint;
import io.praecepta.dao.elastic.model.execution.PraeceptaRuleGroupExecutionAuditPoint;
import io.praecepta.rules.dto.RuleGroupInfo;
import io.praecepta.rules.dto.RuleSpaceInfo;
import io.praecepta.rules.model.PraeceptaActionResult;
import io.praecepta.rules.model.PraeceptaActionResult.ACTION_EXECUTION_STATUS;
import io.praecepta.rules.model.PraeceptaActionResultDetails;
import io.praecepta.rules.model.PraeceptaConditionResult;
import io.praecepta.rules.model.PraeceptaConditionResult.CONDITION_RESULT;
import io.praecepta.rules.model.PraeceptaRuleResult;

public class PraeceptaExecutionAuditsCaptureClient {
	
private static final String SPACE_NAME = "ABC";
	
	private static final String CLIENT_D = "";
	
	private static final String APP_NAME = "Creadit Risk";
	
	private static final String VERSION = "V1";
	
	private static final String GRP_NAME = "CREDIT_CHECK";

	public static void main(String[] args) throws Exception {

		CloseableHttpClient client = HttpClients.createDefault();

//		HttpPost httpPost = new HttpPost("http://localhost:8080/audit/addRuleGroupAudit/ABC/Risk/Creadit/V2/Credit");
		HttpPut httpPost = new HttpPut(
				"http://localhost:4567/audit/addRuleGroupExecutionAudits/ABC/Risk/Creadit/V2/CreditCheck");

		PraeceptaExecutionAuditPoint executionTraceDto1 = getRuleExecutionAuditPoint();
		PraeceptaExecutionAuditPoint executionTraceDto2 = getRuleExecutionAuditPoint();
		
		PraeceptaExecutionAuditPoints executionTraceDtos = new PraeceptaExecutionAuditPoints();
		
		Collection<PraeceptaExecutionAuditPoint> ruleExecutionAuditPoints = new ArrayList<>();
		ruleExecutionAuditPoints.add(executionTraceDto1);
		ruleExecutionAuditPoints.add(executionTraceDto2);
		
		executionTraceDtos.setRuleExecutionAuditPoints(ruleExecutionAuditPoints);

		String json = GsonHelper.toJson(executionTraceDtos);

		System.out.println("JSON to Insert --> " + json);
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

	public static PraeceptaExecutionAuditPoint getRuleExecutionAuditPoint() {

		PraeceptaExecutionAuditPoint executionTraceDto = new PraeceptaExecutionAuditPoint();

		// Trace Id
		String traceId = UUID.randomUUID().toString();

//		Calendar calendar = Calendar.getInstance();

		LocalDateTime ldt = LocalDateTime.now();

		Date ruleGrpStartTime = addMinutesToDate(ldt, 0);// calendar.getTime();//new Date();

		// Start Pre Rule GROUP Execution Audit Point
		PraeceptaRuleGroupExecutionAuditPoint preRuleGroupExecutionAuditDetails = executionTraceDto
				.getPreRuleGroupExecutionAuditDetails();

		preRuleGroupExecutionAuditDetails.setRuleGroupInfo(getRuleGroupInfo());

		preRuleGroupExecutionAuditDetails.setTraceId(traceId);
		preRuleGroupExecutionAuditDetails.setStartTime(ruleGrpStartTime);
		// END Pre Rule GROUP Execution Audit Point

		// Span Id
		String spanId = UUID.randomUUID().toString();

		// Start Pre Rule Execution Audit Point
		PraeceptaRuleExecutionAuditPoint preRuleExecutionAuditDetails = executionTraceDto
				.getPreRuleExecutionAuditDetails();

		preRuleExecutionAuditDetails.setTraceId(traceId);
		preRuleExecutionAuditDetails.setSpanId(spanId);

//		calendar.add(Calendar.MINUTE, 1);

//		preRuleExecutionAuditDetails.setStartTime(calendar.getTime());
		Date ruleStart = addMinutesToDate(ldt, 1);

		preRuleExecutionAuditDetails.setStartTime(ruleStart);
		preRuleExecutionAuditDetails.setRuleGroupInfo(getRuleGroupInfo());

		PraeceptaRuleResult preRuleExecutionRuleResult = new PraeceptaRuleResult("Score_Check", null, null);

		preRuleExecutionAuditDetails.setRuleExecutionAuditPoint(preRuleExecutionRuleResult);

//		Score_Check

		// END Pre Rule Execution Audit Point

		// Start POST Rule Execution Audit Point
		PraeceptaRuleExecutionAuditPoint postRuleExecutionAuditDetails = executionTraceDto
				.getPostRuleExecutionAuditDetails();

		postRuleExecutionAuditDetails.setTraceId(traceId);
		postRuleExecutionAuditDetails.setSpanId(spanId);

		preRuleExecutionAuditDetails.setRuleGroupInfo(getRuleGroupInfo());

//		postRuleExecutionAuditDetails.setStartTime(calendar.getTime());
		postRuleExecutionAuditDetails.setStartTime(ruleStart);

//		calendar.add(Calendar.MINUTE, 3);

		Date ruleEnd = addMinutesToDate(ldt, 3);

//		postRuleExecutionAuditDetails.setEndTime(calendar.getTime());
		postRuleExecutionAuditDetails.setEndTime(ruleEnd);
		postRuleExecutionAuditDetails.setRuleGroupInfo(getRuleGroupInfo());

		PraeceptaConditionResult conditionResult = new PraeceptaConditionResult();

		conditionResult.setResult(CONDITION_RESULT.SATISFIED);
		conditionResult.setResultMessage("Score Check Rule is Satisfied");

		List<PraeceptaActionResultDetails> allActionResultDetails = new ArrayList<>();

		PraeceptaActionResult successActionResult = new PraeceptaActionResult();

		successActionResult.setActionResultStatus(ACTION_EXECUTION_STATUS.SUCCESS);
		successActionResult.setActionResultStatusMessage("Success Action Performed on Score Check Rule");

		PraeceptaActionResultDetails successActionResultDetails = new PraeceptaActionResultDetails(
				"Score Check Success Action", successActionResult, "scoreCheckResult", "Excellent");

		allActionResultDetails.add(successActionResultDetails);

		PraeceptaActionResult failActionResult = new PraeceptaActionResult();

		failActionResult.setActionResultStatus(ACTION_EXECUTION_STATUS.UNABALE_TO_PERFORM_ACTION);
		failActionResult.setActionResultStatusMessage("Failure Action Performed on Score Check Rule");

		PraeceptaActionResultDetails failActionResultDetails = new PraeceptaActionResultDetails(
				"Score Check Failure Action", failActionResult, "scoreCheckResult", null);

		allActionResultDetails.add(failActionResultDetails);

		PraeceptaRuleResult postRuleExecutionRuleResult = new PraeceptaRuleResult("Score_Check", conditionResult,
				allActionResultDetails);

		postRuleExecutionAuditDetails.setRuleExecutionAuditPoint(postRuleExecutionRuleResult);

		// END POST Rule Execution Audit Point

		// Start POST Rule GROUP Execution Audit Point
		PraeceptaRuleGroupExecutionAuditPoint postRuleGroupExecutionAuditDetails = executionTraceDto
				.getPostRuleGroupExecutionAuditDetails();

		postRuleGroupExecutionAuditDetails.setRuleGroupInfo(getRuleGroupInfo());

		postRuleGroupExecutionAuditDetails.setStartTime(ruleGrpStartTime);
		postRuleGroupExecutionAuditDetails.setTraceId(traceId);

//		calendar.add(Calendar.MINUTE, 5);

//		postRuleGroupExecutionAuditDetails.setEndTime(calendar.getTime());

		Date ruleGrpEnd = addMinutesToDate(ldt, 5);

		postRuleGroupExecutionAuditDetails.setEndTime(ruleGrpEnd);

		return executionTraceDto;
	}

	private static Date addMinutesToDate(LocalDateTime ldt, long minutes) {

		LocalDateTime ldtAfterAdd = ldt.plus(minutes, ChronoUnit.MINUTES);

		ZoneId zoneId = ZoneId.systemDefault();
		ZoneOffset zoneOffsetAfterAdd = zoneId.getRules().getOffset(ldtAfterAdd);

		Instant instantAfterAdd = ldtAfterAdd.toInstant(zoneOffsetAfterAdd);

		Date dateToReturn = Date.from(instantAfterAdd);

		System.out.println(dateToReturn);

		return dateToReturn;
	}

	private static RuleGroupInfo getRuleGroupInfo() {

		RuleGroupInfo ruleGroupInfo = new RuleGroupInfo();

//		ruleGroupInfo.setRuleGroupName(GRP_NAME);

		RuleSpaceInfo ruleSpaceInfo = getRuleSpaceInfo();

		ruleGroupInfo.setRuleSpaceInfo(ruleSpaceInfo);

		return ruleGroupInfo;
	}

	private static RuleSpaceInfo getRuleSpaceInfo() {

		RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();

//		ruleSpaceInfo.setSpaceName(SPACE_NAME);
//		ruleSpaceInfo.setClientId(CLIENT_D);
//		ruleSpaceInfo.setAppName(APP_NAME);
//		ruleSpaceInfo.setVersion(VERSION);

		return ruleSpaceInfo;
	}
}

package io.praecepta.dao.elastic;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.praecepta.dao.elastic.config.spring.PraeceptaAuditDaoSpringConfig;
import io.praecepta.dao.elastic.enums.execution.EXECUTION_AUDIT_OPERATION_TYPE;
import io.praecepta.dao.elastic.model.PraeceptaExecutionAuditPointEntity;
import io.praecepta.dao.elastic.model.PraeceptaRuleExecutionAuditPointEntity;
import io.praecepta.dao.elastic.model.PraeceptaRuleGroupExecutionAuditPointEntity;
import io.praecepta.dao.elastic.model.PraeceptaRulesExecutionEntity;
import io.praecepta.dao.elastic.model.execution.PraeceptaExecutionAuditPoint;
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

public class PraeceptaRulesExecutionAuditDaoTest {
	
	ApplicationContext context = null;
	
	PraeceptaRulesExecutionAuditDao dao = null;
	
	private static final String SPACE_NAME = "XYZ Bank";
	
	private static final String CLIENT_D = "Risk Management";
	
	private static final String APP_NAME = "Creadit Risk";
	
	private static final String VERSION = "V1";
	
	private static final String GRP_NAME = "CREDIT_CHECK";
	
	@Before
	public void setup() {
		context = new AnnotationConfigApplicationContext(PraeceptaAuditDaoSpringConfig.class);
		dao = (PraeceptaRulesExecutionAuditDao) context.getBean("praeceptaRulesExecutionAuditDao");
	}
	
	@Test
	public void test() {
		
		System.setProperty("LOG_LEVEL", "INFO");
		
		PraeceptaRulesExecutionEntity entity = getEntityInfo();
		
		PraeceptaExecutionAuditPoint executionTraceDto = new PraeceptaExecutionAuditPoint();
		
		// Trace Id
		String traceId = UUID.randomUUID().toString();
		
//		Calendar calendar = Calendar.getInstance();
		
		LocalDateTime ldt = LocalDateTime.now();
		
		Date ruleGrpStartTime = addMinutesToDate(ldt, 0);//calendar.getTime();//new Date();
		
		// Start Pre Rule GROUP Execution Audit Point
		PraeceptaRuleGroupExecutionAuditPoint preRuleGroupExecutionAuditDetails = executionTraceDto.getPreRuleGroupExecutionAuditDetails();
		
		preRuleGroupExecutionAuditDetails.setRuleGroupInfo(getRuleGroupInfo());
		
		preRuleGroupExecutionAuditDetails.setTraceId(traceId);
		preRuleGroupExecutionAuditDetails.setStartTime(ruleGrpStartTime);
		// END Pre Rule GROUP Execution Audit Point
		
		// Span Id
		String spanId = UUID.randomUUID().toString();
		
		// Start Pre Rule Execution Audit Point
		PraeceptaRuleExecutionAuditPoint preRuleExecutionAuditDetails = executionTraceDto.getPreRuleExecutionAuditDetails();
		
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
		PraeceptaRuleExecutionAuditPoint postRuleExecutionAuditDetails = executionTraceDto.getPostRuleExecutionAuditDetails();
		
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
		
		PraeceptaActionResultDetails successActionResultDetails = new PraeceptaActionResultDetails("Score Check Success Action", successActionResult, "scoreCheckResult", "Excellent");

		allActionResultDetails.add(successActionResultDetails);
		
		PraeceptaActionResult failActionResult = new PraeceptaActionResult();
		
		failActionResult.setActionResultStatus(ACTION_EXECUTION_STATUS.UNABALE_TO_PERFORM_ACTION);
		failActionResult.setActionResultStatusMessage("Failure Action Performed on Score Check Rule");
		
		PraeceptaActionResultDetails failActionResultDetails = new PraeceptaActionResultDetails("Score Check Failure Action", failActionResult, "scoreCheckResult", null);

		allActionResultDetails.add(failActionResultDetails);
		
		PraeceptaRuleResult postRuleExecutionRuleResult = new PraeceptaRuleResult("Score_Check", conditionResult, allActionResultDetails);
		
		postRuleExecutionAuditDetails.setRuleExecutionAuditPoint(postRuleExecutionRuleResult);
		
		// END POST Rule Execution Audit Point
		
		// Start POST Rule GROUP Execution Audit Point
		PraeceptaRuleGroupExecutionAuditPoint postRuleGroupExecutionAuditDetails = executionTraceDto.getPostRuleGroupExecutionAuditDetails();
		
		postRuleGroupExecutionAuditDetails.setRuleGroupInfo(getRuleGroupInfo());
		
		postRuleGroupExecutionAuditDetails.setStartTime(ruleGrpStartTime);
		postRuleGroupExecutionAuditDetails.setTraceId(traceId);
		
//		calendar.add(Calendar.MINUTE, 5);
		
//		postRuleGroupExecutionAuditDetails.setEndTime(calendar.getTime());
		
		Date ruleGrpEnd = addMinutesToDate(ldt, 5);
		
		postRuleGroupExecutionAuditDetails.setEndTime(ruleGrpEnd);
		// END POST Rule GROUP Execution Audit Point
		
		PraeceptaExecutionAuditPointEntity executionTraceEntity = convertDtoToEntity(executionTraceDto);
		
		System.out.println("Entity Info From Dto after Conversion "+executionTraceEntity);
		
		entity.setExecutionTrace(executionTraceEntity);
		
		dao.insert(entity);
//		
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(entity.getId());
		
	}
	
	private PraeceptaExecutionAuditPointEntity convertDtoToEntity(PraeceptaExecutionAuditPoint executionTraceDto) {
		
		PraeceptaExecutionAuditPointEntity executionTraceEntity =  new PraeceptaExecutionAuditPointEntity();
		
		// Capture Pre Rule Grp Audit Details
		PraeceptaRuleGroupExecutionAuditPoint preRuleGroupExecutionAuditDetailsDto = executionTraceDto.getPreRuleGroupExecutionAuditDetails();
		
		if(preRuleGroupExecutionAuditDetailsDto != null) {
			PraeceptaRuleGroupExecutionAuditPointEntity preRuleGroupExecutionAuditDetailsEntity = new PraeceptaRuleGroupExecutionAuditPointEntity(EXECUTION_AUDIT_OPERATION_TYPE.PRE_RULE_GROUP_HAWK_EYE);
		
			BeanUtils.copyProperties(preRuleGroupExecutionAuditDetailsDto, preRuleGroupExecutionAuditDetailsEntity);
			
			Date ruleGrpStartTime = preRuleGroupExecutionAuditDetailsEntity.getStartTime();
			
			if(ruleGrpStartTime != null) {
				String ruleGrpStartTimeText = formatToEsDate(ruleGrpStartTime);
				
				preRuleGroupExecutionAuditDetailsEntity.setRuleGrpStartTime(ruleGrpStartTimeText);
			}
			
			System.out.println(preRuleGroupExecutionAuditDetailsEntity);
			
			executionTraceEntity.setPreRuleGroupExecutionAuditDetails(preRuleGroupExecutionAuditDetailsEntity);
		}
		
		// Capture Pre Rule Audit Details
		PraeceptaRuleExecutionAuditPoint preRuleExecutionAuditDetailsDto = executionTraceDto.getPreRuleExecutionAuditDetails();
		
		if(preRuleExecutionAuditDetailsDto != null) {
			PraeceptaRuleExecutionAuditPointEntity preRuleExecutionAuditDetailsEntity = new PraeceptaRuleExecutionAuditPointEntity(EXECUTION_AUDIT_OPERATION_TYPE.PRE_RULE_HAWK_EYE);
		
			BeanUtils.copyProperties(preRuleExecutionAuditDetailsDto, preRuleExecutionAuditDetailsEntity);

			Date ruleStartTime = preRuleExecutionAuditDetailsEntity.getStartTime();
			
			if(ruleStartTime != null) {
				String ruleStartTimeText = formatToEsDate(ruleStartTime);
				
				preRuleExecutionAuditDetailsEntity.setRuleStartTime(ruleStartTimeText);
			}
			
			System.out.println(preRuleExecutionAuditDetailsEntity);
			
			executionTraceEntity.setPreRuleExecutionAuditDetails(preRuleExecutionAuditDetailsEntity);
		}
		
		// Capture Post Rule Audit Details
		PraeceptaRuleExecutionAuditPoint postRuleExecutionAuditDetailsDto = executionTraceDto.getPostRuleExecutionAuditDetails();
		
		if(postRuleExecutionAuditDetailsDto != null) {
			PraeceptaRuleExecutionAuditPointEntity postRuleExecutionAuditDetailsEntity = new PraeceptaRuleExecutionAuditPointEntity(EXECUTION_AUDIT_OPERATION_TYPE.POST_RULE_HAWK_EYE);
		
			BeanUtils.copyProperties(postRuleExecutionAuditDetailsDto, postRuleExecutionAuditDetailsEntity);

			Date ruleStartTime = postRuleExecutionAuditDetailsEntity.getStartTime();
			
			if(ruleStartTime != null) {
				String ruleStartTimeText = formatToEsDate(ruleStartTime);
				
				postRuleExecutionAuditDetailsEntity.setRuleStartTime(ruleStartTimeText);
			}

			Date ruleEndTime = postRuleExecutionAuditDetailsEntity.getEndTime();
			
			if(ruleEndTime != null) {
				String ruleEndTimeText = formatToEsDate(ruleEndTime);
				
				postRuleExecutionAuditDetailsEntity.setRuleEndTime(ruleEndTimeText);
			}
			
			System.out.println(postRuleExecutionAuditDetailsEntity);
			
			executionTraceEntity.setPostRuleExecutionAuditDetails(postRuleExecutionAuditDetailsEntity);
		}

		// Capture Post Rule Grp Audit Details
		PraeceptaRuleGroupExecutionAuditPoint postRuleGroupExecutionAuditDetailsDto = executionTraceDto.getPostRuleGroupExecutionAuditDetails();
		
		if(preRuleGroupExecutionAuditDetailsDto != null) {
			PraeceptaRuleGroupExecutionAuditPointEntity postRuleGroupExecutionAuditDetailsEntity = new PraeceptaRuleGroupExecutionAuditPointEntity(EXECUTION_AUDIT_OPERATION_TYPE.POST_RULE_GROUP_HAWK_EYE);
		
			BeanUtils.copyProperties(postRuleGroupExecutionAuditDetailsDto, postRuleGroupExecutionAuditDetailsEntity);

			Date ruleGrpStartTime = postRuleGroupExecutionAuditDetailsEntity.getStartTime();
			
			if(ruleGrpStartTime != null) {
				String ruleGrpStartTimeText = formatToEsDate(ruleGrpStartTime);
				
				postRuleGroupExecutionAuditDetailsEntity.setRuleGrpStartTime(ruleGrpStartTimeText);
			}
			
			Date ruleGrpEndTime = postRuleGroupExecutionAuditDetailsEntity.getEndTime();
			
			if(ruleGrpEndTime != null) {
				String ruleGrpEndTimeText = formatToEsDate(ruleGrpEndTime);
				
				postRuleGroupExecutionAuditDetailsEntity.setRuleGrpEndTime(ruleGrpEndTimeText);
			}
			
			System.out.println(postRuleGroupExecutionAuditDetailsEntity);
			
			executionTraceEntity.setPostRuleGroupExecutionAuditDetails(postRuleGroupExecutionAuditDetailsEntity);
		}
		
//		executionTraceEntity
		
//		BeanUtils.copyProperties(executionTraceDto, executionTraceEntity);

		return executionTraceEntity;
	}

	private PraeceptaRulesExecutionEntity getEntityInfo() {
		PraeceptaRulesExecutionEntity entity = new PraeceptaRulesExecutionEntity();
		
		entity.setSpaceName(SPACE_NAME);
		entity.setClientId(CLIENT_D);
		entity.setAppName(APP_NAME);
		entity.setVersion(VERSION);
		return entity;
	}
	
	private RuleGroupInfo getRuleGroupInfo() {
		
		RuleGroupInfo ruleGroupInfo = new RuleGroupInfo();
		
		ruleGroupInfo.setRuleGroupName(GRP_NAME);
		
		RuleSpaceInfo ruleSpaceInfo = getRuleSpaceInfo();
		
		ruleGroupInfo.setRuleSpaceInfo(ruleSpaceInfo);
		
		return ruleGroupInfo;
	}
	
	private RuleSpaceInfo getRuleSpaceInfo() {
		
		RuleSpaceInfo ruleSpaceInfo = new RuleSpaceInfo();
		
		ruleSpaceInfo.setSpaceName(SPACE_NAME);
		ruleSpaceInfo.setClientId(CLIENT_D);
		ruleSpaceInfo.setAppName(APP_NAME);
		ruleSpaceInfo.setVersion(VERSION);
		
        return ruleSpaceInfo;
    }
	
	private static Date addMinutesToDate(LocalDateTime ldt, long minutes) {
		
		LocalDateTime ldtAfterAdd  = ldt.plus(minutes, ChronoUnit.MINUTES);
		
		ZoneId zoneId =  ZoneId.systemDefault();
		ZoneOffset zoneOffsetAfterAdd = zoneId.getRules().getOffset(ldtAfterAdd);
		
		Instant instantAfterAdd = ldtAfterAdd.toInstant(zoneOffsetAfterAdd);
		
		Date dateToReturn = Date.from(instantAfterAdd);
		
		System.out.println(dateToReturn);
		
		formatToEsDate(dateToReturn);
		
		return dateToReturn;
	}

	private static String formatToEsDate(Date dateToFormat) {
		
		SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		
		String formattedDate = output.format(dateToFormat);

		System.out.println(formattedDate);
		
		return formattedDate;
	}
	
	

}

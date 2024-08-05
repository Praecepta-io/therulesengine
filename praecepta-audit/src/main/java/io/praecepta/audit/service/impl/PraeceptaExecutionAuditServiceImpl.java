package io.praecepta.audit.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import io.praecepta.audit.service.IPraeceptaExecutionAuditService;
import io.praecepta.audit.service.helper.PraeceptaRuleSpaceHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.dao.elastic.PraeceptaRulesExecutionAuditDao;
import io.praecepta.dao.elastic.enums.execution.EXECUTION_AUDIT_OPERATION_TYPE;
import io.praecepta.dao.elastic.model.PraeceptaExecutionAuditPointEntity;
import io.praecepta.dao.elastic.model.PraeceptaRuleExecutionAuditPointEntity;
import io.praecepta.dao.elastic.model.PraeceptaRuleGroupExecutionAuditPointEntity;
import io.praecepta.dao.elastic.model.PraeceptaRulesExecutionEntity;
import io.praecepta.dao.elastic.model.execution.PraeceptaExecutionAuditPoint;
import io.praecepta.dao.elastic.model.execution.PraeceptaExecutionAuditPoints;
import io.praecepta.dao.elastic.model.execution.PraeceptaRuleExecutionAuditPoint;
import io.praecepta.dao.elastic.model.execution.PraeceptaRuleGroupExecutionAuditPoint;

public class PraeceptaExecutionAuditServiceImpl implements IPraeceptaExecutionAuditService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaExecutionAuditServiceImpl.class);

	@Autowired
	private PraeceptaRulesExecutionAuditDao rulesExecutionAuditDao;

	@Override
	public void captureRuleGroupExecutionAudit(String spaceName, String clientId, String appName, String version,
			String groupname, PraeceptaExecutionAuditPoint executionTraceDto) {
		
		LOG.debug("Inside captureRuleGroupExecutionAudit");
		
		if(PraeceptaRuleSpaceHelper.validateNonEmptySpaceKeyInfo(spaceName, clientId, appName, version, groupname) &&  
				!PraeceptaObjectHelper.isObjectEmpty(executionTraceDto)) {
			
			PraeceptaRulesExecutionEntity entity = buildPraeceptaRulesExecutionEntity(spaceName, clientId, appName, version, groupname,
					executionTraceDto);
			
			LOG.info("Inserting Execution Entity - {}", entity);
			
			rulesExecutionAuditDao.insert(entity);
		}
		
		LOG.debug("Exiting captureRuleGroupExecutionAudit");

	}

	private PraeceptaRulesExecutionEntity buildPraeceptaRulesExecutionEntity(String spaceName, String clientId, String appName, String version,
			String groupname, PraeceptaExecutionAuditPoint executionTraceDto) {
		
			PraeceptaRulesExecutionEntity entity = new PraeceptaRulesExecutionEntity();
	
			PraeceptaExecutionAuditPointEntity executionTraceEntity = convertDtoToEntity(executionTraceDto);
	
			entity.setSpaceName(spaceName);
			entity.setClientId(clientId);
			entity.setAppName(appName);
			entity.setVersion(version);
			entity.setRuleGroupName(groupname);
	
			entity.setExecutionTrace(executionTraceEntity);
			
		return entity;
	}
	
	@Override
	public void captureRuleGroupExecutionAudits(String spaceName, String clientId, String appName, String version,
			String groupname, Collection<PraeceptaExecutionAuditPoint> executionTraceDtos) {
		
		LOG.debug("Inside captureRuleGroupExecutionAudits");
		
		if(PraeceptaRuleSpaceHelper.validateNonEmptySpaceKeyInfo(spaceName, clientId, appName, version, groupname) &&  
				!PraeceptaObjectHelper.isObjectEmpty(executionTraceDtos)) {
			
			Collection<PraeceptaRulesExecutionEntity> entities = new ArrayList<>();
			
			executionTraceDtos.stream().forEach( executionTraceDto -> {
				entities.add( buildPraeceptaRulesExecutionEntity(spaceName, clientId, appName, version, groupname,
					executionTraceDto));
			});
			
			LOG.info("Inserting Execution Entities with Size - {} ", entities.size());
			
			rulesExecutionAuditDao.insertAll(entities);
		}
		
		LOG.debug("Exiting captureRuleGroupExecutionAudits");
	}



	@Override
	public PraeceptaExecutionAuditPoints fetchRuleGroupExecutionAudit(String spaceName, String clientId,
			String appName, String version, String groupname) {
		
		LOG.debug("Inside fetchRuleGroupExecutionAudit");
		
		if (PraeceptaRuleSpaceHelper.validateNonEmptySpaceKeyInfo(spaceName, clientId, appName, version, groupname)) {
			PraeceptaRulesExecutionEntity exampleEntity = new PraeceptaRulesExecutionEntity();

			exampleEntity.setSpaceName(spaceName);
			exampleEntity.setClientId(clientId);
			exampleEntity.setAppName(appName);
			exampleEntity.setVersion(version);
			exampleEntity.setRuleGroupName(groupname);
			
			PraeceptaExecutionAuditPoints praeceptaExecutionAuditPoints = new PraeceptaExecutionAuditPoints();
			
			Collection<PraeceptaRulesExecutionEntity> ruleExecutionAuditPointEntities = rulesExecutionAuditDao.findByExample(exampleEntity);
			
			if (!PraeceptaObjectHelper.isObjectEmpty(ruleExecutionAuditPointEntities)) {
				
				Collection<PraeceptaExecutionAuditPoint> executionTraceDtos = new ArrayList<>();
				
				ruleExecutionAuditPointEntities.stream().forEach( (eachEntity) -> {
					executionTraceDtos.add(convertEntityToDto(eachEntity));
				});
			}
			

			return praeceptaExecutionAuditPoints;
		}
		
		LOG.debug("Exiting fetchRuleGroupExecutionAudit");
		return null;
	}

	private static PraeceptaExecutionAuditPoint convertEntityToDto(PraeceptaRulesExecutionEntity eachEntity) {
		
		PraeceptaExecutionAuditPoint dto = new PraeceptaExecutionAuditPoint();
		
		BeanUtils.copyProperties(eachEntity, dto);
		
		return dto;
	}

	private static PraeceptaExecutionAuditPointEntity convertDtoToEntity(PraeceptaExecutionAuditPoint executionTraceDto) {
		
		LOG.debug("Inside Convert PraeceptaExecutionAuditPoint Dto to Entity ");
		
		if (!PraeceptaObjectHelper.isObjectEmpty(executionTraceDto)) {
			
			LOG.info("Triggering the Conversion for Dto - ()", executionTraceDto);

			PraeceptaExecutionAuditPointEntity executionTraceEntity = new PraeceptaExecutionAuditPointEntity();

			// Capture Pre Rule Grp Audit Details
			PraeceptaRuleGroupExecutionAuditPoint preRuleGroupExecutionAuditDetailsDto = executionTraceDto
					.getPreRuleGroupExecutionAuditDetails();

			if (preRuleGroupExecutionAuditDetailsDto != null) {
				
				LOG.debug("Pre Rule Group Dto Details - {}", preRuleGroupExecutionAuditDetailsDto);
				
				PraeceptaRuleGroupExecutionAuditPointEntity preRuleGroupExecutionAuditDetailsEntity = new PraeceptaRuleGroupExecutionAuditPointEntity(
						EXECUTION_AUDIT_OPERATION_TYPE.PRE_RULE_GROUP_HAWK_EYE);

				BeanUtils.copyProperties(preRuleGroupExecutionAuditDetailsDto, preRuleGroupExecutionAuditDetailsEntity);

				Date ruleGrpStartTime = preRuleGroupExecutionAuditDetailsEntity.getStartTime();

				if (ruleGrpStartTime != null) {
					String ruleGrpStartTimeText = formatToEsDate(ruleGrpStartTime);

					preRuleGroupExecutionAuditDetailsEntity.setRuleGrpStartTime(ruleGrpStartTimeText);
				}

				LOG.info("Pre Rule Group Entity Details - {}", preRuleGroupExecutionAuditDetailsEntity);

				executionTraceEntity.setPreRuleGroupExecutionAuditDetails(preRuleGroupExecutionAuditDetailsEntity);
			}

			// Capture Pre Rule Audit Details
			PraeceptaRuleExecutionAuditPoint preRuleExecutionAuditDetailsDto = executionTraceDto
					.getPreRuleExecutionAuditDetails();

			if (preRuleExecutionAuditDetailsDto != null) {
				
				LOG.debug("Pre Rule Dto Details - {}", preRuleExecutionAuditDetailsDto);
				
				PraeceptaRuleExecutionAuditPointEntity preRuleExecutionAuditDetailsEntity = new PraeceptaRuleExecutionAuditPointEntity(
						EXECUTION_AUDIT_OPERATION_TYPE.PRE_RULE_HAWK_EYE);

				BeanUtils.copyProperties(preRuleExecutionAuditDetailsDto, preRuleExecutionAuditDetailsEntity);

				Date ruleStartTime = preRuleExecutionAuditDetailsEntity.getStartTime();

				if (ruleStartTime != null) {
					String ruleStartTimeText = formatToEsDate(ruleStartTime);

					preRuleExecutionAuditDetailsEntity.setRuleStartTime(ruleStartTimeText);
				}

				LOG.info("Pre Rule Entity Details - {}", preRuleExecutionAuditDetailsEntity);

				executionTraceEntity.setPreRuleExecutionAuditDetails(preRuleExecutionAuditDetailsEntity);
			}

			// Capture Post Rule Audit Details
			PraeceptaRuleExecutionAuditPoint postRuleExecutionAuditDetailsDto = executionTraceDto
					.getPostRuleExecutionAuditDetails();

			if (postRuleExecutionAuditDetailsDto != null) {
				
				LOG.debug("Post Rule Dto Details - {}", postRuleExecutionAuditDetailsDto);
				
				PraeceptaRuleExecutionAuditPointEntity postRuleExecutionAuditDetailsEntity = new PraeceptaRuleExecutionAuditPointEntity(
						EXECUTION_AUDIT_OPERATION_TYPE.POST_RULE_HAWK_EYE);

				BeanUtils.copyProperties(postRuleExecutionAuditDetailsDto, postRuleExecutionAuditDetailsEntity);

				Date ruleStartTime = postRuleExecutionAuditDetailsEntity.getStartTime();

				if (ruleStartTime != null) {
					String ruleStartTimeText = formatToEsDate(ruleStartTime);

					postRuleExecutionAuditDetailsEntity.setRuleStartTime(ruleStartTimeText);
				}

				Date ruleEndTime = postRuleExecutionAuditDetailsEntity.getEndTime();

				if (ruleEndTime != null) {
					String ruleEndTimeText = formatToEsDate(ruleEndTime);

					postRuleExecutionAuditDetailsEntity.setRuleEndTime(ruleEndTimeText);
				}

				LOG.info("Post Rule Entity Details - {}", postRuleExecutionAuditDetailsEntity);

				executionTraceEntity.setPostRuleExecutionAuditDetails(postRuleExecutionAuditDetailsEntity);
			}

			// Capture Post Rule Grp Audit Details
			PraeceptaRuleGroupExecutionAuditPoint postRuleGroupExecutionAuditDetailsDto = executionTraceDto
					.getPostRuleGroupExecutionAuditDetails();

			if (preRuleGroupExecutionAuditDetailsDto != null) {
				
				LOG.debug("Post Rule Group Dto Details - {}", preRuleGroupExecutionAuditDetailsDto);
				
				PraeceptaRuleGroupExecutionAuditPointEntity postRuleGroupExecutionAuditDetailsEntity = new PraeceptaRuleGroupExecutionAuditPointEntity(
						EXECUTION_AUDIT_OPERATION_TYPE.POST_RULE_GROUP_HAWK_EYE);

				BeanUtils.copyProperties(postRuleGroupExecutionAuditDetailsDto,
						postRuleGroupExecutionAuditDetailsEntity);

				Date ruleGrpStartTime = postRuleGroupExecutionAuditDetailsEntity.getStartTime();

				if (ruleGrpStartTime != null) {
					String ruleGrpStartTimeText = formatToEsDate(ruleGrpStartTime);

					postRuleGroupExecutionAuditDetailsEntity.setRuleGrpStartTime(ruleGrpStartTimeText);
				}

				Date ruleGrpEndTime = postRuleGroupExecutionAuditDetailsEntity.getEndTime();

				if (ruleGrpEndTime != null) {
					String ruleGrpEndTimeText = formatToEsDate(ruleGrpEndTime);

					postRuleGroupExecutionAuditDetailsEntity.setRuleGrpEndTime(ruleGrpEndTimeText);
				}

				LOG.info("Post Rule Group Entity Details - {}",postRuleGroupExecutionAuditDetailsEntity);

				executionTraceEntity.setPostRuleGroupExecutionAuditDetails(postRuleGroupExecutionAuditDetailsEntity);
			}

			return executionTraceEntity;
		}
		LOG.debug("Exiting Convert PraeceptaExecutionAuditPoint Dto to Entity ");
		
		return null;
	}

	private static String formatToEsDate(Date dateToFormat) {

		SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

		String formattedDate = output.format(dateToFormat);

		System.out.println(formattedDate);

		return formattedDate;
	}

}

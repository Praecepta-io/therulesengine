package io.praecepta.audit.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import io.praecepta.audit.service.IPraeceptaAuditService;
import io.praecepta.audit.service.helper.PraeceptaRuleSpaceHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.dao.elastic.PraeceptaAuditDao;
import io.praecepta.dao.elastic.model.PraeceptaAuditEntity;
import io.praecepta.dao.elastic.model.PraeceptaRuleGroupAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleSpaceAuditPoint;

public class PraeceptaAuditServiceImpl implements IPraeceptaAuditService{
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaAuditServiceImpl.class);
	
	@Autowired
	private PraeceptaAuditDao praeceptaAuditDao;

	@Override
	public PraeceptaRuleSpaceAuditPoint captureRuleGroupAudit(String spaceName, String clientId, String appName, String version,
			String groupname, PraeceptaRuleGroupAuditPoint ruleGroupAuditPoint) {
		
		LOG.debug("Inside captureRuleGroupAudit");
		
		if (PraeceptaRuleSpaceHelper.validateNonEmptySpaceKeyInfo(spaceName, clientId, appName, version, groupname) &&  !PraeceptaObjectHelper.isObjectEmpty(ruleGroupAuditPoint)) {

			PraeceptaRuleSpaceAuditPoint praeceptaRuleSpaceAuditPoint = new PraeceptaRuleSpaceAuditPoint();

			PraeceptaAuditEntity ruleAuditEntity = getAuditEntityForAGroup(spaceName, clientId, appName, version,
					groupname);
			
			LOG.info("About to insert Audit Entity with details {}", ruleAuditEntity);

			ruleAuditEntity.setRuleGroupAuditPoint(ruleGroupAuditPoint);

			praeceptaAuditDao.insert(ruleAuditEntity);

			if (ruleAuditEntity != null && ruleAuditEntity.getId() != null) {

				praeceptaRuleSpaceAuditPoint = buildRuleSpaceAuditPoint(ruleAuditEntity);
			}

			LOG.debug("Exiting  captureRuleGroupAudit");
			return praeceptaRuleSpaceAuditPoint;
		}
		
		return null;
	}
	
	@Override
	public List<PraeceptaRuleSpaceAuditPoint> fetchRuleGroupAudit(String spaceName, String clientId, String appName,
			String version, String groupname) {
		
		LOG.debug("Inside fetchRuleGroupAudit");
		
		List<PraeceptaRuleSpaceAuditPoint> ruleSpaceAuditPoints = new ArrayList<>();

		if (PraeceptaRuleSpaceHelper.validateNonEmptySpaceKeyInfo(spaceName, clientId, appName, version, groupname)){
			
			PraeceptaAuditEntity ruleAuditEntityExample = getAuditEntityForAGroup(spaceName, clientId, appName, version, groupname);
			
			LOG.info("Performing the fetchRuleGroupAudit");
			
			Collection<PraeceptaAuditEntity> ruleAuditEntities = praeceptaAuditDao.findByExample(ruleAuditEntityExample);
			
			if(!PraeceptaObjectHelper.isObjectEmpty(ruleAuditEntities)) {
				
				ruleAuditEntities.forEach( (eachEntityFromES) -> {
					ruleSpaceAuditPoints.add(buildRuleSpaceAuditPoint(eachEntityFromES));
				});
			}
		}
		LOG.debug("Exiting fetchRuleGroupAudit");
		
		return ruleSpaceAuditPoints;
	}
	
	@Override
	public void refurbishRuleGroupAudit(String uniqueId, PraeceptaRuleSpaceAuditPoint ruleGroupAuditPointToRefurbish) {
		LOG.debug("Inside fetchRuleGroupAudit");
		
		if(!PraeceptaObjectHelper.isObjectEmpty(uniqueId) && !PraeceptaObjectHelper.isObjectEmpty(ruleGroupAuditPointToRefurbish)) {
			
			LOG.info("Performing the Refurbish for Unique Id - {} and Audit point - {} ", uniqueId, ruleGroupAuditPointToRefurbish);
			
			PraeceptaAuditEntity ruleAuditEntityToRefurbish = new PraeceptaAuditEntity();
					
			BeanUtils.copyProperties(ruleGroupAuditPointToRefurbish, ruleAuditEntityToRefurbish);
			
			ruleAuditEntityToRefurbish.setId(uniqueId);
			
			praeceptaAuditDao.update(ruleAuditEntityToRefurbish);
		}
		
		LOG.debug("Exiting fetchRuleGroupAudit");
	}

	private PraeceptaAuditEntity getAuditEntityForAGroup(String spaceName, String clientId, String appName, String version,
			String groupname) {
		
		PraeceptaAuditEntity ruleAuditEntity = new PraeceptaAuditEntity();
		
		ruleAuditEntity.setSpaceName(spaceName);
		ruleAuditEntity.setClientId(clientId);
		ruleAuditEntity.setAppName(appName);
		ruleAuditEntity.setVersion(version);
		ruleAuditEntity.setRuleGroupName(groupname);
		
		return ruleAuditEntity;
	}

	private PraeceptaRuleSpaceAuditPoint buildRuleSpaceAuditPoint(PraeceptaAuditEntity ruleAuditEntity) {
		
		PraeceptaRuleSpaceAuditPoint praeceptaRuleSpaceAuditPoint = new PraeceptaRuleSpaceAuditPoint();
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleAuditEntity)) {
			BeanUtils.copyProperties(ruleAuditEntity, praeceptaRuleSpaceAuditPoint);
		}
		
		return praeceptaRuleSpaceAuditPoint;
	}

}

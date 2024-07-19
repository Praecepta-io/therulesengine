package io.praecepta.audit.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import io.praecepta.audit.service.IPraeceptaAuditService;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.dao.elastic.PraeceptaAuditDao;
import io.praecepta.dao.elastic.model.PraeceptaAuditEntity;
import io.praecepta.dao.elastic.model.PraeceptaRuleGroupAuditPoint;
import io.praecepta.dao.elastic.model.PraeceptaRuleSpaceAuditPoint;

public class PraeceptaAuditServiceImpl implements IPraeceptaAuditService{
	
	@Autowired
	private PraeceptaAuditDao praeceptaAuditDao;

	@Override
	public PraeceptaRuleSpaceAuditPoint captureRuleGroupAudit(String spaceName, String clientId, String appName, String version,
			String groupname, PraeceptaRuleGroupAuditPoint ruleGroupAuditPoint) {
		
		PraeceptaRuleSpaceAuditPoint praeceptaRuleSpaceAuditPoint = new PraeceptaRuleSpaceAuditPoint();
		
		PraeceptaAuditEntity ruleAuditEntity = getAuditEntityForAGroup(spaceName, clientId, appName, version, groupname);
		
		ruleAuditEntity.setRuleGroupAuditPoint(ruleGroupAuditPoint);
		
		praeceptaAuditDao.insert(ruleAuditEntity);
		
		if(ruleAuditEntity != null && ruleAuditEntity.getId() != null) {
			
			praeceptaRuleSpaceAuditPoint = buildRuleSpaceAuditPoint(ruleAuditEntity);
		}
		
		return praeceptaRuleSpaceAuditPoint;
	}
	
	@Override
	public List<PraeceptaRuleSpaceAuditPoint> fetchRuleGroupAudit(String spaceName, String clientId, String appName,
			String version, String groupname) {
		
		List<PraeceptaRuleSpaceAuditPoint>  ruleSpaceAuditPoints = new ArrayList<>();
		
		PraeceptaAuditEntity ruleAuditEntityExample = getAuditEntityForAGroup(spaceName, clientId, appName, version, groupname);
		
		Collection<PraeceptaAuditEntity> ruleAuditEntities = praeceptaAuditDao.findByExample(ruleAuditEntityExample);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(ruleAuditEntities)) {
			
			ruleAuditEntities.forEach( (eachEntityFromES) -> {
				ruleSpaceAuditPoints.add(buildRuleSpaceAuditPoint(eachEntityFromES));
			});
		}
		
		return ruleSpaceAuditPoints;
	}
	
	@Override
	public void refurbishRuleGroupAudit(String uniqueId, PraeceptaRuleSpaceAuditPoint ruleGroupAuditPointToRefurbish) {
		
		PraeceptaAuditEntity ruleAuditEntityToRefurbish = new PraeceptaAuditEntity();
				
		BeanUtils.copyProperties(ruleGroupAuditPointToRefurbish, ruleAuditEntityToRefurbish);
		
		ruleAuditEntityToRefurbish.setId(uniqueId);
		
		praeceptaAuditDao.update(ruleAuditEntityToRefurbish);
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

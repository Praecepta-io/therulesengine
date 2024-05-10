package io.praecepta.rules.hub.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collection;

import org.junit.Test;

import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_SUB_TYPE;
import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_TYPE;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;

public class PraeceptaRuleDaoRegisterTest {

	@Test
	public void testRuleSpaceDao() {
		
		IPraeceptaRuleSpaceDao ruleSapceDao = PraeceptaRuleDaoRegister.
				getRuleSpaceDaoForTypeAndSubType(RULE_SET_UP_PERSISTANT_TYPE.SQL_DB.name(), RULE_SET_UP_PERSISTANT_SUB_TYPE.MySQL.name());
		
		
		assertNotNull(ruleSapceDao);
	}
	
	@Test
	public void testRuleGroupDao() {
		
		IPraeceptaRuleGroupDao ruleGroupDao = PraeceptaRuleDaoRegister.
				getRuleGroupDaoForTypeAndSubType(RULE_SET_UP_PERSISTANT_TYPE.NO_SQL_DB.name(), RULE_SET_UP_PERSISTANT_SUB_TYPE.MongoDb.name());
		
		
		assertNotNull(ruleGroupDao);
	}
	
	@Test
	public void testRegisterDaoMapper() {
		
		String type = "TestType";
		String subType= "TestSubType";
		
		PraeceptaRuleDaoRegister.registerRuleDaoProviderForTypeAndSubType(type, subType, new IPraeceptaRuleSpaceDao() {
			
			@Override
			public void updateAll(Collection<PraeceptaRuleSpace> entities) {
			}
			
			@Override
			public void update(PraeceptaRuleSpace entity) {
			}
			
			@Override
			public void insertAll(Collection<PraeceptaRuleSpace> entities) {
			}
			
			@Override
			public void insert(PraeceptaRuleSpace entity) {
			}
			
			@Override
			public Collection<PraeceptaRuleSpace> findByExample(PraeceptaRuleSpace entity) {
				return null;
			}
			
			@Override
			public Collection<PraeceptaRuleSpace> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
				return null;
			}
			
			@Override
			public Collection<PraeceptaRuleSpace> fetchAll() {
				return null;
			}
			
			@Override
			public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
			}
			
			@Override
			public void deleteAll(Collection<PraeceptaRuleSpace> entities) {
			}
			
			@Override
			public void clear() {
			}
			
			@Override
			public PraeceptaRuleSpace fetchByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {
				return null;
			}
		}, new IPraeceptaRuleGroupDao() {
			
			@Override
			public void updateAll(Collection<PraeceptaRuleGroup> entities) {
			}
			
			@Override
			public void update(PraeceptaRuleGroup entity) {
			}
			
			@Override
			public void insertAll(Collection<PraeceptaRuleGroup> entities) {
			}
			
			@Override
			public void insert(PraeceptaRuleGroup entity) {
			}
			
			@Override
			public Collection<PraeceptaRuleGroup> findByExample(PraeceptaRuleGroup entity) {
				return null;
			}
			
			@Override
			public Collection<PraeceptaRuleGroup> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
				return null;
			}
			
			@Override
			public Collection<PraeceptaRuleGroup> fetchAll() {
				return null;
			}
			
			@Override
			public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
			}
			
			@Override
			public void deleteAll(Collection<PraeceptaRuleGroup> entities) {
			}
			
			@Override
			public void delete(PraeceptaRuleGroup entity) {
			}
			
			@Override
			public void clear() {
			}
			
			@Override
			public PraeceptaRuleGroup fetchByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {
				return null;
			}
		});
		
		IPraeceptaRuleSpaceDao ruleSpaceDao = PraeceptaRuleDaoRegister.getRuleSpaceDaoForTypeAndSubType(type, subType);
		
		assertNotNull(ruleSpaceDao);
		
		IPraeceptaRuleGroupDao ruleGroupDao = PraeceptaRuleDaoRegister.getRuleGroupDaoForTypeAndSubType(type, subType);
		
		assertNotNull(ruleGroupDao);
	}

}

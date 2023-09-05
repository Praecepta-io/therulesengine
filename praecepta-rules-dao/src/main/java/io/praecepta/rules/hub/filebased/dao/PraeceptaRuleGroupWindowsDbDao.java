package io.praecepta.rules.hub.filebased.dao;

import java.util.Collection;

import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleTernaryFileDbDao.FILE_TYPE;

public class PraeceptaRuleGroupWindowsDbDao implements  IPraeceptaRuleGroupDao{
	
	private String folderName;
	
	private String grpFileNamePrefix;
	
	private PraeceptaRuleTernaryFileDbDao ternaryFileDbDao;

	public PraeceptaRuleGroupWindowsDbDao(String folderName, String grpFileNamePrefix) {
		this.folderName = folderName;
		this.grpFileNamePrefix = grpFileNamePrefix;
		ternaryFileDbDao = new PraeceptaRuleTernaryFileDbDao(FILE_TYPE.GRP, folderName, grpFileNamePrefix);
	}

	@Override
	public Collection<PraeceptaRuleGroup> fetchAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<PraeceptaRuleGroup> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<PraeceptaRuleGroup> findByExample(PraeceptaRuleGroup entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(PraeceptaRuleGroup entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertAll(Collection<PraeceptaRuleGroup> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PraeceptaRuleGroup entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAll(Collection<PraeceptaRuleGroup> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(PraeceptaRuleGroup entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Collection<PraeceptaRuleGroup> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PraeceptaRuleGroup fetchByKeyAndName(PraeceptaRuleSpaceCompositeKey key, String ruleGroupName) {
		// TODO Auto-generated method stub
		return null;
	}

}

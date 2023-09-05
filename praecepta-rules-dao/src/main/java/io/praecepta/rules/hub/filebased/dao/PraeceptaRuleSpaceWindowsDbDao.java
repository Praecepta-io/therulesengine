package io.praecepta.rules.hub.filebased.dao;

import java.util.Collection;

import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleTernaryFileDbDao.FILE_TYPE;

public class PraeceptaRuleSpaceWindowsDbDao implements  IPraeceptaRuleSpaceDao{

	private String folderName;
	
	private String spaceFileNamePrefix;
	
	private PraeceptaRuleTernaryFileDbDao ternaryFileDbDao;
	
	public PraeceptaRuleSpaceWindowsDbDao(String folderName, String spaceFileNamePrefix) {
		this.folderName = folderName;
		this.spaceFileNamePrefix = spaceFileNamePrefix;
		ternaryFileDbDao = new PraeceptaRuleTernaryFileDbDao(FILE_TYPE.SPACE, folderName, spaceFileNamePrefix);
	}

	@Override
	public Collection<PraeceptaRuleSpace> fetchAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<PraeceptaRuleSpace> fetchByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<PraeceptaRuleSpace> findByExample(PraeceptaRuleSpace entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(PraeceptaRuleSpace entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertAll(Collection<PraeceptaRuleSpace> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PraeceptaRuleSpace entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAll(Collection<PraeceptaRuleSpace> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByIds(Collection<PraeceptaRuleSpaceCompositeKey> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Collection<PraeceptaRuleSpace> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PraeceptaRuleSpace fetchByKeyAndVersion(PraeceptaRuleSpaceCompositeKey key, String version) {
		// TODO Auto-generated method stub
		return null;
	}


}

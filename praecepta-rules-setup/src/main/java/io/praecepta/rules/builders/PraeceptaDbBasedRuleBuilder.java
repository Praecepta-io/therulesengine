package io.praecepta.rules.builders;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import io.praecepta.rules.hub.PraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;

@Service
@EnableCaching(proxyTargetClass = true)
public class PraeceptaDbBasedRuleBuilder  extends PraeceptaPivotalRulesHubManager{

	public PraeceptaDbBasedRuleBuilder(IPraeceptaRuleSpaceDao ruleSpaceDao, IPraeceptaRuleGroupDao ruleGroupDao, IPraeceptaRuleSideCarsDao sideCarsDao) {
		super(ruleSpaceDao, ruleGroupDao, sideCarsDao);
	}
//
//	@Autowired
//	private IPraeceptaRuleSpaceDao ruleSpaceDao;
//
//	@Autowired
//	private IPraeceptaRuleSpaceDao ruleGroupDao;
//
//	public PraeceptaRuleSpace getRuleSpace(String ruleSpaceName) {
//
//		return null;
//	}
//
//	public void persistRuleSpace(PraeceptaRuleSpace ruleSpace) {
//		// to insert rule Space Info
//		ruleSpaceDao.insert(ruleSpace);
//
////		
////		Collection<PraeceptaRuleSpaceDbModel> list= ruleSpaceDao.fetchAll();
////		System.out.println(list);
////		
////		PraeceptaRuleSpaceDbModel modelObj=	(PraeceptaRuleSpaceDbModel) ruleSpaceDao.fetchByKeyAndVersion(ruleSpaceModel.getRuleSpaceCompositeKey(),ruleSpaceModel.getRuleSpaceCompositeKey().getVersion());
////		
////		System.out.println(modelObj);
////		//to insert ruleGroup details
////		//List<PraeceptaRuleGroupDbModel> ruleGrpModels=prepareRuleGroupModel(ruleSpace, ruleSpaceModel.getId());
////		//ruleGroupDao.updateAll(ruleGrpModels);
//	}
//
//	public PraeceptaRuleGroup getRuleGroup(String ruleSpaceName, String clientName, String appName) {
//
//		return null;
//	}
}

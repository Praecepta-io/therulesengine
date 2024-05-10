package io.praecepta.rules.hub.dao.helper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.hub.dao.models.PraeceptaSideCarsInfo;
import io.praecepta.rules.hub.dbbased.model.PraeceptaRuleGroupDbModel;
import io.praecepta.rules.hub.dbbased.model.PraeceptaRuleSideCarsDbModel;
import io.praecepta.rules.hub.dbbased.model.PraeceptaRuleSpaceDbModel;
import io.praecepta.rules.sidecars.model.PraeceptaRuleGroupSideCarsInfo;

public class ConverterDaoHelper {
	private final static Logger logger = LoggerFactory.getLogger(ConverterDaoHelper.class);
//	final static GsonHelperWithAdapter gsonHelperWithAdapter = new GsonHelperWithAdapter(IPraeceptaAction.class, new PraeceptaActionStrategyInterfaceAdapter());

	public static String getKeyRuleSpaceIdCompositeKeyMap(PraeceptaRuleSpaceDbModel objModel) {
		return new StringBuilder(objModel.getSpaceName()).append("_").append(objModel.getAppName()).append("_")
				.append(objModel.getClientId()).append("_").append(objModel.getVersion()).toString();
	}
	
	public static String getKeyRuleGroupIdCompositeKeyMap(PraeceptaRuleGroupDbModel objModel) {
		return new StringBuilder(objModel.getSpaceName()).append("_").append(objModel.getAppName()).append("_")
				.append(objModel.getClientName()).append("_").append(objModel.getVersion()).append("_").append(objModel.getRuleGroupName()).toString();
	}

	/* Method to convert rule space db models to rule space dto objects */
	public static List<PraeceptaRuleSpace> getRuleSpaceList(Collection<PraeceptaRuleSpaceDbModel> ruleSpaceModels) {
		logger.debug("inside getRuleSpaceList ");
		if (!PraeceptaObjectHelper.isObjectEmpty(ruleSpaceModels)) {

			// Converting rule space models to rule space dto objects
			return ruleSpaceModels.stream().map(objModel -> {
				PraeceptaRuleSpace rulespace = new PraeceptaRuleSpace();

				rulespace.setRuleSpaceKey(new PraeceptaRuleSpaceCompositeKey(objModel.getSpaceName(),
						objModel.getClientId(), objModel.getAppName()));

				rulespace.getRuleSpaceKey().setVersion(objModel.getVersion());
				rulespace.setActive(objModel.isActive());

				return rulespace;
			}).collect(Collectors.toList());
		}
		return Collections.EMPTY_LIST;
	}

	public static String prepareKeyRuleSpaceIdMap(PraeceptaRuleSpaceCompositeKey key, String version) {
		StringBuilder keyBuilder = new StringBuilder(key.getSpaceName()).append("_").append(key.getAppName())
				.append("_").append(key.getClientId());

		if (!PraeceptaObjectHelper.isObjectEmpty(version)) {
			keyBuilder.append("_").append(version);
		} else if (!PraeceptaObjectHelper.isObjectEmpty(key.getVersion())) {
			keyBuilder.append("_").append(key.getVersion());
		}
		return keyBuilder.toString();
	}

	/* method to prepare rule space model object to persist */
	public static PraeceptaRuleSpaceDbModel prepareRuleSpaceModel(PraeceptaRuleSpace ruleSpace,Map<String, Long> ruleSpaceIdByCompositeKeyMap) {
		PraeceptaRuleSpaceDbModel ruleSpaceModel = new PraeceptaRuleSpaceDbModel();
		PraeceptaRuleSpaceCompositeKey key = ruleSpace.getRuleSpaceKey();
		BeanUtils.copyProperties(key, ruleSpaceModel);
		BeanUtils.copyProperties(ruleSpace, ruleSpaceModel);
		Long ruleSpaceId = ruleSpaceIdByCompositeKeyMap.get(prepareKeyRuleSpaceIdMap(key));
		if (!PraeceptaObjectHelper.isObjectNull(ruleSpaceId)) {
			ruleSpaceModel.setId(ruleSpaceId);
		}
		
		return ruleSpaceModel;
	}

	public static void prepareListRuleSpaceIdsByKey(PraeceptaRuleSpaceCompositeKey key,
			List<Long> ruleSpaceIdsToDelete,Map<String, Long> ruleSpaceIdByCompositeKeyMap) {
		Long ruleSpaceId = ruleSpaceIdByCompositeKeyMap.get(prepareKeyRuleSpaceIdMap(key));
		if (!PraeceptaObjectHelper.isObjectNull(ruleSpaceIdsToDelete)) {
			ruleSpaceIdsToDelete.add(ruleSpaceId);
		}
	}

	public static List<Long> getIdsListByKey(PraeceptaRuleSpaceCompositeKey key,Map<String, Long> ruleGrpIdByKeyAndGrpNameMap) {
		// preparing the list of ruleGroupIds to delete from DB for key matches with
		// rule space composite key
		return ruleGrpIdByKeyAndGrpNameMap.entrySet().stream().filter(obj -> {
			if (obj.getKey().contains(prepareKeyRuleGroupIdMap(key, null))) {
				return true;
			}
			return false;
		}).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	public static String prepareKeyRuleGroupIdMap(PraeceptaRuleSpaceCompositeKey key,
			String ruleGroupName) {
		StringBuilder keyBuilder = new StringBuilder(key.getSpaceName()).append("_").append(key.getAppName())
				.append("_").append(key.getClientId());

		if (!PraeceptaObjectHelper.isObjectEmpty(key.getVersion())) {
			keyBuilder.append("_").append(key.getVersion());
		}
		if (!PraeceptaObjectHelper.isObjectEmpty(ruleGroupName)) {
			keyBuilder.append("_").append(ruleGroupName);
		}
		return keyBuilder.toString();
	}

	public static PraeceptaRuleGroup convertModelToRuleGroup(PraeceptaRuleGroupDbModel objRuleGroupModel) {

		PraeceptaRuleGroup ruleGroup = new PraeceptaRuleGroup(objRuleGroupModel.getSpaceName(),
				objRuleGroupModel.getClientName(), objRuleGroupModel.getAppName());

		BeanUtils.copyProperties(objRuleGroupModel, ruleGroup);
		ruleGroup.setUniqueId(objRuleGroupModel.getId().toString());
		
		PraeceptaRuleGroup ruleGroupInfo=GsonHelper.toEntity(objRuleGroupModel.getRules(), PraeceptaRuleGroup.class);
		
		if(!PraeceptaObjectHelper.isObjectNull(ruleGroupInfo)) {
			ruleGroup.setPraeceptaCriterias(ruleGroupInfo.getPraeceptaCriterias());
			ruleGroup.setActionToPerform(ruleGroupInfo.getActionToPerform());
			ruleGroup.setActionToPerformOnFailure(ruleGroupInfo.getActionToPerformOnFailure());
		}

		ruleGroup.getRuleSpaceKey().setVersion(objRuleGroupModel.getVersion());

		return ruleGroup;
	}
	
    public static PraeceptaRuleGroupDbModel populateRuleGroupDbModel(PraeceptaRuleGroup ruleGroup) {
    	PraeceptaRuleGroupDbModel rgModel = new PraeceptaRuleGroupDbModel();
		rgModel.setRuleGroupName(ruleGroup.getRuleGroupName());
		BeanUtils.copyProperties(ruleGroup, rgModel);
		if (!PraeceptaObjectHelper.isObjectNull(ruleGroup.getRuleSpaceKey())
				&& !PraeceptaObjectHelper.isObjectNull(ruleGroup.getRuleSpaceKey().getVersion())) {
			rgModel.setVersion(ruleGroup.getRuleSpaceKey().getVersion());
		}
		return rgModel;
    }
    
    public static PraeceptaRuleGroupDbModel convertToRuleGroupDbModel(PraeceptaRuleGroup ruleGroup,Map<String, Long> ruleSpaceIdByCompositeKeyMap,Map<String, Long> ruleGrpIdByKeyAndGrpNameMap) {
    	PraeceptaRuleGroupDbModel rgModel = new PraeceptaRuleGroupDbModel();
		rgModel.setRuleGroupName(ruleGroup.getRuleGroupName());
		BeanUtils.copyProperties(ruleGroup, rgModel);
		rgModel.setVersion(ruleGroup.getRuleSpaceKey().getVersion());
		//Getting ruleSpaceId from map by rule space key
		Long ruleSpaceId = ruleSpaceIdByCompositeKeyMap.get(prepareKeyRuleSpaceIdMap(ruleGroup.getRuleSpaceKey()));
		if (!PraeceptaObjectHelper.isObjectNull(ruleSpaceId)) {
			rgModel.setRuleSpaceId(ruleSpaceId);
		}
		//checking rule groupId for compositeKey and rule group name combination
		Long ruleGroupId = ruleGrpIdByKeyAndGrpNameMap.get(prepareKeyRuleGroupIdMap(ruleGroup.getRuleSpaceKey(),ruleGroup.getRuleGroupName()));
		if (!PraeceptaObjectHelper.isObjectNull(ruleGroupId)) {
			rgModel.setId(ruleGroupId);
		}else if (!PraeceptaObjectHelper.isObjectEmpty(ruleGroup.getUniqueId())) {
			rgModel.setId(Long.parseLong(ruleGroup.getUniqueId()));
		}
		rgModel.setRules(GsonHelper.toJson(ruleGroup));
		return rgModel;
    }
    
    public static List<Long> getLstRuleGroupIdsByKeyAndName(PraeceptaRuleSpaceCompositeKey key,String ruleGroupName,Map<String, Long> ruleGrpIdByKeyAndGrpNameMap){
		 //preparing the list of ruleGroupIds to delete from DB for key matches with rule space  composite key  
		// and rule groupanme
		return ruleGrpIdByKeyAndGrpNameMap.entrySet().stream().filter(obj -> {
			if (obj.getKey().contains(prepareKeyRuleGroupIdMap(key,ruleGroupName))) {
				return true;
			}
			return false;
		}).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	public static String prepareKeyRuleSpaceIdMap(PraeceptaRuleSpaceCompositeKey key) {
		return prepareKeyRuleSpaceIdMap(key, key.getVersion());
	}
	
	public static Predicate<PraeceptaRuleSpaceDbModel> matchRuleSpaceKey(PraeceptaRuleSpaceCompositeKey key){
		logger.debug("check for rule space key");
		
		return (obj)->{
			if (obj.getSpaceName().equalsIgnoreCase(key.getSpaceName())
					&& obj.getAppName().equalsIgnoreCase(key.getAppName())
					&& obj.getClientId().equalsIgnoreCase(key.getClientId())) {

				if (!PraeceptaObjectHelper.isStringEmpty(key.getVersion())) {
					return obj.getVersion().equalsIgnoreCase(key.getVersion());
				}
				return true;
			}
			return false;
		};
	}
	
	public static Predicate<PraeceptaRuleGroupDbModel> matchKeyAndRuleGroupName(String ruleGroupName,PraeceptaRuleSpaceCompositeKey key){
		logger.debug("check for rule space key and rule group name");
		
		return (obj)->{
			
			return ruleSpaceKeyMatch(key, obj)&&ruleGroupNameMatch(ruleGroupName, obj);
		};
	}
	
	private static boolean ruleSpaceKeyMatch(PraeceptaRuleSpaceCompositeKey key,PraeceptaRuleGroupDbModel obj) {
		if (obj.getSpaceName().equalsIgnoreCase(key.getSpaceName())
				&& obj.getAppName().equalsIgnoreCase(key.getAppName())
				&& obj.getClientName().equalsIgnoreCase(key.getClientId())) {

			if (!PraeceptaObjectHelper.isStringEmpty(key.getVersion())) {
				return obj.getVersion().equalsIgnoreCase(key.getVersion());
			}
			return true;
		}
		return false;
	}
	private static boolean ruleGroupNameMatch(String ruleGroupName,PraeceptaRuleGroupDbModel obj) {
		if (!PraeceptaObjectHelper.isStringEmpty(ruleGroupName)) {
			return obj.getRuleGroupName().equalsIgnoreCase(ruleGroupName);
		}
		return true;
	}
	
	// Start for Rule Side Cars
	public static Predicate<PraeceptaRuleSideCarsDbModel> matchSidecarsByKeyAndRuleGrpName(String ruleGroupName,PraeceptaRuleSpaceCompositeKey key){
		logger.debug("check for rule space key and rule group name");
		
		return (obj)->{
			
			return ruleSpaceKeyCheck(key, obj)&&ruleGroupNameCheck(ruleGroupName, obj);
		};
	}
	
	private static boolean ruleSpaceKeyCheck(PraeceptaRuleSpaceCompositeKey key,PraeceptaRuleSideCarsDbModel obj) {
		if (obj.getSpaceName().equalsIgnoreCase(key.getSpaceName())
				&& obj.getAppName().equalsIgnoreCase(key.getAppName())
				&& obj.getClientName().equalsIgnoreCase(key.getClientId())) {

			if (!PraeceptaObjectHelper.isStringEmpty(key.getVersion())) {
				return obj.getVersion().equalsIgnoreCase(key.getVersion());
			}
			return true;
		}
		return false;
	}
	private static boolean ruleGroupNameCheck(String ruleGroupName,PraeceptaRuleSideCarsDbModel obj) {
		if (!PraeceptaObjectHelper.isStringEmpty(ruleGroupName)) {
			return obj.getRuleGroupName().equalsIgnoreCase(ruleGroupName);
		}
		return true;
	}
	
	public static PraeceptaSideCarsInfo convertModelToRuleSideCars(PraeceptaRuleSideCarsDbModel objRuleSideCarModel) {

		PraeceptaSideCarsInfo ruleSideCar = new PraeceptaSideCarsInfo(objRuleSideCarModel.getSpaceName(),
				objRuleSideCarModel.getClientName(), objRuleSideCarModel.getAppName(), objRuleSideCarModel.getVersion());

		BeanUtils.copyProperties(objRuleSideCarModel, ruleSideCar);
		
		ruleSideCar.setRuleGroupSideCars(GsonHelper.toEntity(objRuleSideCarModel.getMetaData(), PraeceptaRuleGroupSideCarsInfo.class));

		ruleSideCar.getRuleSpaceKey().setVersion(objRuleSideCarModel.getVersion());

		return ruleSideCar;
	}
	public static String getRuleSideCarsIdMapKeyMap(PraeceptaRuleSideCarsDbModel objModel) {
		return new StringBuilder(objModel.getSpaceName()).append("_").append(objModel.getAppName()).append("_")
				.append(objModel.getClientName()).append("_").append(objModel.getVersion()).append("_").append(objModel.getRuleGroupName()).toString();
	}
	
	public static PraeceptaRuleSideCarsDbModel populateRuleSideCarDbModel(PraeceptaSideCarsInfo ruleSideCar) {
		PraeceptaRuleSideCarsDbModel ruleSideCarDbModel = new PraeceptaRuleSideCarsDbModel();
		ruleSideCarDbModel.setRuleGroupName(ruleSideCar.getRuleGroupSideCars().getRuleGrpName());
		BeanUtils.copyProperties(ruleSideCar, ruleSideCarDbModel);
		if (!PraeceptaObjectHelper.isObjectNull(ruleSideCar.getRuleSpaceKey())
				&& !PraeceptaObjectHelper.isObjectNull(ruleSideCar.getRuleSpaceKey().getVersion())) {
			ruleSideCarDbModel.setVersion(ruleSideCar.getRuleSpaceKey().getVersion());
		}
		return ruleSideCarDbModel;
    }
	
	public static PraeceptaRuleSideCarsDbModel convertToRuleSideCarDbModel(PraeceptaSideCarsInfo ruleSideCar,Map<String, Long> ruleSideCarIdByKeyAndGrpNameMap) {
		PraeceptaRuleSideCarsDbModel ruleSideCarModel = new PraeceptaRuleSideCarsDbModel();
		ruleSideCarModel.setRuleGroupName(ruleSideCar.getRuleGroupSideCars().getRuleGrpName());
		BeanUtils.copyProperties(ruleSideCar, ruleSideCarModel);
		ruleSideCarModel.setVersion(ruleSideCar.getRuleSpaceKey().getVersion());
		
		//checking rule side car Id for compositeKey and rule group name combination
		Long ruleSideCarId = ruleSideCarIdByKeyAndGrpNameMap.get(prepareKeyRuleGroupIdMap(ruleSideCar.getRuleSpaceKey(),ruleSideCar.getRuleGroupSideCars().getRuleGrpName()));
		if (!PraeceptaObjectHelper.isObjectNull(ruleSideCarId)) {
			ruleSideCarModel.setId(ruleSideCarId);
		}
		ruleSideCarModel.setMetaData(GsonHelper.toJson(ruleSideCar.getRuleGroupSideCars()));
		return ruleSideCarModel;
    }
}

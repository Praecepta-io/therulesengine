package io.praecepta.rules.engine.execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.ObjectHelper;
import io.praecepta.rules.builders.PraeceptaRuleBuilder;
import io.praecepta.rules.engine.rule.sidecars.PraeceptaDefaultPostRuleSideCarInjector;
import io.praecepta.rules.engine.rule.sidecars.PraeceptaDefaultPreRuleSideCarInjector;
import io.praecepta.rules.engine.rulegrp.sidecars.PraeceptaDefaultPostRuleGrpSideCarInjector;
import io.praecepta.rules.engine.rulegrp.sidecars.PraeceptaDefaultPreRuleGrpSideCarInjector;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.engine.sidecars.GenericPraeceptaRuleLevelInfoTrackerSideCarInjector;
import io.praecepta.rules.engine.sidecars.IPraeceptaInfoTrackerSideCarInjector;
import io.praecepta.rules.enums.PraeceptaRuleRequestStoreType;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleGroup;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpace;
import io.praecepta.rules.hub.dao.models.PraeceptaRuleSpaceCompositeKey;
import io.praecepta.rules.model.PraeceptaCriteria;
import io.praecepta.rules.model.projection.IPraeceptaAction;

/**
 * 
 * @author rajasrikar
 * 
 *         - Build the Rule space first. Default can be a File load - Validate
 *         each rule space, criteria format to make sure all rules have a
 *         condition and action to perform - loop thru the each Criteria, then
 *         Call Basic Validation function, Evaluate the Condition, then Take
 *         Action of Either OnMatch, or on UnMatch
 */
public abstract class AbstractPraeceptaRuleExecutionEngine
		implements IPraeceptaRuleExecutionEngine<PraeceptaRuleSpace, PraeceptaRequestStore> {

	private final static Logger logger = LoggerFactory.getLogger(AbstractPraeceptaRuleExecutionEngine.class);

	private PraeceptaRuleBuilder ruleBuider = initializeRuleBuilder();

	/*private GenericPraeceptaInfoTrackerSideCarInjector preRuleGrpSideCar = new PraeceptaDefaultPreRuleGrpSideCarInjector();

	private GenericPraeceptaInfoTrackerSideCarInjector preRuleSideCar = new PraeceptaDefaultPreRuleSideCarInjector();

	private GenericPraeceptaInfoTrackerSideCarInjector postRuleSideCar = new PraeceptaDefaultPostRuleSideCarInjector();

	private GenericPraeceptaInfoTrackerSideCarInjector postRuleGrpSideCar = new PraeceptaDefaultPostRuleGrpSideCarInjector();*/
	
	private boolean shutDownEngineTriggered;
	
	// <PraeceptaRuleSpaceCompositeKey [Space name, client id, appname], <Version, rulespace>>
	private Map<PraeceptaRuleSpaceCompositeKey, Map<String, PraeceptaRuleSpace>> ruleSpacesMap = new HashMap<>();
	
	protected PraeceptaRuleBuilder initializeRuleBuilder() {
		
		return PraeceptaRuleBuilder.buildWithDefaultProps();
	}

	@Override
	public PraeceptaRuleSpace buildRuleSapce(PraeceptaRuleSpaceCompositeKey ruleSpaceKey) {

		PraeceptaRuleSpace ruleSpaceToReturn = null;

		if (!ObjectHelper.isObjectEmpty(ruleSpaceKey)) {

			String verison = ruleSpaceKey.getVersion();

			if (!ObjectHelper.isObjectEmpty(verison)) {

				if (!ObjectHelper.isObjectEmpty(ruleSpacesMap)) {

					Map<String, PraeceptaRuleSpace> versionToRuleSpaceMap = ruleSpacesMap.get(ruleSpaceKey);

					if (!ObjectHelper.isObjectEmpty(versionToRuleSpaceMap)) {

						if (!ObjectHelper.isObjectEmpty(ruleSpaceKey.getVersion())) {
							ruleSpaceToReturn = versionToRuleSpaceMap.get(ruleSpaceKey.getVersion());
						}
					}
				}

				// Build the Rule Space incase the space doesn't exist in the Local Cache 
				if (ObjectHelper.isObjectEmpty(ruleSpaceToReturn)) {
					ruleSpaceToReturn = ruleBuider.build(ruleSpaceKey.getSpaceName(), ruleSpaceKey.getClientId(),
							ruleSpaceKey.getAppName(), ruleSpaceKey.getVersion());
				}
			} else {

				logger.error(
						"To Get Rule Space, it requires to have version information to be passed in the Space key  ");
			}
		}

		return ruleSpaceToReturn;
	}

	@Override
	public boolean shutDownRuleEngine() {
		return false;
	}
	
	@Override
	public Collection<PraeceptaRuleSpace> buildRuleSapces() {

		logger.debug("Inside buildRuleSapce ");
		
		Collection<PraeceptaRuleSpace> ruleSpaces =  Collections.emptyList();
		
		if(!ObjectHelper.isObjectEmpty(ruleSpacesMap)) {
			
			Collection<PraeceptaRuleSpace> ruleSpacesFromMap = new ArrayList<>(); 
			
			ruleSpacesMap.forEach( (spacekey, versionToRuleSpaceMap) -> {
				
				ruleSpacesFromMap.add(versionToRuleSpaceMap.get(spacekey.getVersion()));
			});
			
			ruleSpaces = ruleSpacesFromMap;
			
		} else {
			ruleSpaces = ruleBuider.buildAll();
			
			if(!ObjectHelper.isObjectEmpty(ruleSpaces)) {
				ruleSpaces.stream().forEach( eachSpace -> {
					
					PraeceptaRuleSpaceCompositeKey spaceKey = eachSpace.getRuleSpaceKey();
					
					ruleSpacesMap.putIfAbsent(spaceKey, new HashMap<String, PraeceptaRuleSpace>());
					
					Map<String, PraeceptaRuleSpace> versionToRuleSpaceMap = ruleSpacesMap.get(spaceKey);
					
					versionToRuleSpaceMap.put(spaceKey.getVersion(), eachSpace);
				});
			}
		}

		return ruleSpaces;
	}

	@Override
	public void performRuleEngineExecution(PraeceptaRequestStore ruleStore) {

		logger.debug("Inside performRuleEngineExecution ");
		
		if(shutDownEngineTriggered) {
			
			logger.warn("Rules Engine Shut Down is Triggered. Unable to Perform any Execution ");
			return;
		}

		PraeceptaRuleSpace ruleSpace = (PraeceptaRuleSpace) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_SPACE);
		
		preExecutionChecks(ruleStore);

		// Side Car is nothing but an Injector
		// This Method will attach Injectors to perform Pre Rule Group
		// this is to attach any Pre Injectors or Listeners to Capture Additional Checks
		// or Capture Audit or Status etc
		attachPreRuleGroupSideCar(ruleStore);

		performOrderingOfTheRules(ruleSpace);

		// Side Car for Before Each Rule Triggering
		// This Method will attach Injectors to perform Pre Rule Run
		// this is to attach any Injectors or Listeners to Capture Additional Checks or
		// Capture Audit or Status etc
		attachPreRuleSideCar(ruleStore);

		triggerRules(ruleStore);

		// Side Car for After Each Rule Triggering
		// This Method will attach Injectors to perform Post Rule Run
		// this is to attach any Injectors or Listeners to Capture Additional Checks or
		// Capture Audit or Status etc
		attachPostRuleSideCar(ruleStore);

		postExecutionChecks(ruleStore);

		// Side Car is nothing but an Injector
		// This Method will attach Injectors to perform Post Rule Group
		// this is to attach any Post Injectors or Capture Audit or Status etc
		attachPostRuleGroupSideCar(ruleStore);
		
		//to trigger configured Rule Group actions on success/failure
		triggerRuleGroupActions(ruleStore);

		logger.debug("Exiting performRuleEngineExecution ");

	}

	protected abstract void triggerRules(PraeceptaRequestStore ruleStore);

	protected abstract void postExecutionChecks(PraeceptaRequestStore ruleStore);

	protected boolean performAdditionalRuleSpaceChecks(PraeceptaRuleSpace ruleSpace) {
		return true;
	};

	protected void preExecutionChecks(PraeceptaRequestStore ruleStore) {

		logger.debug("Inside preExecutionChecks ");

		PraeceptaRuleSpace ruleSpace = (PraeceptaRuleSpace) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_SPACE);

		if (ruleSpace == null || ObjectHelper.isObjectEmpty(ruleSpace.getPraeceptaRuleGrps())) {
			ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_VALIDATION_STATUS, false);
			return;
		}

		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.RULES_REQUEST_VALIDATION_STATUS,
				performAdditionalRuleSpaceChecks(ruleSpace));

		logger.debug("Exiting preExecutionChecks ");
	};

	protected void performOrderingOfTheRules(PraeceptaRuleSpace ruleSpace) {

		logger.debug("Inside performOrderingOfTheRules ");

		if (ruleSpace != null && !ObjectHelper.isObjectEmpty(ruleSpace.getPraeceptaRuleGrps())) {
			Collection<PraeceptaRuleGroup> praeceptaRuleGrps = ruleSpace.getPraeceptaRuleGrps();

			praeceptaRuleGrps.forEach(eachRuleGrp -> {

				if (eachRuleGrp != null && !ObjectHelper.isObjectEmpty(eachRuleGrp.getPraeceptaCriterias())) {
//					Collection<PraeceptaCriteria> praeceptaCriterias = eachRuleGrp.getPraeceptaCriterias();

//					praeceptaCriterias.iterator().

					List<PraeceptaCriteria> orderedCriterias = eachRuleGrp.getPraeceptaOrderedCriterias();
					/*
					 * Collections.sort(praeceptaCriterias,
					 * Comparator.comparing(PraeceptaCriteria::getOrderNumber) );
					 */
				}
			});
		}

		logger.debug("Exiting performOrderingOfTheRules ");
	}

	protected void attachPreRuleSideCar(PraeceptaRequestStore ruleStore) {

		logger.debug("Inside attachPreRuleSideCar ");
		
		GenericPraeceptaRuleLevelInfoTrackerSideCarInjector preRuleSideCar = new PraeceptaDefaultPreRuleSideCarInjector();

		List<IPraeceptaInfoTrackerSideCarInjector> preBeforeRuleSideCars = (List<IPraeceptaInfoTrackerSideCarInjector>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_BEFORE_RULE_SIDE_CARS);

		List<IPraeceptaInfoTrackerSideCarInjector> preAfterRuleSideCars = (List<IPraeceptaInfoTrackerSideCarInjector>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_AFTER_RULE_SIDE_CARS);

		if (!ObjectHelper.isObjectEmpty(preBeforeRuleSideCars)) {
			preRuleSideCar.setBeforeExecutionSideCars(preBeforeRuleSideCars);
		}
		
		addPreRuleExecutionSideCars(ruleStore, preRuleSideCar);
		
		if (!ObjectHelper.isObjectEmpty(preAfterRuleSideCars)) {
			preRuleSideCar.setAfterExecutionSideCars(preAfterRuleSideCars);
		}

		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_SIDE_CAR, preRuleSideCar);
		
		logger.debug("Exiting attachPreRuleSideCar ");

	}

	protected void addPreRuleExecutionSideCars(PraeceptaRequestStore ruleStore,
			GenericPraeceptaRuleLevelInfoTrackerSideCarInjector preRuleSideCar) {
		
		Map<String, List<IPraeceptaInfoTrackerSideCarInjector>> ruleNameToExecutionSideCars =  (Map<String, List<IPraeceptaInfoTrackerSideCarInjector>>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_EXECUTION_SIDE_CARS);
		
		preRuleSideCar.setRuleNameToExecutionSideCars(ruleNameToExecutionSideCars);
		
	}

	protected void attachPostRuleSideCar(PraeceptaRequestStore ruleStore) {

		logger.debug("Inside attachPostRuleSideCar ");
		
		GenericPraeceptaRuleLevelInfoTrackerSideCarInjector postRuleSideCar = new PraeceptaDefaultPostRuleSideCarInjector();

		List<IPraeceptaInfoTrackerSideCarInjector> postBeforeRuleSideCars = (List<IPraeceptaInfoTrackerSideCarInjector>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_BEFORE_RULE_SIDE_CARS);
		
		List<IPraeceptaInfoTrackerSideCarInjector> postAfterRuleSideCars = (List<IPraeceptaInfoTrackerSideCarInjector>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_AFTER_RULE_SIDE_CARS);

		if (!ObjectHelper.isObjectEmpty(postBeforeRuleSideCars)) {
			postRuleSideCar.setBeforeExecutionSideCars(postBeforeRuleSideCars);
		}
		
		addPostRuleExecutionSideCars(ruleStore, postRuleSideCar);

		if (!ObjectHelper.isObjectEmpty(postAfterRuleSideCars)) {
			postRuleSideCar.setAfterExecutionSideCars(postAfterRuleSideCars);
		}
		
		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_SIDE_CAR, postRuleSideCar);

		logger.debug("Exiting attachPostRuleSideCar ");
	}

	protected void addPostRuleExecutionSideCars(PraeceptaRequestStore ruleStore,
			GenericPraeceptaRuleLevelInfoTrackerSideCarInjector postRuleSideCar) {
		
		Map<String, List<IPraeceptaInfoTrackerSideCarInjector>> ruleNameToExecutionSideCars =  (Map<String, List<IPraeceptaInfoTrackerSideCarInjector>>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_EXECUTION_SIDE_CARS);
				
		postRuleSideCar.setRuleNameToExecutionSideCars(ruleNameToExecutionSideCars);
	}
	
	protected void attachPreRuleGroupSideCar(PraeceptaRequestStore ruleStore) {

		logger.debug("Inside attachPreRuleGroupSideCar ");
		
		GenericPraeceptaInfoTrackerSideCarInjector preRuleGrpSideCar = new PraeceptaDefaultPreRuleGrpSideCarInjector();

		List<IPraeceptaInfoTrackerSideCarInjector> preBeforeRuleGrpSideCars = (List<IPraeceptaInfoTrackerSideCarInjector>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_BEFORE_RULE_GROUP_SIDE_CARS);

		List<IPraeceptaInfoTrackerSideCarInjector> preRuleGrpExecSideCars = (List<IPraeceptaInfoTrackerSideCarInjector>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_GROUP_EXECUTION_SIDE_CARS);

		List<IPraeceptaInfoTrackerSideCarInjector> preAfterRuleGrpSideCars = (List<IPraeceptaInfoTrackerSideCarInjector>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_AFTER_RULE_GROUP_SIDE_CARS);

		if (!ObjectHelper.isObjectEmpty(preBeforeRuleGrpSideCars)) {
			preRuleGrpSideCar.setBeforeExecutionSideCars(preBeforeRuleGrpSideCars);
		}

		if (!ObjectHelper.isObjectEmpty(preRuleGrpExecSideCars)) {
			preRuleGrpSideCar.setExecutionSideCars(preRuleGrpExecSideCars);
		}

		if (!ObjectHelper.isObjectEmpty(preAfterRuleGrpSideCars)) {
			preRuleGrpSideCar.setAfterExecutionSideCars(preAfterRuleGrpSideCars);
		}

		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.PRE_RULE_GROUP_SIDE_CAR, preRuleGrpSideCar);

		logger.debug("Exiting attachPreRuleGroupSideCar ");
	}

	protected void attachPostRuleGroupSideCar(PraeceptaRequestStore ruleStore) {

		logger.debug("Inside attachPostRuleGroupSideCar ");
		
		GenericPraeceptaInfoTrackerSideCarInjector postRuleGrpSideCar = new PraeceptaDefaultPostRuleGrpSideCarInjector();

		List<IPraeceptaInfoTrackerSideCarInjector> postBeforeRuleGrpSideCars = (List<IPraeceptaInfoTrackerSideCarInjector>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_BEFORE_RULE_GROUP_SIDE_CARS);
		
		List<IPraeceptaInfoTrackerSideCarInjector> postRuleGrpExecSideCars = (List<IPraeceptaInfoTrackerSideCarInjector>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_GROUP_EXECUTION_SIDE_CARS);

		List<IPraeceptaInfoTrackerSideCarInjector> postAfterRuleGrpSideCars = (List<IPraeceptaInfoTrackerSideCarInjector>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.POST_AFTER_RULE_GROUP_SIDE_CARS);

		if (!ObjectHelper.isObjectEmpty(postBeforeRuleGrpSideCars)) {
			postRuleGrpSideCar.setBeforeExecutionSideCars(postBeforeRuleGrpSideCars);
		}
		
		if (!ObjectHelper.isObjectEmpty(postRuleGrpExecSideCars)) {
			postRuleGrpSideCar.setExecutionSideCars(postRuleGrpExecSideCars);
		}

		if (!ObjectHelper.isObjectEmpty(postAfterRuleGrpSideCars)) {
			postRuleGrpSideCar.setAfterExecutionSideCars(postAfterRuleGrpSideCars);
		}

		ruleStore.upsertToPraeceptaStore(PraeceptaRuleRequestStoreType.POST_RULE_GROUP_SIDE_CAR, postRuleGrpSideCar);
		
		logger.debug("Exiting attachPostRuleGroupSideCar ");
	}

	@Override
	public boolean shutDownRuleEngineExecution(PraeceptaRuleSpace ruleSpace) {
		return false;
	}

	public PraeceptaRuleBuilder getRuleBuider() {
		return ruleBuider;
	}

	public void setRuleBuider(PraeceptaRuleBuilder ruleBuider) {
		this.ruleBuider = ruleBuider;
	}
	
	protected void triggerRuleGroupActions(PraeceptaRequestStore ruleStore) {
		
		logger.info("Inside triggerRuleGroupActions ");

		List<IPraeceptaAction<PraeceptaRequestStore>> ruleGroupActions = (List<IPraeceptaAction<PraeceptaRequestStore>>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_ACTIONS);

		if (!ObjectHelper.isObjectEmpty(ruleGroupActions)) {
			ruleGroupActions.forEach(action -> {
				action.performAction(ruleStore);
			});
		}
		
		List<IPraeceptaAction<PraeceptaRequestStore>> ruleGroupActionsOnFailure = (List<IPraeceptaAction<PraeceptaRequestStore>>) ruleStore
				.fetchFromPraeceptaStore(PraeceptaRuleRequestStoreType.RULE_GROUP_ACTIONS_ON_FAILURE);

		if (!ObjectHelper.isObjectEmpty(ruleGroupActionsOnFailure)) {
			ruleGroupActionsOnFailure.forEach(action -> {
				action.performAction(ruleStore);
			});
		}
	}
}

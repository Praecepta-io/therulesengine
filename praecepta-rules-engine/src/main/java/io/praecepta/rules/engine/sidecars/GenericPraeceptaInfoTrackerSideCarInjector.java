package io.praecepta.rules.engine.sidecars;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;

public class GenericPraeceptaInfoTrackerSideCarInjector implements IPraeceptaInfoTrackerSideCarInjector{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(GenericPraeceptaInfoTrackerSideCarInjector.class);

//	private List<IPraeceptaInfoTrackerSideCarInjector> beforeExecutionSideCars = Collections.emptyList();

	private List<IPraeceptaInfoTrackerSideCarInjector> executionSideCars = Collections.emptyList();

//	private List<IPraeceptaInfoTrackerSideCarInjector> afterExecutionSideCars = Collections.emptyList();
	
	@Override
	public void trackAndCaptureInitialInfo(PraeceptaRequestStore ruleStore) {
		
		LOGGER.debug(" Inside trackAndCaptureInitialInfo of GenericPraeceptaInfoTrackerSideCarInjector");

		/*if(!PraeceptaObjectHelper.isObjectEmpty(beforeExecutionSideCars)) {
			beforeExecutionSideCars.forEach( eachBeforeExecutionSideCar -> eachBeforeExecutionSideCar.trackAndCaptureInitialInfo(ruleStore));
		}*/
		
		// Perform Default Side Car Operations here 
		captureInInfo(ruleStore);
		
		/*if(!PraeceptaObjectHelper.isObjectEmpty(afterExecutionSideCars)) {
			afterExecutionSideCars.forEach( eachAfterExecutionSideCar -> eachAfterExecutionSideCar.trackAndCaptureInitialInfo(ruleStore));
		}*/
		
		LOGGER.debug(" Exiting trackAndCaptureInitialInfo of GenericPraeceptaInfoTrackerSideCarInjector");
		
	}

	public void captureInInfo(PraeceptaRequestStore ruleStore) {
	}
	
	@Override
	public void executeSideCar(PraeceptaRequestStore ruleStore) {
		
		LOGGER.debug(" Inside executeSideCar of GenericPraeceptaInfoTrackerSideCarInjector");

		if(!PraeceptaObjectHelper.isObjectEmpty(executionSideCars)) {
			triggerSideCars(executionSideCars, ruleStore);
		}
	}

	protected void triggerSideCars(List<IPraeceptaInfoTrackerSideCarInjector> sideCarsToTrigger,
			PraeceptaRequestStore ruleStore) {
		
		LOGGER.debug(" Inside triggerSideCars of GenericPraeceptaInfoTrackerSideCarInjector");
		
		if(!PraeceptaObjectHelper.isObjectEmpty(sideCarsToTrigger) && !PraeceptaObjectHelper.isObjectEmpty(ruleStore)) {
			
			LOGGER.debug(" Number of Sidecars to Trigger -> {} ", sideCarsToTrigger.size());
			
			sideCarsToTrigger.forEach(eachSideCarToExecute -> {
	
				eachSideCarToExecute.trackAndCaptureInitialInfo(ruleStore);
				eachSideCarToExecute.executeSideCar(ruleStore);
				eachSideCarToExecute.trackAndCaptureExitInfo(ruleStore);
			});
		
		}
		
		LOGGER.debug(" Exiting triggerSideCars of GenericPraeceptaInfoTrackerSideCarInjector");
	}

	@Override
	public void trackAndCaptureExitInfo(PraeceptaRequestStore ruleStore) {
		
		LOGGER.debug(" Inside trackAndCaptureExitInfo of GenericPraeceptaInfoTrackerSideCarInjector");

		/*if(!PraeceptaObjectHelper.isObjectEmpty(beforeExecutionSideCars)) {
			beforeExecutionSideCars.forEach( eachBeforeExecutionSideCar -> eachBeforeExecutionSideCar.trackAndCaptureExitInfo(ruleStore));
		}*/
		
		// Perform Default Side Car Operations here 
		captureExitInfo(ruleStore);
		
		/*if(!PraeceptaObjectHelper.isObjectEmpty(afterExecutionSideCars)) {
			afterExecutionSideCars.forEach( eachAfterExecutionSideCar -> eachAfterExecutionSideCar.trackAndCaptureExitInfo(ruleStore));
		}*/
		
		LOGGER.debug(" Exitng trackAndCaptureExitInfo of GenericPraeceptaInfoTrackerSideCarInjector");
	}

	public void captureExitInfo(PraeceptaRequestStore ruleStore) {
	}

	/*public List<IPraeceptaInfoTrackerSideCarInjector> getBeforeExecutionSideCars() {
		return Collections.unmodifiableList(beforeExecutionSideCars);
	}

	public void setBeforeExecutionSideCars(List<IPraeceptaInfoTrackerSideCarInjector> beforeExecutionSideCars) {
		this.beforeExecutionSideCars = beforeExecutionSideCars;
	}

	public List<IPraeceptaInfoTrackerSideCarInjector> getAfterExecutionSideCars() {
		return Collections.unmodifiableList(afterExecutionSideCars);
	}

	public void setAfterExecutionSideCars(List<IPraeceptaInfoTrackerSideCarInjector> afterExecutionSideCars) {
		this.afterExecutionSideCars = afterExecutionSideCars;
	}*/

	public List<IPraeceptaInfoTrackerSideCarInjector> getExecutionSideCars() {
		return Collections.unmodifiableList(executionSideCars);
	}

	public void setExecutionSideCars(List<IPraeceptaInfoTrackerSideCarInjector> executionSideCars) {
		this.executionSideCars = executionSideCars;
	}

}

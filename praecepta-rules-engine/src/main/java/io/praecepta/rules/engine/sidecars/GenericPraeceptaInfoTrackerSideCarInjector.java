package io.praecepta.rules.engine.sidecars;

import java.util.Collections;
import java.util.List;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.ObjectHelper;

public class GenericPraeceptaInfoTrackerSideCarInjector implements IPraeceptaInfoTrackerSideCarInjector{

	private List<IPraeceptaInfoTrackerSideCarInjector> beforeExecutionSideCars = Collections.emptyList();

	private List<IPraeceptaInfoTrackerSideCarInjector> executionSideCars = Collections.emptyList();

	private List<IPraeceptaInfoTrackerSideCarInjector> afterExecutionSideCars = Collections.emptyList();
	
	@Override
	public void trackAndCaptureInitialInfo(PraeceptaRequestStore ruleStore) {

		if(!ObjectHelper.isObjectEmpty(beforeExecutionSideCars)) {
			beforeExecutionSideCars.forEach( eachBeforeExecutionSideCar -> eachBeforeExecutionSideCar.trackAndCaptureInitialInfo(ruleStore));
		}
		
		// Perform Default Side Car Operations here 
		captureInInfo(ruleStore);
		
		if(!ObjectHelper.isObjectEmpty(afterExecutionSideCars)) {
			afterExecutionSideCars.forEach( eachAfterExecutionSideCar -> eachAfterExecutionSideCar.trackAndCaptureInitialInfo(ruleStore));
		}
		
	}

	public void captureInInfo(PraeceptaRequestStore ruleStore) {
	}
	
	@Override
	public void executeSideCar(PraeceptaRequestStore ruleStore) {

		if(!ObjectHelper.isObjectEmpty(executionSideCars)) {
			triggerSideCars(executionSideCars, ruleStore);
		}
	}

	protected void triggerSideCars(List<IPraeceptaInfoTrackerSideCarInjector> sideCarsToTrigger,
			PraeceptaRequestStore ruleStore) {
		sideCarsToTrigger.forEach(eachSideCarToExecute -> {

			eachSideCarToExecute.trackAndCaptureInitialInfo(ruleStore);
			eachSideCarToExecute.executeSideCar(ruleStore);
			eachSideCarToExecute.trackAndCaptureExitInfo(ruleStore);
		});
	}

	@Override
	public void trackAndCaptureExitInfo(PraeceptaRequestStore ruleStore) {

		if(!ObjectHelper.isObjectEmpty(beforeExecutionSideCars)) {
			beforeExecutionSideCars.forEach( eachBeforeExecutionSideCar -> eachBeforeExecutionSideCar.trackAndCaptureExitInfo(ruleStore));
		}
		
		// Perform Default Side Car Operations here 
		captureExitInfo(ruleStore);
		
		if(!ObjectHelper.isObjectEmpty(afterExecutionSideCars)) {
			afterExecutionSideCars.forEach( eachAfterExecutionSideCar -> eachAfterExecutionSideCar.trackAndCaptureExitInfo(ruleStore));
		}
		
	}

	public void captureExitInfo(PraeceptaRequestStore ruleStore) {
	}

	public List<IPraeceptaInfoTrackerSideCarInjector> getBeforeExecutionSideCars() {
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
	}

	public List<IPraeceptaInfoTrackerSideCarInjector> getExecutionSideCars() {
		return Collections.unmodifiableList(executionSideCars);
	}

	public void setExecutionSideCars(List<IPraeceptaInfoTrackerSideCarInjector> executionSideCars) {
		this.executionSideCars = executionSideCars;
	}

}

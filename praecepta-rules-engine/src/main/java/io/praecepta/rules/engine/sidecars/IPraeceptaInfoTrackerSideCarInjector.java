package io.praecepta.rules.engine.sidecars;

import io.praecepta.core.data.PraeceptaRequestStore;

public interface IPraeceptaInfoTrackerSideCarInjector {
	
	void trackAndCaptureInitialInfo(PraeceptaRequestStore ruleStore);
	
	default void executeSideCar(PraeceptaRequestStore ruleStore) {
		
	}
	
	void trackAndCaptureExitInfo(PraeceptaRequestStore ruleStore);
}

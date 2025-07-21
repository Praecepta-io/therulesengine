package io.praecepta.core.data.intf;

import java.util.Collection;

@FunctionalInterface
public interface IPraeceptaDataProcessor<E> {
	
	void processData(Collection<E> dataToProcess);

}

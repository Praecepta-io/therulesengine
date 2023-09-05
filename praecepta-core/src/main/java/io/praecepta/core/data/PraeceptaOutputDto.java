package io.praecepta.core.data;

import io.praecepta.core.data.intf.IDto;

public class PraeceptaOutputDto implements IDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4203407107135122261L;
	
	private Object outputData;

	public Object getOutputData() {
		return outputData;
	}

	public void setOutputData(Object outputData) {
		this.outputData = outputData;
	}
	
}

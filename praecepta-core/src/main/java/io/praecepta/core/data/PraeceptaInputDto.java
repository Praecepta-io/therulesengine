package io.praecepta.core.data;

import io.praecepta.core.data.intf.IDto;

public class PraeceptaInputDto implements IDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8491947268670333385L;
	
	private Object inputData;

	public Object getInputData() {
		return inputData;
	}

	public void setInputData(Object inputData) {
		this.inputData = inputData;
	}
	
}

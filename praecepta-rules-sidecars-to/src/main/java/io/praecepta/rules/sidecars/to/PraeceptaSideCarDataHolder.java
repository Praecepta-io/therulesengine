package io.praecepta.rules.sidecars.to;

import java.util.Map;

public class PraeceptaSideCarDataHolder {

	private Object sideCarInput;
	
	private Object sideCarOuput;
	
	private Map<String, Object> additionalHolderInfo;
	
	public <I> void addInput(I input) {
		this.sideCarInput = input;
	}
	
	public <O> void addOutput(O output) {
		this.sideCarOuput = output;
	}
	
	public <I> I retriveInput(Class<I> inputTypeClass) {
		return (I)sideCarInput;
	}
	
	public <O> O retriveOutput(Class<O> outputTypeClass) {
		return (O)sideCarOuput;
	}

	public Map<String, Object> getAdditionalHolderInfo() {
		return additionalHolderInfo;
	}

	public void setAdditionalHolderInfo(Map<String, Object> additionalHolderInfo) {
		this.additionalHolderInfo = additionalHolderInfo;
	}
	
}

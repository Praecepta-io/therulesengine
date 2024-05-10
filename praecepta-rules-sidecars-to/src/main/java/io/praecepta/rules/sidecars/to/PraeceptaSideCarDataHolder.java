package io.praecepta.rules.sidecars.to;

import java.util.Map;

public class PraeceptaSideCarDataHolder<In_Type, Out_Type> {

	private In_Type sideCarInput;
	
	private Out_Type sideCarOuput;
	
	private Map<String, Object> additionalHolderInfo;
	
	private PraeceptaSideCarDataHolder<?,?> parentSideCarDataHolder;
	
	private PraeceptaSideCarDataHolder<?,?> nextSideCarDataHolder;
	
	public PraeceptaSideCarDataHolder() {
	}
	
	public PraeceptaSideCarDataHolder(PraeceptaSideCarDataHolder<?,?> parentSideCarDataHolder) {
		this.parentSideCarDataHolder = parentSideCarDataHolder;
	}
	
	public PraeceptaSideCarDataHolder(PraeceptaSideCarDataHolder<?,?> parentSideCarDataHolder, PraeceptaSideCarDataHolder<?,?> nextSideCarDataHolder) {
		this.parentSideCarDataHolder = parentSideCarDataHolder;
		this.nextSideCarDataHolder = nextSideCarDataHolder;
	}
	
	public void addInput(In_Type input) {
		this.sideCarInput = input;
	}
	
	public void addOutput(Out_Type output) {
		this.sideCarOuput = output;
	}
	
	public In_Type retriveInput() {
		return sideCarInput;
	}
	
	public Out_Type retriveOutput() {
		return sideCarOuput;
	}

	public Map<String, Object> getAdditionalHolderInfo() {
		return additionalHolderInfo;
	}

	public void setAdditionalHolderInfo(Map<String, Object> additionalHolderInfo) {
		this.additionalHolderInfo = additionalHolderInfo;
	}

	public PraeceptaSideCarDataHolder<?, ?> getParentSideCarDataHolder() {
		return parentSideCarDataHolder;
	}

	public void setParentSideCarDataHolder(PraeceptaSideCarDataHolder<?, ?> parentSideCarDataHolder) {
		this.parentSideCarDataHolder = parentSideCarDataHolder;
	}

	public PraeceptaSideCarDataHolder<?, ?> getNextSideCarDataHolder() {
		return nextSideCarDataHolder;
	}

	public void setNextSideCarDataHolder(PraeceptaSideCarDataHolder<?, ?> nextSideCarDataHolder) {
		this.nextSideCarDataHolder = nextSideCarDataHolder;
	}

	@Override
	public String toString() {
		String parentSideInput = parentSideCarDataHolder != null ? ", parentSideCarDataHolder Input="+ parentSideCarDataHolder.retriveInput() : "";
		String parentSideOutput = parentSideCarDataHolder != null ? ", parentSideCarDataHolder Output="+ parentSideCarDataHolder.retriveOutput() : "";
		String nextSideInput = nextSideCarDataHolder != null ? ", nextSideCarDataHolder Input="+ nextSideCarDataHolder.retriveInput() : "";
		String nextSideOutput = nextSideCarDataHolder != null ? ", nextSideCarDataHolder Output="+ nextSideCarDataHolder.retriveOutput() : "";
		
		return "PraeceptaSideCarDataHolder [sideCarInput=" + sideCarInput + ", sideCarOuput=" + sideCarOuput
				+ ", additionalHolderInfo=" + additionalHolderInfo 
				+ parentSideInput 
				+ parentSideOutput
				+ nextSideInput
				+ nextSideOutput; 
	}
	
}

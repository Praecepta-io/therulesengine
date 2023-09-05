package io.praecepta.rules.model;

import java.util.Map;

public class PraeceptaConditionResult {

	private CONDITION_RESULT result;
	
	private String resultMessage;
	
	private Map<String, Object> additionalResultInfo; 
	
	public enum CONDITION_RESULT {
		SATISFIED, NOT_SATISFIED;
	}

	public CONDITION_RESULT getResult() {
		return result;
	}

	public void setResult(CONDITION_RESULT result) {
		this.result = result;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public Map<String, Object> getAdditionalResultInfo() {
		return additionalResultInfo;
	}

	public void setAdditionalResultInfo(Map<String, Object> additionalResultInfo) {
		this.additionalResultInfo = additionalResultInfo;
	}

	@Override
	public String toString() {
		return "PraeceptaConditionResult [result=" + result + ", resultMessage=" + resultMessage
				+ ", additionalResultInfo=" + additionalResultInfo + "]";
	}
	
}

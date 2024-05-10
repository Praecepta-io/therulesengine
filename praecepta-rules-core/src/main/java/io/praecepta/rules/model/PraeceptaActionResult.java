package io.praecepta.rules.model;

import java.util.HashMap;
import java.util.Map;

public class PraeceptaActionResult {

	private ACTION_EXECUTION_STATUS actionResultStatus;
	
	private String actionResultStatusMessage;
	
	private Map<String, Object> additionalActionResultInfo; 
	
	public enum ACTION_EXECUTION_STATUS {
		SUCCESS, FAILURE, UNABALE_TO_PERFORM_ACTION;
	}

	public ACTION_EXECUTION_STATUS getActionResultStatus() {
		return actionResultStatus;
	}

	public void setActionResultStatus(ACTION_EXECUTION_STATUS actionResultStatus) {
		this.actionResultStatus = actionResultStatus;
	}

	public String getActionResultStatusMessage() {
		return actionResultStatusMessage;
	}

	public void setActionResultStatusMessage(String actionResultStatusMessage) {
		this.actionResultStatusMessage = actionResultStatusMessage;
	}

	public Map<String, Object> getAdditionalActionResultInfo() {
		
		if(additionalActionResultInfo == null) {
			additionalActionResultInfo = new HashMap<String, Object>();
		}
		
		return additionalActionResultInfo;
	}

	public void setAdditionalActionResultInfo(Map<String, Object> additionalActionResultInfo) {
		this.additionalActionResultInfo = additionalActionResultInfo;
	}

	@Override
	public String toString() {
		return "PraeceptaActionResult [actionResultStatus=" + actionResultStatus + ", actionResultStatusMessage="
				+ actionResultStatusMessage + ", additionalActionResultInfo=" + additionalActionResultInfo + "]";
	}

}

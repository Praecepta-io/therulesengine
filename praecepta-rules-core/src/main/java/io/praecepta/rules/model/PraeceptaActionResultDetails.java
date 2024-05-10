package io.praecepta.rules.model;

public class PraeceptaActionResultDetails {

	private String actionName;
	
	private PraeceptaActionResult actionResult;
	
	private Object actionedValue;
	
	private String actionAttributeName;
	
	public PraeceptaActionResultDetails(String actionName, PraeceptaActionResult actionResult, String actionAttributeName, Object actionedValue) {
		this.actionName = actionName;
		this.actionResult = actionResult;
		this.actionAttributeName = actionAttributeName;
		this.actionedValue = actionedValue;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public PraeceptaActionResult getActionResult() {
		return actionResult;
	}

	public void setActionResult(PraeceptaActionResult actionResult) {
		this.actionResult = actionResult;
	}

	public Object getActionedValue() {
		return actionedValue;
	}

	public void setActionedValue(Object actionedValue) {
		this.actionedValue = actionedValue;
	}
	
	public String getActionAttributeName() {
		return actionAttributeName;
	}

	public void setActionAttributeName(String actionAttributeName) {
		this.actionAttributeName = actionAttributeName;
	}

	@Override
	public String toString() {
		return "PraeceptaActionResultDetails [actionName=" + actionName + ", actionResult=" + actionResult
				+ ", actionedValue=" + actionedValue + ", actionAttributeName=" + actionAttributeName + "]";
	}
	
}

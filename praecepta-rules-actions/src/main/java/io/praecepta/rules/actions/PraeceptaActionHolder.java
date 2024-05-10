package io.praecepta.rules.actions;

import io.praecepta.rules.model.PraeceptaActionResult;

public class PraeceptaActionHolder {
	
	private PraeceptaActionResult actionResult;
	
	private Object actionedValue;
	
	private String actionAttributeName;

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
		return "PraeceptaActionHolder [actionResult=" + actionResult + ", actionedValue=" + actionedValue
				+ ", actionAttributeName=" + actionAttributeName + "]";
	}
	
}

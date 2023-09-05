package io.praecepta.rules.actions;

import io.praecepta.rules.model.PraeceptaActionResult;

public class PraeceptaActionHolder {
	
	private PraeceptaActionResult actionResult;
	
	private Object actionedValue;

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

}

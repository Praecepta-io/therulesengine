package io.praecepta.rules.model.projection;

/**
 * 
 * @author rajasrikar
 *
 */
public interface IPraeceptaAction<INPUT> {

	void  performAction(INPUT input);
	
	String getActionName();
	
	default boolean validate(INPUT input) {
		return true;
	}
}

package io.praecepta.rules.registry;

import java.util.HashMap;
import java.util.Map;

import io.praecepta.core.helper.ObjectHelper;
import io.praecepta.rules.model.projection.IPraeceptaAction;

/**
 * 
 * @author rajasrikar
 *
 */
public class PraeceptaActionRegistry {

	private static final Map<String, IPraeceptaAction<?>>  actionRegistry = new HashMap<>();
	
	public static void registerAnAction(String actionName, IPraeceptaAction<?> action) {
		
		if(ObjectHelper.isObjectEmpty(actionName) && ObjectHelper.isObjectEmpty(action)) {
			actionRegistry.put(actionName, action);
		}
	}
}

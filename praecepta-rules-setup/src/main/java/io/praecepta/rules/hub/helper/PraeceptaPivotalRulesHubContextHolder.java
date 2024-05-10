package io.praecepta.rules.hub.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import io.praecepta.rules.hub.IPraeceptaPivotalRulesHubManager;
import io.praecepta.rules.hub.datastore.PraeceptaPivotalRulesHubStore;

public class PraeceptaPivotalRulesHubContextHolder {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaPivotalRulesHubContextHolder.class);
	
	private  ApplicationContext context;
	
	private  IPraeceptaPivotalRulesHubManager hubManager;
	
	private PraeceptaPivotalRulesHubStore hubStore = new PraeceptaPivotalRulesHubStore(null, null, null);
	
	private static PraeceptaPivotalRulesHubContextHolder holder = getInstance();
	
	private PraeceptaPivotalRulesHubContextHolder() {
	}
	
	private static PraeceptaPivotalRulesHubContextHolder getInstance() {
		return new PraeceptaPivotalRulesHubContextHolder();
	}

	public static void addHubManager(IPraeceptaPivotalRulesHubManager hubManagerToAdd) {
		
		if(holder.hubManager == null) {
			holder.hubManager = hubManagerToAdd;
		}
	}
	
	public static void addHubContext(ApplicationContext hubContextToAdd) {
		
		if(holder.context == null) {
			holder.context = hubContextToAdd;
		}
	}
	
	public static void addHubStore(PraeceptaPivotalRulesHubStore hubStore) {
		
//		if(holder.hubStore == null) {
			holder.hubStore = hubStore;
//		}
	}
	
	public static ApplicationContext getHubContext() {
		return holder.context;
	}
	
	public static IPraeceptaPivotalRulesHubManager getHubManager() {
		return holder.hubManager;
	}

	public static PraeceptaPivotalRulesHubStore getHubStore() {
		return holder.hubStore;
	}
}

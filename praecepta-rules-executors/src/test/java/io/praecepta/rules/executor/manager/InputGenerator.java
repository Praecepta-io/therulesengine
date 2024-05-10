package io.praecepta.rules.executor.manager;

import java.util.HashMap;
import java.util.Map;

import io.praecepta.core.helper.GsonHelper;

public class InputGenerator {
	
    private static final String ACTIVE_SPACE_VERSION = "1.0";
    
    private static final String SPACE_NAME = "BRD";
    
    private static final String CLIENT_NAME = "GTO";
    
    private static final String APP_NAME = "ACATS";
    
    private static final String ACATS_TRANSFER_STOP_GRP = "TRANSFER_STOP";
	
	public static void main(String[] args) {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		dataMap.put("spaceName", SPACE_NAME);
		dataMap.put("clientId", CLIENT_NAME);
		dataMap.put("appName", APP_NAME);
		dataMap.put("version", ACTIVE_SPACE_VERSION);
		dataMap.put("ruleGroupName",ACATS_TRANSFER_STOP_GRP);
		
		Map<String, Object> employeeDataMap = new HashMap<String, Object>();
		
		employeeDataMap.put("name", "Vara");
		employeeDataMap.put("dob", "1999-01-01");
		
		dataMap.put("emp",employeeDataMap);
		
		System.out.println(GsonHelper.toJson(dataMap));
	}

}

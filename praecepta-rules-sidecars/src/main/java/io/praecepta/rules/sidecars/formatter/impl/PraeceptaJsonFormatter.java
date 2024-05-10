package io.praecepta.rules.sidecars.formatter.impl;

import java.util.Map;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;

public class PraeceptaJsonFormatter extends PraeceptaGenericFormatter{

	@Override
	protected String getTemplate(Map<String, Object> sideCarConfigs) {
		
		String templateStringToReturn = "";
		
		Map<String, Object> jsonRemplateAsAMap = (Map<String, Object>) sideCarConfigs.get(FORMATTER_TEMPLATE);
		
		if(!PraeceptaObjectHelper.isObjectEmpty(jsonRemplateAsAMap)) {
			templateStringToReturn =  GsonHelper.toJson(jsonRemplateAsAMap);
		}
		
		System.out.println("JSON Template --> "+templateStringToReturn);
		
		return templateStringToReturn;
	}

}

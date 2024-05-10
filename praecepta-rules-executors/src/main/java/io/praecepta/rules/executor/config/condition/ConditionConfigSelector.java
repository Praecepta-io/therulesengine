package io.praecepta.rules.executor.config.condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.executor.config.enums.DATA_COLLECTOR_TYPE;

public abstract class ConditionConfigSelector implements Condition {

	private final static Logger logger = LoggerFactory.getLogger(ConditionConfigSelector.class);
	
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		logger.debug("Inside the Match method of ConditionConfigSelector ");
		
		String dataCollectorType = context.getEnvironment().getProperty(DATA_COLLECTOR_TYPE.class.getSimpleName());
		
		logger.info(" Checking the Selector for Data Collector Type {} ", dataCollectorType);
		
		return checkCollectorType(dataCollectorType);
	}
	
	protected abstract DATA_COLLECTOR_TYPE getCollectorType();

	private boolean checkCollectorType(String dataCollectorType) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(dataCollectorType) ) {
			DATA_COLLECTOR_TYPE type = DATA_COLLECTOR_TYPE.valueOf(dataCollectorType);
			
			logger.info(" Comparing getCollectorType {} and DATA_COLLECTOR_TYPE of dataCollectorType {} ", getCollectorType(), type);
			
			if(getCollectorType() == type) {
				logger.info(" Seletor Matched for Collector Type ");
				return true;
			}
		}
		
		return false;
	}
}

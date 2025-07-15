package io.praecepta.rules.hub.config.condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_SUB_TYPE;
import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_TYPE;

public abstract class ConditionConfigSelector implements Condition {

	private final static Logger logger = LoggerFactory.getLogger(ConditionConfigSelector.class);
	
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		logger.debug("Inside the Match method of ConditionConfigSelector ");
		
		String rulePersistantType = context.getEnvironment().getProperty(RULE_SET_UP_PERSISTANT_TYPE.class.getSimpleName());
		
		logger.info(" Checking the Selector for Persistant Type {} ", rulePersistantType);
		
//		return checkPersistantType(rulePersistantType) && checkPersistantSubType(rulePersistantSubType);
		return checkPersistantType(rulePersistantType) && additionalCheck(context, metadata);
	}
	
	protected abstract RULE_SET_UP_PERSISTANT_TYPE getPersistantType();

	protected abstract RULE_SET_UP_PERSISTANT_SUB_TYPE getPersistantSubType();
	
	private boolean checkPersistantType(String rulePersistantType) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(rulePersistantType) ) {
			RULE_SET_UP_PERSISTANT_TYPE type = RULE_SET_UP_PERSISTANT_TYPE.valueOf(rulePersistantType);
			
			logger.info(" Comparing getPersistentType {} and RULE_SET_UP_PERSISTANT_TYPE of rulePersistentType {} ", getPersistantType(), type);
			
			if(getPersistantType() == type) {
				logger.info(" Selector Matched for Persistent Type ");
				return true;
			}
		}
		
		return false;
	}
	
	protected boolean additionalCheck(ConditionContext context, AnnotatedTypeMetadata metadata) {
		
		if(isSubTypeCheckNeeded()) {
			logger.info(" SubType Check is needed ");
			
			String rulePersistantSubType = context.getEnvironment().getProperty(RULE_SET_UP_PERSISTANT_SUB_TYPE.class.getSimpleName());
			
			logger.info(" SubType Passed {} ", rulePersistantSubType);
			
			return checkPersistantSubType(rulePersistantSubType);
		}
		return true;	
	}
	
	private boolean checkPersistantSubType(String rulePersistantSubType) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(rulePersistantSubType)) {
			RULE_SET_UP_PERSISTANT_SUB_TYPE subType = RULE_SET_UP_PERSISTANT_SUB_TYPE.valueOf(rulePersistantSubType);
			
			logger.info(" Ccomparing getPersistantSubType {} and RULE_SET_UP_PERSISTANT_SUB_TYPE of rulePersistantSubType {} "
					, getPersistantSubType(), subType);
			
			if(getPersistantSubType() == subType) {
				logger.info(" Seletor Matched for Sub Persistant Type ");
				return true;
			}
		}
		
		return false;
	}

	public abstract boolean isSubTypeCheckNeeded();

}

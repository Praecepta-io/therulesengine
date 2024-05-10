package io.praecepta.rules.hub.spring.file.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.builders.enums.RULE_BUILDER_CONFIG_KEYS;
import io.praecepta.rules.hub.config.condition.file.WindowsFileConditionConfigSelector;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSideCarsDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleGroupWindowsDbDao;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleSpaceWindowsDbDao;
import io.praecepta.rules.hub.filebased.dao.PraeceptaSidecarWindowsDbDao;

@Configuration
@Conditional(WindowsFileConditionConfigSelector.class)
public class PraeceptaRuleBuilderFileServiceConfig {

    @Autowired
    private Environment env;
    
    private static final String defaultWindowsFolder = "C:\\Users\\Public";
    private static final String defaultMetaDataFileNameSuffix = "praeceptaMetaData.json";
    
    @Bean(name = "ruleSpaceDao")
    public IPraeceptaRuleSpaceDao getRuleSpaceDao() {
        String folderName = getFolderName();
        String metaDataFileNameSuffix = getMetaDataFileNameSuffix();
        return new PraeceptaRuleSpaceWindowsDbDao(folderName, metaDataFileNameSuffix);
    }

	private String getMetaDataFileNameSuffix() {
		String metaDataFileNameSuffix =   env.getProperty(getConnectionPrefix()+"file_name",defaultMetaDataFileNameSuffix);
		if(!PraeceptaObjectHelper.isObjectEmpty(metaDataFileNameSuffix)) return metaDataFileNameSuffix;
		else return defaultMetaDataFileNameSuffix;
	}

	private String getFolderName() {
		String folderName =  env.getProperty(getConnectionPrefix()+"folder_name",defaultWindowsFolder);
		if(!PraeceptaObjectHelper.isObjectEmpty(folderName)) return folderName;
		else return defaultWindowsFolder;
	}

    @Bean(name = "ruleGroupDao")
    public IPraeceptaRuleGroupDao getRuleGroupDao() {
    	String folderName = getFolderName();
        String metaDataFileNameSuffix = getMetaDataFileNameSuffix();
        return new PraeceptaRuleGroupWindowsDbDao(folderName, metaDataFileNameSuffix);
    }

    @Bean(name = "sidecarDao")
    public IPraeceptaRuleSideCarsDao getSidecarDao() {
        String folderName = getFolderName();
        String metaDataFileNameSuffix = getMetaDataFileNameSuffix();
        return new PraeceptaSidecarWindowsDbDao(folderName, metaDataFileNameSuffix);
    }

    private String getConnectionPrefix() {
        return System.getProperty(RULE_BUILDER_CONFIG_KEYS.RULE_CONNECTIOS_PREFIX.value)+".";
    }
}

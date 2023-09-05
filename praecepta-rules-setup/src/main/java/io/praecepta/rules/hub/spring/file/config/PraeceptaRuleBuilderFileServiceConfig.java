package io.praecepta.rules.hub.spring.file.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.praecepta.rules.hub.config.condition.file.WindowsFileConditionConfigSelector;
import io.praecepta.rules.hub.dao.IPraeceptaRuleGroupDao;
import io.praecepta.rules.hub.dao.IPraeceptaRuleSpaceDao;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleGroupWindowsDbDao;
import io.praecepta.rules.hub.filebased.dao.PraeceptaRuleSpaceWindowsDbDao;

@Configuration
@Conditional(WindowsFileConditionConfigSelector.class)
public class PraeceptaRuleBuilderFileServiceConfig {

    @Autowired
    private Environment env;
    
    private static final String defaultWindowsFolder = "C:\\Users\\Public";
    private static final String defaultWindowsSpaceFileName = "PraeceptaRuleSpace";
    private static final String defaultWindowsGrpFileName = "PraeceptaRuleGrp";
    
    @Bean(name = "ruleSpaceDao")
    public IPraeceptaRuleSpaceDao getRuleSpaceDao() {
        String folderName =  env.getProperty("folder_name",defaultWindowsFolder);
        String spaceFileNamePrefix =   env.getProperty("file_name",defaultWindowsSpaceFileName);
        return new PraeceptaRuleSpaceWindowsDbDao(folderName, spaceFileNamePrefix);
    }

    @Bean(name = "ruleGroupDao")
    public IPraeceptaRuleGroupDao getRuleGroupDao() {
    	String folderName =  env.getProperty("folder_name",defaultWindowsFolder);
        String grpFileNamePrefix =   env.getProperty("file_name",defaultWindowsGrpFileName);
        return new PraeceptaRuleGroupWindowsDbDao(folderName, grpFileNamePrefix);
    }
}

package io.praecepta.rest.api;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestService;
import io.praecepta.rest.api.builder.PraeceptaSparkApiServiceBuilder;
import io.praecepta.rest.api.model.PraeceptaRestServiceInterceptorModel;
import io.praecepta.rules.builders.PraeceptaRuleBuilder;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;
import spark.Filter;
/**
 * RuleGroup Api Launcher
 *
 */
public class PraeceptaRuleGroupApiLauncher
{
	
	public static final String APP_PACKAGE = "io.praecepta.rest.api";
    public static void main( String[] args ) throws JsonProcessingException
    {
    	
    	System.setProperty("contextPath", "io.praecepta.rest.api.config.RulesConfiguration");

    	PraeceptaRuleBuilder.buildWithEnvParmPropsFile().buildAll();

		ApplicationContext context = PraeceptaPivotalRulesHubContextHolder.getHubContext();

		Map<String,Object> beans = context.getBeansWithAnnotation(PraeceptaExposeAsRestService.class);

		Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPreInterceptors = (Map<PraeceptaRestServiceInterceptorModel, List<Filter>>) context.getBean("restPreInterceptors");

		Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPostInterceptors = (Map<PraeceptaRestServiceInterceptorModel, List<Filter>>) context.getBean("restPostInterceptors");

    	PraeceptaSparkApiServiceBuilder.buildServices(beans, restPreInterceptors, restPostInterceptors);
    	
    }
}

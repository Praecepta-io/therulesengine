package io.praecepta.parser.execution.api;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestService;
import io.praecepta.rest.api.builder.PraeceptaSparkApiServiceBuilder;
import io.praecepta.rest.api.model.PraeceptaRestServiceInterceptorModel;
import io.praecepta.rules.builders.PraeceptaRuleBuilder;
import io.praecepta.rules.hub.helper.PraeceptaPivotalRulesHubContextHolder;

import spark.Filter;

public class ParserExecutionApiLauncher {

	public static void main(String[] args) {
		

		System.setProperty("contextPath", "io.praecepta.parser.execution.config.ParserConfiguration");

    	PraeceptaRuleBuilder.buildWithDefaultProps().build(null, null, null, null);



		ApplicationContext context = PraeceptaPivotalRulesHubContextHolder.getHubContext();

		Map<String,Object> beans = context.getBeansWithAnnotation(PraeceptaExposeAsRestService.class);

		Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPreInterceptors = (Map<PraeceptaRestServiceInterceptorModel, List<Filter>>) context.getBean("restPreInterceptors");

		Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPostInterceptors = (Map<PraeceptaRestServiceInterceptorModel, List<Filter>>) context.getBean("restPostInterceptors");

		System.out.println(beans.size());


    	PraeceptaSparkApiServiceBuilder.buildServices(beans, restPreInterceptors, restPostInterceptors);
    	
    	
	}

}

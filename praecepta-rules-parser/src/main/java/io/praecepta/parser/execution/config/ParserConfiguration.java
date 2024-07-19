package io.praecepta.parser.execution.config;

import static io.praecepta.rest.api.interceptor.PraeceptaRestServiceInterceptorRegistry.addPreInterceptors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.praecepta.parser.execution.constants.ServiceAndMethodNames;
import io.praecepta.parser.execution.controller.IPraeceptaSQLParserExecutionController;
import io.praecepta.parser.execution.controller.PraeceptaParserExecutionController;
import io.praecepta.parser.execution.service.IPraeceptaParserExecutionService;
import io.praecepta.parser.execution.service.impl.PraeceptaParserExecutionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import io.praecepta.rest.api.interceptor.PraeceptaRestServiceInterceptorRegistry;
import io.praecepta.rest.api.model.PraeceptaRestServiceInterceptorModel;
import io.praecepta.rest.api.service.IPraeceptaRulesGroupService;
import io.praecepta.rest.api.service.IPraeceptaSidecarService;
import io.praecepta.rest.api.service.impl.PraeceptaRuleGroupServiceImpl;
import io.praecepta.rest.api.service.impl.PraeceptaSidecarService;
import io.praecepta.rules.hub.spring.config.PraeceptaRuleBuilderConfig;

import spark.Filter;
import spark.route.HttpMethod;

@Configuration
@Import({ PraeceptaRuleBuilderConfig.class })
public class ParserConfiguration {

	@Bean(name = ServiceAndMethodNames.PARSER_EXECUTION_CONTROLLER_NAME)
	public IPraeceptaSQLParserExecutionController getParserExecutionController() {
		return new PraeceptaParserExecutionController();
	}

	@Bean(name = "parserExecutionService")
	public IPraeceptaParserExecutionService getParserExecutionService() {
		return new PraeceptaParserExecutionService();
	}

	@Bean(name = "rulesGroupService")
	public IPraeceptaRulesGroupService getRuleGroupService() {
		return new PraeceptaRuleGroupServiceImpl();
	}
	
	@Bean(name ="sidecarService")
	public IPraeceptaSidecarService getSidecarService() {
		return new PraeceptaSidecarService();
	}
	

	@Bean(name = "restPreInterceptors")
	public Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPreInterceptors(){
		
		// Class Level Pre Interceptors for testController
		PraeceptaRestServiceInterceptorModel controlLevelmodel = 
				new PraeceptaRestServiceInterceptorModel(ServiceAndMethodNames.PARSER_EXECUTION_CONTROLLER_NAME);
		
		List<Filter> rulePreControllerLeverlFilters = new ArrayList<>();

		addPreInterceptors(controlLevelmodel, rulePreControllerLeverlFilters );
		
		// Method Level Pre Interceptors for testController
		PraeceptaRestServiceInterceptorModel rulePreGetMethodLevelModel = 
				new PraeceptaRestServiceInterceptorModel(ServiceAndMethodNames.PARSER_EXECUTION_CONTROLLER_NAME, ServiceAndMethodNames.PARSER_EXECUTOR_METHOD_NAME, HttpMethod.post);
		
		
		List<Filter> rulePreMethodLeverlFilters = new ArrayList<>();
		
		rulePreMethodLeverlFilters.add( (req, res) -> {
			res.header("methodFilterTriggered", "Yes");
		});
		
		addPreInterceptors(rulePreGetMethodLevelModel, rulePreMethodLeverlFilters );
		
		
		return PraeceptaRestServiceInterceptorRegistry.getPreInterceptors();
	}
	
	@Bean(name = "restPostInterceptors")
	public Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPostInterceptors(){
		
		return PraeceptaRestServiceInterceptorRegistry.getPostInterceptors();
	}
	
	private Filter corseFilter() {
		return (req, res) -> {
			res.header("Access-Control-Allow-Headers", "*");
			res.header("Access-Control-Allow-Origin", "*");
			res.header("Access-Control-Allow-Methods", "*");
			res.header("Access-Control-Request-Method", "*");
		};
	}

}

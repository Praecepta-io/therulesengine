package io.praecepta.rest.config;

import static io.praecepta.rest.api.interceptor.PraeceptaRestServiceInterceptorRegistry.addPreInterceptors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.praecepta.rest.api.controller.IPraeceptaRuleExecutionController;
import io.praecepta.rest.api.controller.PraeceptaRuleExecutionController;
import io.praecepta.rest.api.interceptor.PraeceptaRestServiceInterceptorRegistry;
import io.praecepta.rest.api.model.PraeceptaRestServiceInterceptorModel;
import io.praecepta.rest.constants.ServiceAndMethodNames;
import io.praecepta.rules.hub.spring.config.PraeceptaRuleBuilderConfig;
import org.springframework.context.annotation.PropertySource;
import spark.Filter;
import spark.route.HttpMethod;

@Configuration
@Import({PraeceptaRuleBuilderConfig.class})
@PropertySource("classpath:praecepta-${envTarget:local}.properties")
public class PraeceptaRuleExecutorConfiguration {
	
	@Bean(name = ServiceAndMethodNames.RULE_EXECUTION_CONTROLLER_NAME)
	public IPraeceptaRuleExecutionController getRuleGroupController() {

		return new PraeceptaRuleExecutionController();
	}


	@Bean(name = "restPreInterceptors")
	public Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPreInterceptors(){

		// Class Level Pre Interceptors for testController
		PraeceptaRestServiceInterceptorModel controlLevelmodel =
				new PraeceptaRestServiceInterceptorModel(ServiceAndMethodNames.RULE_EXECUTION_CONTROLLER_NAME);

		List<Filter> rulePreControllerLeverlFilters = new ArrayList<>();

	//	rulePreControllerLeverlFilters.add( corseFilter());

		addPreInterceptors(controlLevelmodel, rulePreControllerLeverlFilters );

		// Method Level Pre Interceptors for testController
		PraeceptaRestServiceInterceptorModel rulePreGetMethodLevelModel =
				new PraeceptaRestServiceInterceptorModel(ServiceAndMethodNames.RULE_EXECUTION_CONTROLLER_NAME, ServiceAndMethodNames.RULE_EXECUTOR_METHOD_NAME, HttpMethod.post);


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
		//	res.header("Access-Control-Allow-Origin", "*");
			res.header("Access-Control-Allow-Methods", "*");
			res.header("Access-Control-Request-Method", "*");
		};
	}
	

}

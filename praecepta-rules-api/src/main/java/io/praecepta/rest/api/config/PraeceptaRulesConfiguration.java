package io.praecepta.rest.api.config;

import static io.praecepta.rest.api.interceptor.PraeceptaRestServiceInterceptorRegistry.addPreInterceptors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.praecepta.rest.api.controller.IPraeceptaRuleGroupController;
import io.praecepta.rest.api.controller.IPraeceptaRuleSpaceController;
import io.praecepta.rest.api.controller.IPraeceptaSidecarController;
import io.praecepta.rest.api.controller.PraeceptaRuleGroupController;
import io.praecepta.rest.api.controller.PraeceptaRuleSpaceController;
import io.praecepta.rest.api.controller.PraeceptaSidecarController;
import io.praecepta.rest.api.interceptor.PraeceptaRestServiceInterceptorRegistry;
import io.praecepta.rest.api.model.PraeceptaRestServiceInterceptorModel;
import io.praecepta.rest.api.service.IPraeceptaRulesGroupService;
import io.praecepta.rest.api.service.IPraeceptaSidecarService;
import io.praecepta.rest.api.service.impl.PraeceptaRuleGroupServiceImpl;
import io.praecepta.rest.api.service.impl.PraeceptaSidecarService;
import io.praecepta.rest.constants.ServiceAndMethodNames;
import io.praecepta.rules.hub.spring.config.PraeceptaRuleBuilderConfig;
import org.springframework.context.annotation.PropertySource;
import spark.Filter;
import spark.route.HttpMethod;

@Configuration
@Import({PraeceptaRuleBuilderConfig.class})
@PropertySource("classpath:praecepta-${envTarget:local}.properties")
public class PraeceptaRulesConfiguration {
	
	@Bean(name = ServiceAndMethodNames.RULE_GROUP_CONTROLLER_NAME)
	public IPraeceptaRuleGroupController getRuleGroupController() {
		return new PraeceptaRuleGroupController();
	}

	@Bean(name = ServiceAndMethodNames.SIDE_CAR__CONTROLLER_NAME)
	public IPraeceptaSidecarController getSideCarController() {
		return new PraeceptaSidecarController();
	}
	@Bean(name ="rulesGroupService")
	public IPraeceptaRulesGroupService getRuleGroupService() {
		return new PraeceptaRuleGroupServiceImpl();
	}

	@Bean(name ="sidecarService")
	public IPraeceptaSidecarService getSidecarService() {
		return new PraeceptaSidecarService();
	}

	@Bean(name = ServiceAndMethodNames.RULE_SPACE_CONTROLLER_NAME)
	public IPraeceptaRuleSpaceController getRuleSpaceController() {
		return new PraeceptaRuleSpaceController();
	}
	
	
	@Bean(name = "restPreInterceptors")
	public Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPreInterceptors(){
		
		// Class Level Pre Interceptors for testController
		PraeceptaRestServiceInterceptorModel controlLevelmodel = 
				new PraeceptaRestServiceInterceptorModel(ServiceAndMethodNames.RULE_GROUP_CONTROLLER_NAME);
		
		List<Filter> rulePreControllerLeverlFilters = new ArrayList<>();
		
	//	rulePreControllerLeverlFilters.add( corseFilter());
		
		addPreInterceptors(controlLevelmodel, rulePreControllerLeverlFilters );
		
		// Method Level Pre Interceptors for testController
		PraeceptaRestServiceInterceptorModel rulePreGetMethodLevelModel = 
				new PraeceptaRestServiceInterceptorModel(ServiceAndMethodNames.RULE_GROUP_CONTROLLER_NAME, ServiceAndMethodNames.RULE_GROUP_NAMES_GET_METHOD_NAME, HttpMethod.get);
		
		
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

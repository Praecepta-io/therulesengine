package io.praecepta.rest.api.config;

import static io.praecepta.rest.api.interceptor.PraeceptaRestServiceInterceptorRegistry.addPreInterceptors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

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

@Configuration
@Import({PraeceptaRuleBuilderConfig.class})
public class RulesConfiguration {
	
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
	public Map<PraeceptaRestServiceInterceptorModel, List<HandlerInterceptor>> restPreInterceptors(){
		
		// Class Level Pre Interceptors for ruleGroupController
		PraeceptaRestServiceInterceptorModel controlLevelmodel = 
				new PraeceptaRestServiceInterceptorModel(ServiceAndMethodNames.RULE_GROUP_CONTROLLER_NAME);
		
		List<HandlerInterceptor> rulePreControllerLeverlFilters = new ArrayList<>();
		
		addPreInterceptors(controlLevelmodel, rulePreControllerLeverlFilters );
		
		// Method Level Pre Interceptors for ruleGroupController
		PraeceptaRestServiceInterceptorModel rulePreGetMethodLevelModel = 
				new PraeceptaRestServiceInterceptorModel(ServiceAndMethodNames.RULE_GROUP_CONTROLLER_NAME, ServiceAndMethodNames.RULE_GROUP_NAMES_GET_METHOD_NAME, RequestMethod.GET);
		
		
		List<HandlerInterceptor> rulePreMethodLeverlFilters = new ArrayList<>();
		
		rulePreMethodLeverlFilters.add(new HandlerInterceptor() {
			@Override
			public boolean preHandle(jakarta.servlet.http.HttpServletRequest request, 
					jakarta.servlet.http.HttpServletResponse response, Object handler) throws Exception {
				response.setHeader("methodFilterTriggered", "Yes");
				return true;
			}
		});
		
		addPreInterceptors(rulePreGetMethodLevelModel, rulePreMethodLeverlFilters );
		
		
		return PraeceptaRestServiceInterceptorRegistry.getPreInterceptors();
	}
	
	@Bean(name = "restPostInterceptors")
	public Map<PraeceptaRestServiceInterceptorModel, List<HandlerInterceptor>> restPostInterceptors(){
		
		return PraeceptaRestServiceInterceptorRegistry.getPostInterceptors();
	}

}

package io.praecepta.rest.api.config;

import static io.praecepta.rest.api.interceptor.PraeceptaRestServiceInterceptorRegistry.addPreInterceptors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.praecepta.rest.api.controller.TestController;
import io.praecepta.rest.api.interceptor.PraeceptaRestServiceInterceptorRegistry;
import io.praecepta.rest.api.model.PraeceptaRestServiceInterceptorModel;
import io.praecepta.rest.constants.TestServiceAndMethodNames;
import spark.Filter;
import spark.route.HttpMethod;

@Configuration
public class TestConfig {

	@Bean(name = TestServiceAndMethodNames.TEST_CONTROLLER_NAME)
	public TestController getController() {
		return new TestController();
	}
	
	@Bean(name = "restPreInterceptors")
	public Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPreInterceptors(){
		
		// Class Level Pre Interceptors for testController
		PraeceptaRestServiceInterceptorModel controlLevelmodel = 
				new PraeceptaRestServiceInterceptorModel(TestServiceAndMethodNames.TEST_CONTROLLER_NAME);
		
		List<Filter> testControllerLeverlFilters = new ArrayList<>();
		
//		testControllerLeverlFilters.add( corseFilter());
		
		addPreInterceptors(controlLevelmodel, testControllerLeverlFilters );
		
		// Method Level Pre Interceptors for testController
		PraeceptaRestServiceInterceptorModel testGetMethodLevelModel = 
				new PraeceptaRestServiceInterceptorModel(TestServiceAndMethodNames.TEST_CONTROLLER_NAME, TestServiceAndMethodNames.TEST_GET_METHOD_NAME, HttpMethod.get);
		
		List<Filter> testMethodLeverlFilters = new ArrayList<>();
		
		testMethodLeverlFilters.add( (req, res) -> {
			res.header("methodFilterTriggered", "Yes");
		});
		
		addPreInterceptors(testGetMethodLevelModel, testMethodLeverlFilters );
		
		return PraeceptaRestServiceInterceptorRegistry.getPreInterceptors();
	}
	
	@Bean(name = "restPostInterceptors")
	public Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPostInterceptors(){
		
		return PraeceptaRestServiceInterceptorRegistry.getPostInterceptors();
	}
	
	private static final String headerAllowHeaders = "Access-Control-Allow-Headers";
	private static final String headerAllowOrigin = "Access-Control-Allow-Origin";
	private static final String headerRequestMethod = "Access-Control-Request-Method";
	private static final String wildcard = "*";

	
	private Filter corseFilter() {
		return (req, res) -> {
		//	res.header("Access-Control-Allow-Headers", "*");
			res.header(headerAllowOrigin, wildcard);
			res.header(headerRequestMethod, wildcard);
			res.header(headerAllowHeaders, wildcard);
		};
	}
	
}

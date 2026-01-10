package io.praecepta.rest.api.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.HandlerInterceptor;

import io.praecepta.rest.api.model.PraeceptaRestServiceInterceptorModel;

public class PraeceptaRestServiceInterceptorRegistry {
	
	private static Map<PraeceptaRestServiceInterceptorModel, List<HandlerInterceptor>> preInterceptors = new HashMap<>();
	
	private static Map<PraeceptaRestServiceInterceptorModel, List<HandlerInterceptor>> postInterceptors = new HashMap<>() ;
	
	public static void addPreInterceptors(PraeceptaRestServiceInterceptorModel model, List<HandlerInterceptor> interceptors){
		preInterceptors.put(model, interceptors);
	}
	
	public static Map<PraeceptaRestServiceInterceptorModel, List<HandlerInterceptor>> getPreInterceptors(){
		return preInterceptors;
	}

	public static void addPostInterceptors(PraeceptaRestServiceInterceptorModel model, List<HandlerInterceptor> interceptors){
		postInterceptors.put(model, interceptors);
	}
	
	public static Map<PraeceptaRestServiceInterceptorModel, List<HandlerInterceptor>> getPostInterceptors(){
		return postInterceptors;
	}
	
}

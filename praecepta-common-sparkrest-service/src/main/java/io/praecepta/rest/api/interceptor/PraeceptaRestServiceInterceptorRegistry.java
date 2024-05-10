package io.praecepta.rest.api.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.praecepta.rest.api.model.PraeceptaRestServiceInterceptorModel;
import spark.Filter;

public class PraeceptaRestServiceInterceptorRegistry {
	
	private static Map<PraeceptaRestServiceInterceptorModel, List<Filter>> preInterceptors = new HashMap<>();
	
	private static Map<PraeceptaRestServiceInterceptorModel, List<Filter>> postInterceptors = new HashMap<>() ;
	
	public static void addPreInterceptors(PraeceptaRestServiceInterceptorModel model, List<Filter> filters){
		preInterceptors.put(model, filters);
	}
	
	public static Map<PraeceptaRestServiceInterceptorModel, List<Filter>> getPreInterceptors(){
		return preInterceptors;
	}

	public static void addPostInterceptors(PraeceptaRestServiceInterceptorModel model, List<Filter> filters){
		postInterceptors.put(model, filters);
	}
	
	public static Map<PraeceptaRestServiceInterceptorModel, List<Filter>> getPostInterceptors(){
		return postInterceptors;
	}
	
}

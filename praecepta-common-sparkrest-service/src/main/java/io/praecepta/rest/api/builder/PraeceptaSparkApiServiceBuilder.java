package io.praecepta.rest.api.builder;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.path;
import static spark.Spark.port;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestService;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestServiceMethod;
import io.praecepta.rest.api.model.PraeceptaRestServiceInterceptorModel;
import io.praecepta.rest.api.service.IPraeceptaApiService;
import io.praecepta.rest.api.swagger.PraeceptaSwaggerGenerator;
import spark.Filter;

/**
 * 
 * @author rajasrikar
 *
 */
public class PraeceptaSparkApiServiceBuilder {
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaSparkApiServiceBuilder.class);
	
	private static final String SYSTEM_PARAM_NAME_FOR_ALTERNATE_PORT = "server.port";

	private static final String headerAllowHeaders = "Access-Control-Allow-Headers";
	private static final String headerAllowOrigin = "Access-Control-Allow-Origin";
	private static final String headerRequestMethod = "Access-Control-Request-Method";
	private static final String wildcard = "*";

	public static void buildServices(Map<String,Object> beans, Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPreInterceptors, 
			Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPostInterceptors) {
		
		final List<Method> getMethods = new ArrayList<Method>();
    	final List<Method> postMethods = new ArrayList<Method>();
    	final List<Method> putMethods = new ArrayList<Method>();
    	final List<Method> patchMethods = new ArrayList<Method>();
    	final List<Method> deleteMethods = new ArrayList<Method>();
    	
    	String portNumber = System.getProperty(SYSTEM_PARAM_NAME_FOR_ALTERNATE_PORT);
    	
    	if( portNumber != null) {
    		port(Integer.valueOf(portNumber));
    	}
    	
    	beans.forEach( (NAME, BEAN) -> {
    		
    		Class<?> beanClazz = BEAN.getClass();
    		
    		if(IPraeceptaApiService.class.isInstance(BEAN)) {
    		
	    		PraeceptaExposeAsRestService praeceptaExposeAsRestService = AnnotationUtils.findAnnotation(beanClazz, PraeceptaExposeAsRestService.class);
	    		
    			if(praeceptaExposeAsRestService != null) {
	    		
	    			String serviceName = praeceptaExposeAsRestService.serviceName();
	    					
	    			LOG.info(" Total Methods Size : {}" , beanClazz.getDeclaredMethods().length);
	    			
	    			
		    		
		    		for (final Method method : beanClazz.getDeclaredMethods()) {
		    			PraeceptaExposeAsRestServiceMethod methodLevelAnnotation =  AnnotationUtils.findAnnotation(method, PraeceptaExposeAsRestServiceMethod.class);
		                if (methodLevelAnnotation != null) {
		                	
		                	if(methodLevelAnnotation.get()) {
		                		getMethods.add(method);
		                	}
		                	
		                	if(methodLevelAnnotation.post()) {
		                		postMethods.add(method);
		                	}
		                	
		                	if(methodLevelAnnotation.patch()) {
		                		patchMethods.add(method);
		                	}
		                	
		                	if(methodLevelAnnotation.put()) {
		                		putMethods.add(method);
		                	}
		                	
		                	if(methodLevelAnnotation.delete()) {
		                		deleteMethods.add(method);
		                	}
		                	
		                }
		            }
		    		
		    		String parentRoute = praeceptaExposeAsRestService.servicePath();
		    		
		    		LOG.info(" Here is the Parent Route : {} for Service : {}",  parentRoute, serviceName);
		    		
		    		// Class Level Path Starts Here 
		    		path(parentRoute, () -> {
		    			
		    			options("/*",
		    			        (request, response) -> {

		    			            String accessControlRequestHeaders = request
		    			                    .headers("Access-Control-Request-Headers");
		    			            if (accessControlRequestHeaders != null) {
		    			                response.header("Access-Control-Allow-Headers",
		    			                        accessControlRequestHeaders);
		    			            }

		    			            String accessControlRequestMethod = request
		    			                    .headers("Access-Control-Request-Method");
		    			            if (accessControlRequestMethod != null) {
		    			                response.header("Access-Control-Allow-Methods",
		    			                        accessControlRequestMethod);
		    			            }

		    			            return "OK";
		    			        });


		    			PraeceptaRestServiceInterceptorModel serviceLevelModel = new PraeceptaRestServiceInterceptorModel(serviceName);
		    			
		    			// Adding Class Level Before Interceptors
		    			List<Filter> serviceLevelPreInterceptors = restPreInterceptors.get(serviceLevelModel);
		    			
		    			before("/*", (request, response) -> {
							LOG.info("Execute this Filter to All the Methods in this Class before calling any methods");
							response.header(headerAllowOrigin, wildcard);
							response.header(headerRequestMethod, wildcard);
							response.header(headerAllowHeaders, wildcard);

						});
		    			
		    			if(!PraeceptaObjectHelper.isObjectEmpty(serviceLevelPreInterceptors)) {
		    				serviceLevelPreInterceptors.stream().forEach( (EACH_SERVICE_LEVEL_PRE_FILTER) -> before("/*", EACH_SERVICE_LEVEL_PRE_FILTER));
		    			}
		    			
		    			PraeceptaSparkApiServiceMethodBuilder.addGetFunctions((IPraeceptaApiService)BEAN, serviceName, getMethods, restPreInterceptors, restPostInterceptors);
		    			
		    			PraeceptaSparkApiServiceMethodBuilder.addPostFunctions((IPraeceptaApiService)BEAN, serviceName, postMethods, restPreInterceptors, restPostInterceptors);
		    			
		    			PraeceptaSparkApiServiceMethodBuilder.addPutFunctions((IPraeceptaApiService)BEAN, serviceName, putMethods, restPreInterceptors, restPostInterceptors);
		    			
		    			PraeceptaSparkApiServiceMethodBuilder.addPatchFunctions((IPraeceptaApiService)BEAN, serviceName, patchMethods, restPreInterceptors, restPostInterceptors);
		    			
		    			PraeceptaSparkApiServiceMethodBuilder.addDeleteFunctions((IPraeceptaApiService)BEAN, serviceName, deleteMethods, restPreInterceptors, restPostInterceptors);
		    			
		    			// Adding Class Level After Interceptors
		    			List<Filter> serviceLevelPostInterceptors = restPostInterceptors.get(serviceLevelModel);
		    			
		    			after("/*", (request, response) -> LOG.info("Execute this Filter to All the Methods in this Class After calling any methods"));
		    		
		    			if(!PraeceptaObjectHelper.isObjectEmpty(serviceLevelPostInterceptors)) {
		    				serviceLevelPostInterceptors.stream().forEach( (EACH_SERVICE_LEVEL_POST_FILTER) -> after("/*", EACH_SERVICE_LEVEL_POST_FILTER));
		    			}
		    		});
		    		
		    		get("/swaggerJson"+parentRoute, (req, res) -> {
		    			String url = req.scheme() + "://"+  req.host();
		    			
		    			return PraeceptaSwaggerGenerator.generateSwagger(beanClazz, true, url);
		    		});
		    		get("/swaggerYaml"+parentRoute, (req, res) -> {
		    			String url = req.scheme() + "://"+  req.host();
		    			return PraeceptaSwaggerGenerator.generateSwagger(beanClazz, false, url);
		    		});
	    		
	    		} else {
	    			LOG.warn(" PraeceptaExposeAsRestService or Path Annotation Available to Expose it as a Service");
	    		}
    		}
    	});
    	
	}

	
}

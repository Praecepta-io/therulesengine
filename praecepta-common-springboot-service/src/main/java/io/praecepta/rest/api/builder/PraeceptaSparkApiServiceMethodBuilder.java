package io.praecepta.rest.api.builder;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.post;
import static spark.Spark.put;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestServiceMethod;
import io.praecepta.rest.api.model.PraeceptaRestServiceInterceptorModel;
import io.praecepta.rest.api.service.IPraeceptaApiService;
import io.praecepta.rest.enums.PraeceptaWsRequestStoreType;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;

/**
 * 
 * @author rajasrikar
 *
 */
public class PraeceptaSparkApiServiceMethodBuilder {
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaSparkApiServiceMethodBuilder.class);

	/**
	 * Adding Get Functions 
	 * @param serviceName
	 * @param getMethods
	 * @param restPreInterceptors
	 * @param restPostInterceptors
	 */
	public static void addGetFunctions(IPraeceptaApiService bean, String serviceName, List<Method> getMethods, Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPreInterceptors, Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPostInterceptors) {
		
		if(IPraeceptaApiService.class.isInstance(bean)) {
			LOG.info(" Inside Add Get method function. Get Methods Size : {}",getMethods.size());
			
			// Working on Get Methods in a Class
			getMethods.stream().forEach( EACH_GET_METHOD -> {
				PraeceptaExposeAsRestServiceMethod methodAnnotation =  AnnotationUtils.findAnnotation(EACH_GET_METHOD, PraeceptaExposeAsRestServiceMethod.class);
				
				String methodName = methodAnnotation.methodName();
				
				PraeceptaRestServiceInterceptorModel methodLevelModel = 
						new PraeceptaRestServiceInterceptorModel(serviceName, methodName, HttpMethod.get);
				
				// Adding Method Level Before Interceptors
				List<Filter> getMethodLevelPreInterceptors = restPreInterceptors.get( methodLevelModel );
				
				before(methodAnnotation.functionPath(), 
						(request, response) -> LOG.info("Execute Before Get Method"));
				
				if(!PraeceptaObjectHelper.isObjectEmpty(getMethodLevelPreInterceptors)) {
					getMethodLevelPreInterceptors.stream().forEach( 
							(EACH_GET_METHOD_LEVEL_PRE_FILTER) -> before( methodAnnotation.functionPath(), EACH_GET_METHOD_LEVEL_PRE_FILTER)
							);
				}
				
				// Adding Actual Get Function to Spark API
				get( methodAnnotation.functionPath(), methodAnnotation.produceType(), (Request request, Response response) -> {
					
					return buildAndRespond(bean, request, response, methodLevelModel);
				});
				
				// Adding Method Level After Interceptors
				List<Filter> getMethodLevelPostInterceptors = restPostInterceptors.get( methodLevelModel );
				
				after(methodAnnotation.functionPath(), 
						(request, response) -> LOG.info("Execute After Get Method"));
				
				if(!PraeceptaObjectHelper.isObjectEmpty(getMethodLevelPostInterceptors)) {
					getMethodLevelPostInterceptors.stream().forEach( 
							(EACH_GET_METHOD_LEVEL_POST_FILTER) -> after( methodAnnotation.functionPath(), EACH_GET_METHOD_LEVEL_POST_FILTER)
							);
				}
				
			});
		}
	}
	
	/**
	 * Adding Post Functions 
	 * @param serviceName
	 * @param getMethods
	 * @param restPreInterceptors
	 * @param restPostInterceptors
	 */
	public static void addPostFunctions(IPraeceptaApiService bean, String serviceName, List<Method> postMethods, Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPreInterceptors, Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPostInterceptors) {
		
		if(IPraeceptaApiService.class.isInstance(bean)) {
			LOG.info(" Inside Add Post method function. Post Methods Size : {}",postMethods.size());
			
			// Working on Get Methods in a Class
			postMethods.stream().forEach( EACH_POST_METHOD -> {
				PraeceptaExposeAsRestServiceMethod methodAnnotation = AnnotationUtils.findAnnotation(EACH_POST_METHOD, PraeceptaExposeAsRestServiceMethod.class);
				
				String methodName = methodAnnotation.methodName();
				
				PraeceptaRestServiceInterceptorModel methodLevelModel = 
						new PraeceptaRestServiceInterceptorModel(serviceName, methodName, HttpMethod.post);
				
				// Adding Method Level Before Interceptors
				List<Filter> postMethodLevelPreInterceptors = restPreInterceptors.get( methodLevelModel );
				
				before(methodAnnotation.functionPath(), 
						(request, response) -> LOG.info("Execute Before Post Method"));
				
				if(!PraeceptaObjectHelper.isObjectEmpty(postMethodLevelPreInterceptors)) {
					postMethodLevelPreInterceptors.stream().forEach( 
							(EACH_POST_METHOD_LEVEL_PRE_FILTER) -> before( methodAnnotation.functionPath(), EACH_POST_METHOD_LEVEL_PRE_FILTER)
							);
				}
				
				// Adding Actual Post Function to Spark API
				post( methodAnnotation.functionPath(), methodAnnotation.produceType(), (Request request, Response response) -> {
					
					return buildAndRespond(bean, request, response, methodLevelModel);
				});
				
				// Adding Method Level After Interceptors
				List<Filter> postMethodLevelPostInterceptors = restPostInterceptors.get( methodLevelModel );
				
				after(methodAnnotation.functionPath(), 
						(request, response) -> LOG.info("Execute After Post Method"));
				
				if(!PraeceptaObjectHelper.isObjectEmpty(postMethodLevelPostInterceptors)) {
					postMethodLevelPostInterceptors.stream().forEach( 
							(EACH_POST_METHOD_LEVEL_POST_FILTER) -> after( methodAnnotation.functionPath(), EACH_POST_METHOD_LEVEL_POST_FILTER)
							);
				}
				
			});
		}
	}
	
	/**
	 * Adding Put Functions 
	 * @param serviceName
	 * @param getMethods
	 * @param restPreInterceptors
	 * @param restPostInterceptors
	 */
	public static void addPutFunctions(IPraeceptaApiService bean, String serviceName, List<Method> putMethods, Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPreInterceptors, Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPostInterceptors) {
		
		if(IPraeceptaApiService.class.isInstance(bean)) {
			LOG.info(" Inside Add Put method function. Put Methods Size : {}",putMethods.size());
			
			// Working on Get Methods in a Class
			putMethods.stream().forEach( EACH_PUT_METHOD -> {
				
				PraeceptaExposeAsRestServiceMethod methodAnnotation = AnnotationUtils.findAnnotation(EACH_PUT_METHOD, PraeceptaExposeAsRestServiceMethod.class);
				
				String methodName = methodAnnotation.methodName();
				
				PraeceptaRestServiceInterceptorModel methodLevelModel = 
						new PraeceptaRestServiceInterceptorModel(serviceName, methodName, HttpMethod.put);
				
				// Adding Method Level Before Interceptors
				List<Filter> putMethodLevelPreInterceptors = restPreInterceptors.get( methodLevelModel );
				
				before(methodAnnotation.functionPath(), 
						(request, response) -> LOG.info("Execute Before Put Method"));

				if(!PraeceptaObjectHelper.isObjectEmpty(putMethodLevelPreInterceptors)) {
					putMethodLevelPreInterceptors.stream().forEach( 
							(EACH_PUT_METHOD_LEVEL_PRE_FILTER) -> before( methodAnnotation.functionPath(), EACH_PUT_METHOD_LEVEL_PRE_FILTER)
							);
				}
				
				// Adding Actual Put Function to Spark API
				put( methodAnnotation.functionPath(), methodAnnotation.produceType(), (Request request, Response response) -> {
					
					return buildAndRespond(bean, request, response, methodLevelModel);
				});
				
				// Adding Method Level After Interceptors
				List<Filter> putMethodLevelPostInterceptors = restPostInterceptors.get( methodLevelModel );
				
				after(methodAnnotation.functionPath(), 
						(request, response) -> LOG.info("Execute After Put Method"));
				
				if(!PraeceptaObjectHelper.isObjectEmpty(putMethodLevelPostInterceptors)) {
					putMethodLevelPostInterceptors.stream().forEach( 
							(EACH_PUT_METHOD_LEVEL_POST_FILTER) -> after( methodAnnotation.functionPath(), EACH_PUT_METHOD_LEVEL_POST_FILTER)
							);
				}
				
			});
		}
	}
	
	/**
	 * Adding Patch Functions 
	 * @param serviceName
	 * @param getMethods
	 * @param restPreInterceptors
	 * @param restPostInterceptors
	 */
	public static void addPatchFunctions(IPraeceptaApiService bean, String serviceName, List<Method> patchMethods, Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPreInterceptors, Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPostInterceptors) {
		
		if(IPraeceptaApiService.class.isInstance(bean)) {
			LOG.info(" Inside Add Patch method function. Patch Methods Size : {}",patchMethods.size());
			
			// Working on Get Methods in a Class
			patchMethods.stream().forEach( EACH_PATCH_METHOD -> {
				PraeceptaExposeAsRestServiceMethod methodAnnotation = AnnotationUtils.findAnnotation(EACH_PATCH_METHOD, PraeceptaExposeAsRestServiceMethod.class);;
				
				String methodName = methodAnnotation.methodName();
				
				PraeceptaRestServiceInterceptorModel methodLevelModel = 
						new PraeceptaRestServiceInterceptorModel(serviceName, methodName, HttpMethod.patch);
				
				// Adding Method Level Before Interceptors
				List<Filter> patchMethodLevelPreInterceptors = restPreInterceptors.get( methodLevelModel );
				
				before(methodAnnotation.functionPath(), 
						(request, response) -> LOG.info("Execute Before Patch Method"));
				
				if(!PraeceptaObjectHelper.isObjectEmpty(patchMethodLevelPreInterceptors)) {
					patchMethodLevelPreInterceptors.stream().forEach( 
							(EACH_PATCH_METHOD_LEVEL_PRE_FILTER) -> before( methodAnnotation.functionPath(), EACH_PATCH_METHOD_LEVEL_PRE_FILTER)
							);
				}
				
				// Adding Actual Patch Function to Spark API
				patch( methodAnnotation.functionPath(), methodAnnotation.produceType(), (Request request, Response response) -> {
					
					return buildAndRespond(bean, request, response, methodLevelModel);
				});
				
				// Adding Method Level After Interceptors
				List<Filter> patchMethodLevelPostInterceptors = restPostInterceptors.get( methodLevelModel );
				
				after(methodAnnotation.functionPath(), 
						(request, response) -> LOG.info("Execute After Patch Method"));
				
				if(!PraeceptaObjectHelper.isObjectEmpty(patchMethodLevelPostInterceptors)) {
					patchMethodLevelPostInterceptors.stream().forEach( 
							(EACH_PATCH_METHOD_LEVEL_POST_FILTER) -> after( methodAnnotation.functionPath(), EACH_PATCH_METHOD_LEVEL_POST_FILTER)
							);
				}
				
			});
		}
	}
	
	/**
	 * Adding Delete Functions 
	 * @param serviceName
	 * @param getMethods
	 * @param restPreInterceptors
	 * @param restPostInterceptors
	 */
	public static void addDeleteFunctions(IPraeceptaApiService bean, String serviceName, List<Method> deleteMethods, Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPreInterceptors, Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPostInterceptors) {
		
		if(IPraeceptaApiService.class.isInstance(bean)) {
			LOG.info(" Inside Add Delete method function. Delete Methods Size : {} ",deleteMethods.size());
			
			// Working on Get Methods in a Class
			deleteMethods.stream().forEach( EACH_DELETE_METHOD -> {
				PraeceptaExposeAsRestServiceMethod methodAnnotation = AnnotationUtils.findAnnotation(EACH_DELETE_METHOD, PraeceptaExposeAsRestServiceMethod.class);
				
				String methodName = methodAnnotation.methodName();
				
				PraeceptaRestServiceInterceptorModel methodLevelModel = 
						new PraeceptaRestServiceInterceptorModel(serviceName, methodName, HttpMethod.delete);
				
				// Adding Method Level Before Interceptors
				List<Filter> deleteMethodLevelPreInterceptors = restPreInterceptors.get( methodLevelModel );
				
				before(methodAnnotation.functionPath(), 
						(request, response) -> LOG.info("Execute Before Delete Method"));
				
				if(!PraeceptaObjectHelper.isObjectEmpty(deleteMethodLevelPreInterceptors)) {
					deleteMethodLevelPreInterceptors.stream().forEach( 
							(EACH_DELETE_METHOD_LEVEL_PRE_FILTER) -> before( methodAnnotation.functionPath(), EACH_DELETE_METHOD_LEVEL_PRE_FILTER)
							);
				}
				
				// Adding Actual Delete Function to Spark API
				delete( methodAnnotation.functionPath(), methodAnnotation.produceType(), (Request request, Response response) -> {
					
					return buildAndRespond(bean, request, response, methodLevelModel);
				});
				
				// Adding Method Level After Interceptors
				List<Filter> deleteMethodLevelPostInterceptors = restPostInterceptors.get( methodLevelModel );
				
				after(methodAnnotation.functionPath(), 
						(request, response) -> LOG.info("Execute After Delete Method"));
				
				if(!PraeceptaObjectHelper.isObjectEmpty(deleteMethodLevelPostInterceptors)) {
					deleteMethodLevelPostInterceptors.stream().forEach( 
							(EACH_DELETE_METHOD_LEVEL_POST_FILTER) -> after( methodAnnotation.functionPath(), EACH_DELETE_METHOD_LEVEL_POST_FILTER)
							);
				}
				
			});
		}
	}
	
	private static Object buildAndRespond(IPraeceptaApiService bean, Request request, Response response, PraeceptaRestServiceInterceptorModel methodLevelModel) {
		
		PraeceptaRequestStore requestStore = build(request, response);
		
		requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OPERATION, methodLevelModel.getMethodName());
		
		bean.execute(requestStore);
		
		return requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT);
	}

	private static PraeceptaRequestStore build(Request request, Response response) {

		PraeceptaRequestStore requestStore = new PraeceptaRequestStore();
		
		requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_RAW_INPUT, request);
		requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT, request.body());
		requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_CONTENT_TYPE, request.contentType());
		requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_QUERY_PARAMS, request.queryMap().toMap());
		requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_PATH_PARAMS, request.params());

		requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_RAW_OUTPUT, response);
		
		return requestStore;
	}
}

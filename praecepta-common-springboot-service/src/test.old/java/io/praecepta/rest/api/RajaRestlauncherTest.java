package io.praecepta.rest.api;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestService;
import io.praecepta.rest.api.builder.PraeceptaSparkApiServiceBuilder;
import io.praecepta.rest.api.config.TestConfig;
import io.praecepta.rest.api.model.PraeceptaRestServiceInterceptorModel;
import spark.Filter;

/**
 * 
 * @author rajas
 * 
 * @Autowired
    GenericApplicationContext applicationContext;
    
    public List<String> getBeansWithAnnotation(Class<?> annotationClass) {
        return BeanUtils.getBeansWithAnnotation(applicationContext, annotationClass);
    }
 *
 */
public class RajaRestlauncherTest {

	public static void main( String[] args )
    {
		System.setProperty("server.port", "8080");
		
    	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
    	
    	Map<String,Object> beans = context.getBeansWithAnnotation(PraeceptaExposeAsRestService.class);
    	
    	Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPreInterceptors = (Map<PraeceptaRestServiceInterceptorModel, List<Filter>>) context.getBean("restPreInterceptors");
    	
    	Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPostInterceptors = (Map<PraeceptaRestServiceInterceptorModel, List<Filter>>) context.getBean("restPostInterceptors");
    	
    	System.out.println(beans.size());
    	
    	PraeceptaSparkApiServiceBuilder.buildServices(beans, restPreInterceptors, restPostInterceptors);
    }

	
	
	
}

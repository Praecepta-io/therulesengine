package io.praecepta.audit.config;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.praecepta.audit.controller.PraeceptaAuditController;
import io.praecepta.audit.service.IPraeceptaAuditService;
import io.praecepta.audit.service.IPraeceptaExecutionAuditService;
import io.praecepta.audit.service.PraeceptaAuditServiceConstants;
import io.praecepta.audit.service.impl.PraeceptaAuditServiceImpl;
import io.praecepta.audit.service.impl.PraeceptaExecutionAuditServiceImpl;
import io.praecepta.dao.elastic.config.spring.PraeceptaAuditDaoSpringConfig;
import io.praecepta.rest.api.interceptor.PraeceptaRestServiceInterceptorRegistry;
import io.praecepta.rest.api.model.PraeceptaRestServiceInterceptorModel;
import spark.Filter;

@Configuration
@Import(PraeceptaAuditDaoSpringConfig.class)
public class PraeceptaAuditSpringConfig {

	@Bean(name = PraeceptaAuditServiceConstants.AUDIT_SERVICE_NAME)
	public IPraeceptaAuditService getPraeceptaAuditService() {
		return new PraeceptaAuditServiceImpl();
	}
	
	@Bean(name = PraeceptaAuditServiceConstants.AUDIT_EXECUTION_SERVICE_NAME)
	public IPraeceptaExecutionAuditService getPraeceptaExecutionAuditService() {
		return new PraeceptaExecutionAuditServiceImpl();
	}
	
	@Bean(name = PraeceptaAuditServiceConstants.AUDIT_CONTROLLER_NAME)
	public PraeceptaAuditController getAuditController() {
		return new PraeceptaAuditController();
	}
	
	@Bean(name = "restPreInterceptors")
	public Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPreInterceptors(){
		
		return PraeceptaRestServiceInterceptorRegistry.getPreInterceptors();
	}
	
	@Bean(name = "restPostInterceptors")
	public Map<PraeceptaRestServiceInterceptorModel, List<Filter>> restPostInterceptors(){
		
		return PraeceptaRestServiceInterceptorRegistry.getPostInterceptors();
	}
}

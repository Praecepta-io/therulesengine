package io.praecepta.rest.api.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.praecepta.rest.api.interceptor.PraeceptaRestServiceInterceptorRegistry;

import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.HandlerInterceptor;

import io.praecepta.rest.api.model.PraeceptaRestServiceInterceptorModel;

/**
 * Spring Boot Auto-configuration for Praecepta REST Services
 * Replaces the Spark-based service builders
 */
@Configuration
@ComponentScan(basePackages = "io.praecepta.rest.api")
public class PraeceptaSpringBootAutoConfiguration implements WebMvcConfigurer {

@Override
public void addCorsMappings(CorsRegistry registry) {
registry.addMapping("/**")
.allowedOrigins("*")
.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
.allowedHeaders("*");
}

@Override
public void addInterceptors(InterceptorRegistry registry) {
// Add pre-interceptors
Map<PraeceptaRestServiceInterceptorModel, List<HandlerInterceptor>> preInterceptors = 
PraeceptaRestServiceInterceptorRegistry.getPreInterceptors();

if (preInterceptors != null) {
preInterceptors.values().forEach(interceptors -> {
if (interceptors != null) {
interceptors.forEach(registry::addInterceptor);
}
});
}

// Add post-interceptors
Map<PraeceptaRestServiceInterceptorModel, List<HandlerInterceptor>> postInterceptors = 
PraeceptaRestServiceInterceptorRegistry.getPostInterceptors();

if (postInterceptors != null) {
postInterceptors.values().forEach(interceptors -> {
if (interceptors != null) {
interceptors.forEach(registry::addInterceptor);
}
});
}
}

@Bean
@ConditionalOnMissingBean(name = "restPreInterceptors")
public Map<PraeceptaRestServiceInterceptorModel, List<HandlerInterceptor>> restPreInterceptors() {
return PraeceptaRestServiceInterceptorRegistry.getPreInterceptors();
}

@Bean
@ConditionalOnMissingBean(name = "restPostInterceptors")
public Map<PraeceptaRestServiceInterceptorModel, List<HandlerInterceptor>> restPostInterceptors() {
return PraeceptaRestServiceInterceptorRegistry.getPostInterceptors();
}
}

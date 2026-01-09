package io.praecepta.rest.api.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * marker interface to identify implemented classes are Rest services.
 */
@Retention(RUNTIME)
@Target(ElementType.TYPE) 
public @interface PraeceptaExposeAsRestService {
	String serviceName();
	String servicePath();
}

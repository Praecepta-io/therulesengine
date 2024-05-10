package io.praecepta.rest.api.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(ElementType.METHOD) 
public @interface PraeceptaExposeAsRestServiceMethod {

	public boolean get() default false;
	public boolean put() default false;
	public boolean patch() default false;
	public boolean post() default false;
	public boolean delete() default false;
	public boolean options() default false;
	public String functionPath();
	public String produceType() default "application/json";
	public String consumeType() default "application/json";
	public String methodName();
	
}

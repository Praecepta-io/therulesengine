package io.praecepta.core.helper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {
	
	private static final Gson gson = new Gson();
	
	private static final GsonBuilder gsonBuilder = new GsonBuilder();
	
	public static <ENTITY> String toJson(ENTITY entity){
		
		if(entity != null){
			
			return gson.toJson(entity);
		}
		
		return null;
	}

	public static <ENTITY> ENTITY toEntity(String json, Class<ENTITY> clazz){
		
		if(json != null && !"".equals(json)){
			return gson.fromJson(json, clazz);
		}
		return null;
	}
	
	public static <PARENT, CHILD> PARENT toEntity(String json, Class<PARENT> parentClazz, Class<CHILD> childClazz){
		
		Type type = getType(parentClazz, childClazz);
		
		if(json != null && !"".equals(json)){
			return gson.fromJson(json, type);
		}
		return null;
	}
	
	public static Type getType(Class<?> rawClazz, Class<?> paramterClazz){
		
		return new ParameterizedType() {
			
			@Override
			public Type getRawType() {
				return rawClazz;
			}
			
			@Override
			public Type getOwnerType() {
				return null;
			}
			
			@Override
			public Type[] getActualTypeArguments() {
				return new Type[] {paramterClazz};
			}
		};
	}
}

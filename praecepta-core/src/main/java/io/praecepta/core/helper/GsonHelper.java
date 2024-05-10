package io.praecepta.core.helper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

public class GsonHelper {
	
	private static final Gson gson = new Gson();
	
	private static final Gson stringObjectMappedGson = getStringObjectMapGson();
	
	public static <ENTITY> String toJson(ENTITY entity){
		
		if(entity != null){
			
			return gson.toJson(entity);
		}
		
		return null;
	}

	private static Gson getStringObjectMapGson() {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(new TypeToken<Map <String, Object>>(){}.getType(),  new PraceptaGsonDeserializerForMap<String, Object>());
		gsonBuilder.enableComplexMapKeySerialization();
		return gsonBuilder.create();
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
	
	public static String fromMapToJsonPerseveNumber(Object mapEntity) {
        return stringObjectMappedGson.toJson(mapEntity);
	}
	
	public static  Map toMapEntityPreserveNumber(String json) {
		return stringObjectMappedGson.fromJson(json, new TypeToken<Map <String, Object>>(){}.getType());
	}
	
	public static String toJsonPreserveNumber(Object enity) {
		
		return stringObjectMappedGson.toJson(enity);
	}
	
	public static <T> T toEntityPreserveNumber(String json, Class<T> clazz) {
		return stringObjectMappedGson.fromJson(json, clazz);
	}
	
	public static boolean isValidJson(String json) {
	    try {
	        JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
	        if (!jsonElement.isJsonObject() && !jsonElement.isJsonArray()) return false;
	    } catch (Exception e) {
	        return false;
	    }
	    return true;
	}
	
	/**
	 * Use it for the Deserialization of Map Classes and Objects
	 */
	private static class PraceptaGsonDeserializerForMap<KEY, VALUE> implements JsonDeserializer<Map<KEY, VALUE>>{

		@SuppressWarnings("unchecked")
        @Override
        public Map<KEY, VALUE> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return (Map<KEY, VALUE>) read(json);
        }

        public Object read(JsonElement in) {
            if(in.isJsonArray()){
                List<Object> list = new ArrayList<>();
                JsonArray arr = in.getAsJsonArray();
                for (JsonElement anArr : arr) {
                    list.add(read(anArr));
                }
                return list;
            }else if(in.isJsonObject()){
                Map<String, Object> map = new LinkedTreeMap<String, Object>();
                JsonObject obj = in.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entitySet = obj.entrySet();
                for(Map.Entry<String, JsonElement> entry: entitySet){
                    map.put(entry.getKey(), read(entry.getValue()));
                }
                return map;
            }else if( in.isJsonPrimitive()){
                JsonPrimitive prim = in.getAsJsonPrimitive();
                if(prim.isBoolean()){
                    return prim.getAsBoolean();
                }else if(prim.isString()){
                    return prim.getAsString();
                }else if(prim.isNumber()){
                    Number num = prim.getAsNumber();
                    if(Math.ceil(num.doubleValue())  == num.longValue())
                       return num.longValue();
                    else{
                        return num.doubleValue();
                    }
                }
            }
            return null;
        }

    }
}

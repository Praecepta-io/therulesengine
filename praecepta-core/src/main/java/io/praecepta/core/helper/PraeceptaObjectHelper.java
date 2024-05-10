package io.praecepta.core.helper;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class PraeceptaObjectHelper {

	public static boolean isObjectEmpty(Object obj) {
		if (obj == null) {
			return true;
		}

		if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length() == 0;
		}
		if (obj instanceof Collection) {
			return ((Collection) obj).isEmpty();
		}
		if (obj instanceof Map) {
			return ((Map) obj).isEmpty();
		}

		return false;
	}
	
	public static boolean isStringEmpty(Object str) {
		return (str == null || "".equals(str));
	}

	public static boolean stringEquals(String s1, String s2) {

		if(!isStringEmpty(s1) && !(isStringEmpty(s2))){
			return s1.equalsIgnoreCase(s2);
		}else{
			return false;
		}

	}

	public static boolean isObjectNull(Object obj) {
		if (obj == null) {
			return true;
		}

		return false;
	}



}

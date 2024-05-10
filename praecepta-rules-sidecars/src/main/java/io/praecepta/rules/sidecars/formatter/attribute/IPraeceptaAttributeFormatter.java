package io.praecepta.rules.sidecars.formatter.attribute;

import java.util.Map;

public interface IPraeceptaAttributeFormatter {

    public Object format(String pattern, Object value, Map<String,Object> additionalInfoMap);


}

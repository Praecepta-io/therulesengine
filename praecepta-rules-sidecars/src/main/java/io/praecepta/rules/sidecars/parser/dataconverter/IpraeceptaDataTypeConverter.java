package io.praecepta.rules.sidecars.parser.dataconverter;

import java.util.Map;

public interface IpraeceptaDataTypeConverter {

    public Object convert(String data, Map<String,Object> additionalInfoMap);
}

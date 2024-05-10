package io.praecepta.rules.sidecars.parser.registry;

import java.util.HashMap;
import java.util.Map;

import io.praecepta.rules.sidecars.parser.dataconverter.IpraeceptaDataTypeConverter;
import io.praecepta.rules.sidecars.parser.dataconverter.PraeceptaDateTypeConverter;
import io.praecepta.rules.sidecars.parser.dataconverter.PraeceptaDecimalTypeConverter;
import io.praecepta.rules.sidecars.parser.dataconverter.PraeceptaNumberTypeConverter;

public class PraeceptaDataTypeConvertorRegistry {

    public enum PraeceptaDataTypeEnum{
        NUMBER, DECIMAL,DATE
    }

    private static final PraeceptaDataTypeConvertorRegistry dataTypeRegistryInstance = new PraeceptaDataTypeConvertorRegistry();
    private static Map<String, IpraeceptaDataTypeConverter> converterTypeRegistry = new HashMap<>();

    public static PraeceptaDataTypeConvertorRegistry getInstance(){
        converterTypeRegistry = captureDataConverterTypes();
        return dataTypeRegistryInstance;

    }
    public static IpraeceptaDataTypeConverter getDataTypeConvertor(String dataType) {
        return converterTypeRegistry.get(dataType);
    }


    private static Map<String, IpraeceptaDataTypeConverter> captureDataConverterTypes() {

        Map<String, IpraeceptaDataTypeConverter> dataConverterRegistry = new HashMap<>();

        dataConverterRegistry.put(PraeceptaDataTypeEnum.NUMBER.name(), new PraeceptaNumberTypeConverter());
        dataConverterRegistry.put(PraeceptaDataTypeEnum.DECIMAL.name(), new PraeceptaDecimalTypeConverter());
        dataConverterRegistry.put(PraeceptaDataTypeEnum.DATE.name(), new PraeceptaDateTypeConverter());
        return dataConverterRegistry;
    }
}

package io.praecepta.rules.sidecars.formatter.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.rules.sidecars.formatter.attribute.impl.PraeceptaDateFormatter;
import io.praecepta.rules.sidecars.formatter.attribute.impl.PraeceptaDecimalFormatter;
import io.praecepta.rules.sidecars.formatter.attribute.impl.PraeceptaPhoneNumberFormatter;

public class PraeceptaAttributeFormatterTypeRegistry {

    private final static Logger logger = LoggerFactory.getLogger(PraeceptaAttributeFormatterTypeRegistry.class);

    private static final Map<String, IPraeceptaAttributeFormatter> formatterTypeRegistry = captureFormatterTypes();

    private static final List<String> formatterTypeStrs = getAllFormatterTypesInStringList();

    public enum PraeceptaAttributeFormatterType{
        DECIMAL_FORMAT, PHONE_FORMAT, DATE_FORMAT, HARD_CODE
    }
    private static Map<String, IPraeceptaAttributeFormatter> captureFormatterTypes() {

        Map<String, IPraeceptaAttributeFormatter> formatterRegistry = new HashMap<>();

        formatterRegistry.put(PraeceptaAttributeFormatterType.DECIMAL_FORMAT.name(), new PraeceptaDecimalFormatter());
        formatterRegistry.put(PraeceptaAttributeFormatterType.DATE_FORMAT.name(), new PraeceptaDateFormatter());
        formatterRegistry.put(PraeceptaAttributeFormatterType.PHONE_FORMAT.name(), new PraeceptaPhoneNumberFormatter());

        return formatterRegistry;
    }

    private static List<String> getAllFormatterTypesInStringList() {

        List<String> formatterTypeStrList = new ArrayList<>();

        for(PraeceptaAttributeFormatterType formatterType : PraeceptaAttributeFormatterType.values()) {
            logger.info(" Adding Attribute Formatter Available {} ", formatterType.name());
            formatterTypeStrList.add(formatterType.name());
        }

        logger.info(" List Of Attribute Formatter Available --> {} ", formatterTypeStrList);

        return formatterTypeStrList;
    }

    public static IPraeceptaAttributeFormatter getFormatterByType(String formatterType) {

        logger.info("Inside the Get Attribute Formatter By Type to fetch for Type {} ", formatterType);

        IPraeceptaAttributeFormatter formatter = null;

        if(formatterTypeStrs.contains(formatterType)) {

            formatter = formatterTypeRegistry.get(formatterType);
        }

        logger.info(" Formatter To Return for a Type {}", formatter);
        return formatter;
    }



}

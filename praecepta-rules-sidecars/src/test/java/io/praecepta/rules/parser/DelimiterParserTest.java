package io.praecepta.rules.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.sidecars.IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.parser.impl.PraeceptaDelimiterParser;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class DelimiterParserTest {

    @Test
    public void parseTest(){
        PraeceptaDelimiterParser delimiterParser = new PraeceptaDelimiterParser();

        PraeceptaSideCarDataHolder<String, Map<String,Object>> holder = new PraeceptaSideCarDataHolder();
        String inputJson = "testUser|2024-10-10";

        holder.addInput(inputJson);
        
        Map<String,Object> additionalInfoMap = new HashMap<>();
        
        Map<String,Object> sideCarConfigs = new HashMap<>();
        sideCarConfigs.put(PraeceptaDelimiterParser.DELIMITER_CHAR, "\\|");
        
        Map<Integer, String> attributesMapping = new HashMap<>();
        attributesMapping.put(0,"name");
        attributesMapping.put(1,"dob");
        sideCarConfigs.put(PraeceptaDelimiterParser.DELIMITER_PARSER_ATTRIBUTE_NAMES, attributesMapping);
        
        additionalInfoMap
		.put(IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.RUN_TIME_CONFIG, sideCarConfigs);
        
        holder.setAdditionalHolderInfo(additionalInfoMap);
        delimiterParser.parse(holder);

        Map<String,Object> output = holder.retriveOutput();
        assertNotNull(output);

        assertEquals("testUser",output.get("name"));
        assertEquals("2024-10-10",output.get("dob"));
    }


    @Test
    public void parseTest2(){
        PraeceptaDelimiterParser delimiterParser = new PraeceptaDelimiterParser();

        PraeceptaSideCarDataHolder<String, Map<String,Object>> holder = new PraeceptaSideCarDataHolder();
        String inputJson = "testUser|2024-10-10";

        holder.addInput(inputJson);
        
        Map<String,Object> additionalInfoMap = new HashMap<>();

        Map<String,Object> sideCarConfigs = new HashMap<>();
        sideCarConfigs.put(PraeceptaDelimiterParser.DELIMITER_CHAR, "\\|");
        
        additionalInfoMap
		.put(IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.RUN_TIME_CONFIG, sideCarConfigs);
        
        holder.setAdditionalHolderInfo(additionalInfoMap);
        
        delimiterParser.parse(holder);

        Map<String,Object> output = holder.retriveOutput();
        assertNotNull(output);

        assertEquals("testUser",output.get("index0"));
        assertEquals("2024-10-10",output.get("index1"));
    }
}

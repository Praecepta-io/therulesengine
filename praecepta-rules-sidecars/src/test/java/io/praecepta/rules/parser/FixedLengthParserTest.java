package io.praecepta.rules.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.sidecars.IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.parser.dataconverter.PraeceptaDateTypeConverter;
import io.praecepta.rules.sidecars.parser.impl.PraeceptaFixedLengthParser;
import io.praecepta.rules.sidecars.parser.registry.PraeceptaDataTypeConvertorRegistry.PraeceptaDataTypeEnum;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class FixedLengthParserTest {

    @Test
    public void parseTest(){

        PraeceptaFixedLengthParser fixedLengthParser = new PraeceptaFixedLengthParser();

        PraeceptaSideCarDataHolder<String, Map<String,Object>> holder = new PraeceptaSideCarDataHolder();
        String inputJson = "testUser            100000     ";

        holder.addInput(inputJson);

        Map<String,Object> additionalInfoMap = new HashMap<>();
        
        Map<String,Object> sideCarConfigs = new HashMap<>();
        
	        Map<String,Object> nameMetadataMap = new HashMap<>();
	        nameMetadataMap.put("start", 0);
	        nameMetadataMap.put("end", 20);
	        sideCarConfigs.put("name", nameMetadataMap);
	        
	        Map<String,Object> salMetadataMap = new HashMap<>();
	        salMetadataMap.put("start", 20);
	        salMetadataMap.put("end", 29);
	        
	     // Testing The Data Type as Number and It's Conversion
	        salMetadataMap.put(PraeceptaFixedLengthParser.DATA_TYPE, PraeceptaDataTypeEnum.NUMBER.name());
	        sideCarConfigs.put("sal",salMetadataMap);
        
        additionalInfoMap
		.put(IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.RUN_TIME_CONFIG, sideCarConfigs);
        
        holder.setAdditionalHolderInfo(additionalInfoMap);
        fixedLengthParser.parse(holder);

        Map<String,Object> output = holder.retriveOutput();
        assertNotNull(output);

        assertEquals("testUser",output.get("name"));
        assertEquals(100000L,output.get("sal"));
    }

    @Test
    public void parseDecimalTest(){

        PraeceptaFixedLengthParser fixedLengthParser = new PraeceptaFixedLengthParser();

        PraeceptaSideCarDataHolder<String, Map<String,Object>> holder = new PraeceptaSideCarDataHolder();
        String inputJson = "testUser            100000.9     ";

        holder.addInput(inputJson);

        Map<String,Object> additionalInfoMap = new HashMap<>();
        
        Map<String,Object> sideCarConfigs = new HashMap<>();
        
	        Map<String,Object> nameMetadataMap = new HashMap<>();
	        nameMetadataMap.put("start", 0);
	        nameMetadataMap.put("end", 20);
	        sideCarConfigs.put("name", nameMetadataMap);
	        
	        Map<String,Object> salMetadataMap = new HashMap<>();
	        salMetadataMap.put("start", 20);
	        salMetadataMap.put("end", 29);
	        
	     // Testing The Data Type as Decimal and It's Conversion
	        salMetadataMap.put(PraeceptaFixedLengthParser.DATA_TYPE, PraeceptaDataTypeEnum.DECIMAL.name());
	        sideCarConfigs.put("sal",salMetadataMap);
        
	    additionalInfoMap
			.put(IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.RUN_TIME_CONFIG, sideCarConfigs);
        
        holder.setAdditionalHolderInfo(additionalInfoMap);
        fixedLengthParser.parse(holder);

        Map<String,Object> output = holder.retriveOutput();
        assertNotNull(output);

        assertEquals("testUser",output.get("name"));
        assertEquals(100000.9,output.get("sal"));
    }


    @Test
    public void parseDateTest(){

        PraeceptaFixedLengthParser fixedLengthParser = new PraeceptaFixedLengthParser();

        PraeceptaSideCarDataHolder<String, Map<String,Object>> holder = new PraeceptaSideCarDataHolder();
        String inputJson = "testUser            2024-10-10";

        holder.addInput(inputJson);

        Map<String,Object> additionalInfoMap = new HashMap<>();
        
        Map<String,Object> sideCarConfigs = new HashMap<>();
        
	        Map<String,Object> nameMetadataMap = new HashMap<>();
	        nameMetadataMap.put("start", 0);
	        nameMetadataMap.put("end", 20);
	        sideCarConfigs.put("name", nameMetadataMap);
	        
	        Map<String,Object> salMetadataMap = new HashMap<>();
	        salMetadataMap.put("start", 20);
	        salMetadataMap.put("end", 30);
	        salMetadataMap.put(PraeceptaFixedLengthParser.DATA_TYPE, PraeceptaDataTypeEnum.DATE.name());
	        
	        salMetadataMap.put(PraeceptaDateTypeConverter.FROM_DATE_FORMAT, "yyyy-MM-dd");
	        salMetadataMap.put(PraeceptaDateTypeConverter.TO_DATE_FORMAT, "yyyy/MM/dd");
	        sideCarConfigs.put("dob",salMetadataMap);
	        
	    additionalInfoMap
			.put(IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.RUN_TIME_CONFIG, sideCarConfigs);    
        
        holder.setAdditionalHolderInfo(additionalInfoMap);
        fixedLengthParser.parse(holder);

        Map<String,Object> output = holder.retriveOutput();
        assertNotNull(output);

        assertEquals("testUser",output.get("name"));
        assertEquals("2024/10/10",output.get("dob"));
    }
}

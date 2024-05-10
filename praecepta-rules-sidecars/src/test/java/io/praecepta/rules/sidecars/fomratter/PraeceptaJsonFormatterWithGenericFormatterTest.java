package io.praecepta.rules.sidecars.fomratter;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rules.sidecars.IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.formatter.impl.PraeceptaGenericFormatter;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaJsonFormatterWithGenericFormatterTest {

	@Before
	public void init() {
		
	}

	@Test
	public void test() {
		PraeceptaGenericFormatter jsonFormatterTest = new PraeceptaGenericFormatter();
//		PraeceptaJsonFormatter jsonFormatterTest = new PraeceptaJsonFormatter();
		
		jsonFormatterTest.initializeFormatter();

		PraeceptaSideCarDataHolder<Map<String, Object>, String> sideCarDataHolder = new PraeceptaSideCarDataHolder<>();

		Map<String, Object> additionalInfo = new HashMap<>();
		additionalInfo.put(IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.RUN_TIME_CONFIG, getJsonFormatterConfig());

		sideCarDataHolder.setAdditionalHolderInfo(additionalInfo);

		sideCarDataHolder.addInput(
				(GsonHelper.toEntity(
				"{\"emp\":{\"name\":\"vara\",\"mobile\":\"8297799456\",\"dob\":\"2020-10-19\",\"email\":\"varam.kotapati@gmail.com\",\"sql\":123456.79}}"
						, Map.class)));


		jsonFormatterTest.format(sideCarDataHolder);
        
		assertNotNull(sideCarDataHolder.retriveOutput());
		
	}
	
	public static Map<String, Object> getJsonFormatterConfig(){
		Map<String, Object> jsonFormatterConfigs =new HashMap<>();
		
		String sideCarConfigInJson = "{\r\n" + 
				"   \"TEMPLATE_NAME\":\"TEST\",\r\n" + 
				"   \"FORMATE_DETAILS\":{\r\n" + 
				"      \"formatterAtributes\":[\r\n" + 
				"         {\r\n" + 
				"            \"formattedAttributeName\":\"sal\",\r\n" + 
				"            \"sourceAttributeName\":\"emp.sql\",\r\n" + 
				"            \"formatterConfigs\":{\r\n" + 
				"               \"formatterType\":\"DECIMAL_FORMAT\",\r\n" + 
				"               \"pattern\":\"#.00000\"\r\n" + 
				"            }\r\n" + 
				"         },\r\n" + 
				"         {\r\n" + 
				"            \"formattedAttributeName\":\"mobile\",\r\n" + 
				"            \"sourceAttributeName\":\"emp.mobile\",\r\n" + 
				"            \"formatterConfigs\":{\r\n" + 
				"               \"formatterType\":\"PHONE_FORMAT\",\r\n" + 
				"               \"pattern\":\"(####)-###-###\"\r\n" + 
				"            }\r\n" + 
				"         },\r\n" + 
				"         {\r\n" + 
				"            \"formattedAttributeName\":\"dob\",\r\n" + 
				"            \"sourceAttributeName\":\"emp.dob\",\r\n" + 
				"            \"formatterConfigs\":{\r\n" + 
				"               \"formatterType\":\"DATE_FORMAT\",\r\n" + 
				"               \"pattern\":\"yyyyMMdd\",\r\n" + // To Date Format
				"               \"additionalInfoMap\":{\r\n" + 
				"                  \"fromDateFormat\":\"yyyy-MM-dd\"\r\n" + 
				"               }\r\n" + 
				"            }\r\n" + 
				"         }\r\n" + 
				"      ]\r\n" + 
				"   },\r\n" + 
				"   \"FORMATTER_TEMPLATE\":\"{\\\"emp\\\" : {\\\"name\\\" : \\\"${emp.name}\\\",\\\"sal\\\" : \\\"${emp.sql}\\\",\\\"dob\\\" : \\\"${emp.dob}\\\"}}\"\r\n" + 
				"}";
		
		
		System.out.println(sideCarConfigInJson);
		
		jsonFormatterConfigs = GsonHelper.toEntity(sideCarConfigInJson, Map.class);
		
		return jsonFormatterConfigs;
	}
}

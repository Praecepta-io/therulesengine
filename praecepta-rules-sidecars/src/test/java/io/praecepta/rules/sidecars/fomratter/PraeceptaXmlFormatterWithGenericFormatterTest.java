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

public class PraeceptaXmlFormatterWithGenericFormatterTest {

	@Before
	public void init() {
		
	}

	@Test
	public void test() {
		PraeceptaGenericFormatter xmlFormatterTest = new PraeceptaGenericFormatter();
		
		xmlFormatterTest.initializeFormatter();
		
		PraeceptaSideCarDataHolder<Map<String, Object>, String> sideCarDataHolder = new PraeceptaSideCarDataHolder<>();

		Map<String, Object> additionalInfo = new HashMap<>();
		additionalInfo.put(IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.RUN_TIME_CONFIG, getJsonFormatterConfig());

		sideCarDataHolder.setAdditionalHolderInfo(additionalInfo);

		sideCarDataHolder.addInput(
				GsonHelper.toMapEntityPreserveNumber(
//						"{\"name\":\"vara\",\"mobile\":\"8297799456\",\"dob\":\"2020-10-19\",\"email\":\"varam.kotapati@gmail.com\",\"sql\":123456.79}"
						"{\"emp\":{\"name\":\"vara\",\"mobile\":\"8297799456\",\"dob\":\"2020-10-19\",\"email\":\"varam.kotapati@gmail.com\",\"sql\":123456.79}}"
						));
		
		xmlFormatterTest.format(sideCarDataHolder);
        
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
				"               \"pattern\":\"yyyyMMdd\",\r\n" + 
				"               \"additionalInfoMap\":{\r\n" + 
				"                  \"fromDateFormat\":\"yyyy-MM-dd\"\r\n" + 
				"               }\r\n" + 
				"            }\r\n" + 
				"         }\r\n" + 
				"      ]\r\n" + 
				"   },\r\n" + 
				"   \"FORMATTER_TEMPLATE\":\"<emp><name>${emp.name}</name><sal>${emp.sql}</sal><dob>${emp.dob}</dob><mobile>${emp.mobile}</mobile></emp>\"\r\n" + 
				"}";
		
		System.out.println(sideCarConfigInJson);
		
		jsonFormatterConfigs = GsonHelper.toEntity(sideCarConfigInJson, Map.class);
		
		return jsonFormatterConfigs;
	}
}

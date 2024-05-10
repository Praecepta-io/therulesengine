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

public class PraeceptaGenericFormatterWithOutAttributeFormateTest {

	@Before
	public void init() {
		
	}

	@Test
	public void test() {
		PraeceptaGenericFormatter xmlFormatterTest = new PraeceptaGenericFormatter();
		
		xmlFormatterTest.initializeFormatter();

		PraeceptaSideCarDataHolder sideCarDataHolder = new PraeceptaSideCarDataHolder();

		Map<String, Object> additionalInfo = new HashMap<>();
		additionalInfo.put(IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.RUN_TIME_CONFIG, getJsonFormatterConfig());

		sideCarDataHolder.setAdditionalHolderInfo(additionalInfo);

//		sideCarDataHolder.addInput("{\"name\":\"vara\",\"mobile\":\"8297799456\",\"dob\":\"2020-10-19\",\"email\":\"varam.kotapati@gmail.com\",\"sql\":123456.79}");

		sideCarDataHolder.addInput("{\"emp\":{\"name\":\"vara\",\"mobile\":\"8297799456\",\"dob\":\"2020-10-19\",\"email\":\"varam.kotapati@gmail.com\",\"sql\":123456.79}}");

		xmlFormatterTest.format(sideCarDataHolder);
        
		assertNotNull(sideCarDataHolder.retriveOutput());
		
	}
	
	public static Map<String, Object> getJsonFormatterConfig(){
		Map<String, Object> jsonFormatterConfigs =new HashMap<>();

		String sideCarConfigInJson = "{\r\n" + 
				"   \"TEMPLATE_NAME\":\"TEST\",\r\n" + 
				"   \"FORMATTER_TEMPLATE\":\"<emp><name>${name}</name><sal>${sql}</sal><dob>${dob}</dob></emp>\"\r\n" + 
				"}";
		
		System.out.println(sideCarConfigInJson);
		
		jsonFormatterConfigs = GsonHelper.toEntity(sideCarConfigInJson, Map.class);
		
		return jsonFormatterConfigs;
	}
}

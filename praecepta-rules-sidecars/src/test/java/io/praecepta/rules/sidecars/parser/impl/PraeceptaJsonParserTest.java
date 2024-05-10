package io.praecepta.rules.sidecars.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaJsonParserTest {
	

	@Test
	public void testGsonHelper() {
		String inputJson = "{\"name\":\"testUser\",\"ageNum\":999}";
		
		Map<String, Object> parsedData = GsonHelper.toMapEntityPreserveNumber(inputJson);
		
		System.out.println(parsedData);
		
		String inputClassJsonWithFloat = "{\"attr1\":\"testAttributeFloat\",\"id1\":123.6}";
		
		A a = GsonHelper.toEntityPreserveNumber(inputClassJsonWithFloat, A.class);
		System.out.println(a);
		
		String inputClassJsonWithInt = "{\"attr1\":\"testAttributeInt\",\"id\":534}";
		
		a = GsonHelper.toEntityPreserveNumber(inputClassJsonWithInt, A.class);
		System.out.println(a);
		
	}
	
	@Test
	public void testJsonValidity() {
		
		String inputJson = "{\"name\":\"testUser\",\"ageNum\":999}";
		
		PraeceptaJsonParser jsonParser = new PraeceptaJsonParser();
		
		boolean validate = jsonParser.validate(inputJson);
		
		assertTrue(validate);
		
		String invalidJson = "Test Data";
		
		validate = jsonParser.validate(invalidJson);
		
		assertFalse(validate);
	}
	
	@Test
	public void testParsing() {
		
		String inputJson = "{\"name\":\"testUser\",\"ageNum\":999}";
		
		PraeceptaSideCarDataHolder<String, Map<String, Object>> sideCarDataHolder = new PraeceptaSideCarDataHolder<>();
		sideCarDataHolder.addInput(inputJson);
		
		PraeceptaJsonParser jsonParser = new PraeceptaJsonParser();
		
		jsonParser.parse(sideCarDataHolder);
		
		assertNotNull(sideCarDataHolder.retriveOutput());
		
		Map<String, Object> parsedData = sideCarDataHolder.retriveOutput();
		
		assertEquals("testUser" ,parsedData.get("name"));
		assertEquals(999L ,parsedData.get("ageNum"));
	}
	
	private static class A{
		private String attr1;
		private int id;
		private float id1;
		public A(String attr1, int id) {
			this(attr1, id, 0);
		}
		public A(String attr1, int id, float id1) {
			this.attr1 = attr1;
			this.id = id;
			this.id1 = id1;
		}
		@Override
		public String toString() {
			return "A [attr1=" + attr1 + ", id=" + id + ", id1=" + id1 + "]";
		}
		
		
	}

}

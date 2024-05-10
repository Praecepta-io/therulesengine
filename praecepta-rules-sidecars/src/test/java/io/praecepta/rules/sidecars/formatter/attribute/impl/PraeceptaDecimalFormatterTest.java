package io.praecepta.rules.sidecars.formatter.attribute.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PraeceptaDecimalFormatterTest {

	@Test
	public void testIntgerFormat() {

		PraeceptaDecimalFormatter decimalFormatter = new PraeceptaDecimalFormatter();
		
		int decimalToConvert = 123456789;
		String pattern = "#.00000";
		
		Object formattedObject = decimalFormatter.format(pattern, decimalToConvert, null);
		
		System.out.println(formattedObject);
		
		assertEquals("123456789.00000", formattedObject);
	}
	
	@Test
	public void testFloat() {
		
		PraeceptaDecimalFormatter decimalFormatter = new PraeceptaDecimalFormatter();
		
		Float decimalToConvert = 123456.7F;
		String pattern = "#.00000";
		
		Object formattedObject = decimalFormatter.format(pattern, decimalToConvert, null);
		
		System.out.println(formattedObject);
		
		assertEquals("123456.70000", formattedObject);
	}
	
	@Test
	public void testDouble() {
		
		PraeceptaDecimalFormatter decimalFormatter = new PraeceptaDecimalFormatter();
		
		Double decimalToConvert = 123456.78;
		String pattern = "#.00000";
		
		Object formattedObject = decimalFormatter.format(pattern, decimalToConvert, null);
		
		System.out.println(formattedObject);
		
		assertEquals("123456.78000", formattedObject);
	}
	
	@Test
	public void testInputWithInvalidFormat() {
		
		PraeceptaDecimalFormatter decimalFormatter = new PraeceptaDecimalFormatter();
		
		Integer decimalToConvert = 123456;
		String pattern = "testFormat";
		
		Object formattedObject = decimalFormatter.format(pattern, decimalToConvert, null);
		
		System.out.println(formattedObject);
		
		assertEquals("testFormat123456", formattedObject);
	}
	
	@Test
	public void testInvalidateInputObjectType() {
		
		PraeceptaDecimalFormatter decimalFormatter = new PraeceptaDecimalFormatter();
		
		String decimalToConvert = "testInput";
		String pattern = "#.00000";
		
		System.out.println(decimalFormatter.format(pattern, decimalToConvert, null));
	}

}

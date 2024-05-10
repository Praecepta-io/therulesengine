package io.praecepta.rules.sidecars.formatter.attribute.impl;

import org.junit.Test;

public class PraeceptaPhoneNumberFormatterTest {

	@Test
	public void testDefaultUs() {

		PraeceptaPhoneNumberFormatter phoneFormatter = new PraeceptaPhoneNumberFormatter();
		
		int decimalToConvert = 1234567890;
		
		Object formattedObject = phoneFormatter.format("", decimalToConvert, null);
		
		System.out.println(formattedObject);
	}
	
	@Test
	public void testSingapore() {
		
		PraeceptaPhoneNumberFormatter phoneFormatter = new PraeceptaPhoneNumberFormatter();
		
		int decimalToConvert = 12345678;
		String pattern = "####-####";
		
		Object formattedObject = phoneFormatter.format(pattern, decimalToConvert, null);
		
		System.out.println(formattedObject);
	}
	
	@Test
	public void testStringInput() {
		
		PraeceptaPhoneNumberFormatter phoneFormatter = new PraeceptaPhoneNumberFormatter();
		
		String decimalToConvert = "12345678";
		String pattern = "####-####";
		
		Object formattedObject = phoneFormatter.format(pattern, decimalToConvert, null);
		
		System.out.println(formattedObject);
	}
	
	@Test
	public void testWrongInput() {
		
		PraeceptaPhoneNumberFormatter phoneFormatter = new PraeceptaPhoneNumberFormatter();
		
		String decimalToConvert = "raja";
		String pattern = "####-####";
		
		Object formattedObject = phoneFormatter.format(pattern, decimalToConvert, null);
		
		System.out.println(formattedObject);
	}
	
	@Test
	public void testWrongFormat() {
		
		PraeceptaPhoneNumberFormatter phoneFormatter = new PraeceptaPhoneNumberFormatter();
		
		String decimalToConvert = "12345678";
		String pattern = "testPattern";
		
		Object formattedObject = phoneFormatter.format(pattern, decimalToConvert, null);
		
		System.out.println(formattedObject);
	}

}

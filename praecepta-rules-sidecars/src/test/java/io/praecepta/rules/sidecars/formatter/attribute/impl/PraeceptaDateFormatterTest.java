package io.praecepta.rules.sidecars.formatter.attribute.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class PraeceptaDateFormatterTest {

	@Test
	public void test() {

		PraeceptaDateFormatter dateFormatter = new PraeceptaDateFormatter();

		Map<String, Object> additionalInfoMap = new HashMap<String, Object>(1);
		additionalInfoMap.put(PraeceptaDateFormatter.FROM_FORMAT, "yyyy-MM-dd");

		String dob = "2020-10-19";

		Object formattedObject = dateFormatter.format("yyyyMMdd", dob, additionalInfoMap);

		System.out.println(formattedObject);
		
		assertEquals("20201019", formattedObject);
	}

	@Test
	public void testInputInvalidFormat() {

		PraeceptaDateFormatter dateFormatter = new PraeceptaDateFormatter();

		Map<String, Object> additionalInfoMap = new HashMap<String, Object>(1);
		additionalInfoMap.put(PraeceptaDateFormatter.FROM_FORMAT, "yyyy-MM-dd");

		String dob = "2020-MAR-19";

		Object formattedObject = dateFormatter.format("yyyyMMdd", dob, additionalInfoMap);

		assertEquals(dob, formattedObject);

	}

	@Test
	public void testInputOutvalidFormat() {

		PraeceptaDateFormatter dateFormatter = new PraeceptaDateFormatter();

		Map<String, Object> additionalInfoMap = new HashMap<String, Object>(1);
		additionalInfoMap.put(PraeceptaDateFormatter.FROM_FORMAT, "yyyy-MMM-dd");

		String dob = "2020-MAR-19";

		Object formattedObject = dateFormatter.format("format", dob, additionalInfoMap);

		assertEquals(dob, formattedObject);
	}

	@Test
	public void testDefaultOutFormat() {

		PraeceptaDateFormatter dateFormatter = new PraeceptaDateFormatter();

		Map<String, Object> additionalInfoMap = new HashMap<String, Object>(1);
		additionalInfoMap.put(PraeceptaDateFormatter.FROM_FORMAT, "yyyy-MM-dd");

		String dob = "2020-10-19";

		Object formattedObject = dateFormatter.format("", dob, additionalInfoMap);

		assertEquals("20-Oct-19", formattedObject);
	}

	@Test
	public void testDefaultInFormat() {

		PraeceptaDateFormatter dateFormatter = new PraeceptaDateFormatter();

		Map<String, Object> additionalInfoMap = new HashMap<String, Object>(1);

		String dob = "2020-10-19";

		Object formattedObject = dateFormatter.format("yyyyMMMdd", dob, additionalInfoMap);

		System.out.println(formattedObject);
	}

	@Test
	public void testDefaultInAndOutFormat() {

		PraeceptaDateFormatter dateFormatter = new PraeceptaDateFormatter();

		Map<String, Object> additionalInfoMap = new HashMap<String, Object>(1);

		String dob = "2020-10-19";

		Object formattedObject = dateFormatter.format("", dob, additionalInfoMap);

		System.out.println(formattedObject);
	}

	@Test
	public void testEmptyInput() {

		PraeceptaDateFormatter dateFormatter = new PraeceptaDateFormatter();

		Map<String, Object> additionalInfoMap = new HashMap<String, Object>(1);

		Object formattedObject = dateFormatter.format("", null, additionalInfoMap);

		System.out.println(formattedObject);
	}

}

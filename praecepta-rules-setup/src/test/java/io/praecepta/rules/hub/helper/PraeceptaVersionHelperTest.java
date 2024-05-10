package io.praecepta.rules.hub.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PraeceptaVersionHelperTest {

	@Test
	public void testGenerateAVersion() {
		String verison = PraeceptaVersionHelper.generateAVersion();
		
		assertNotNull(verison);
	}
	
	@Test
	public void testDefaultAVersion() {
		String defaultVerison = PraeceptaVersionHelper.defaultVersion();
		
		assertEquals(defaultVerison, "V1.M1.P1");
	}
	
	@Test
	public void testNextMajorVersion() {
		String nextMajorVersion = PraeceptaVersionHelper.nextMajorVersion("V123");
		
		assertEquals(nextMajorVersion, "V124");
	}
	
	@Test
	public void testNextMajorVersionWithJustVersionNumber() {
		String nextMajorVersion = PraeceptaVersionHelper.nextMajorVersion("567");
		
		assertEquals(nextMajorVersion, "V568");
	}
	
	@Test
	public void testNextMinorVersion() {
		String nextMinorVersion = PraeceptaVersionHelper.nextMinorVersion("M123");
		
		assertEquals(nextMinorVersion, "M124");
	}
	
	@Test
	public void testNextMinorVersionWithJustVersionNumber() {
		String nextMinorVersion = PraeceptaVersionHelper.nextMinorVersion("567");
		
		assertEquals(nextMinorVersion, "M568");
	}
	
	@Test
	public void testNextPatchVersion() {
		String nextPatchVersion = PraeceptaVersionHelper.nextPatchVersion("P123");
		
		assertEquals(nextPatchVersion, "P124");
	}
	
	@Test
	public void testNextPatchVersionWithJustVersionNumber() {
		String nextPatchVersion = PraeceptaVersionHelper.nextPatchVersion("567");
		
		assertEquals(nextPatchVersion, "P568");
	}

}

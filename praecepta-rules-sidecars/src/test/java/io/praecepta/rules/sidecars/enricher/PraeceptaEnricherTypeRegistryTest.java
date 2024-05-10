package io.praecepta.rules.sidecars.enricher;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import io.praecepta.rules.sidecars.enricher.PraeceptaEnricherTypeRegistry.PraeceptaEnricherType;

public class PraeceptaEnricherTypeRegistryTest {

	@Test
	public void test() {

		IPraeceptaSideCarEnricher encricherSideCarTypeObj = 
				PraeceptaEnricherTypeRegistry.getInstance().getEnricherByType("test");
		
		assertNull(encricherSideCarTypeObj);
		
		encricherSideCarTypeObj = 
				PraeceptaEnricherTypeRegistry.getInstance().getEnricherByType(PraeceptaEnricherType.SIMPLE_REST_API.name());
		
		assertNotNull(encricherSideCarTypeObj);
	}

}

package io.praecepta.rules.sidecars.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import io.praecepta.rules.sidecars.parser.PraeceptaParserTypeRegistry.PraeceptaParserType;
import io.praecepta.rules.sidecars.parser.impl.PraeceptaJsonParser;

public class PraeceptaParserTypeRegistryTest {

	@Test
	public void testGetParserByType() {
		
		PraeceptaJsonParser jsonParser = (PraeceptaJsonParser) PraeceptaParserTypeRegistry.getInstance().getParserByType(PraeceptaParserType.JSON.name());
		
		assertNotNull(jsonParser);
		
	}
	
	@Test
	public void testGetParserByAnInvalidType() {
		
		IPraeceptaSideCarParser parser = (PraeceptaJsonParser) PraeceptaParserTypeRegistry.getInstance().getParserByType("ABC");
		
		assertNull(parser);
		
	}

}

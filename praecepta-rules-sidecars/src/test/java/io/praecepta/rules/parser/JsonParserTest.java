package io.praecepta.rules.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.sidecars.parser.impl.PraeceptaJsonParser;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class JsonParserTest {

    @Test
    public void parserTest(){
        PraeceptaJsonParser jsonParser = new PraeceptaJsonParser();

        PraeceptaSideCarDataHolder<String, Map<String,Object>> holder = new PraeceptaSideCarDataHolder();
        String inputJson = "{\"name\":\"testUser\",\"sal\":100000}";

        holder.addInput(inputJson);

        jsonParser.parse(holder);

        Map<String,Object> output = holder.retriveOutput();
        assertNotNull(output);

        assertEquals("testUser",output.get("name"));
        assertEquals(100000L,output.get("sal"));
    }
}

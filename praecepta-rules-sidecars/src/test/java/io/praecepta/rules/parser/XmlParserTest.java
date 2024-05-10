package io.praecepta.rules.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.sidecars.parser.impl.PraeceptaXmlParser;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class XmlParserTest {

    @Test
    public void xmlParserTest(){
        PraeceptaXmlParser xmlParser = new PraeceptaXmlParser();

        PraeceptaSideCarDataHolder<String, Map<String,Object>> holder = new PraeceptaSideCarDataHolder();
        String inputJson = "<emp><name>testUser</name><sal>100000</sal></emp>";

        holder.addInput(inputJson);

        xmlParser.parse(holder);

        Map<String,Object> output = holder.retriveOutput();
        assertNotNull(output);

        assertEquals("testUser",output.get("name"));
        assertEquals("100000",output.get("sal"));
    }


    @Test
    public void complexXmlParserTest(){
        PraeceptaXmlParser xmlParser = new PraeceptaXmlParser();

        PraeceptaSideCarDataHolder<String, Map<String,Object>> holder = new PraeceptaSideCarDataHolder();
        String inputJson = "<emp><name>testUser</name><sal>100000</sal><address><street>name of the street</street></address></emp>";

        holder.addInput(inputJson);

        xmlParser.parse(holder);

        Map<String,Object> output = holder.retriveOutput();
        assertNotNull(output);

        assertEquals("testUser",output.get("name"));
        assertEquals("100000",output.get("sal"));

        assertNotNull(output.get("address"));

        assertEquals("name of the street",((Map<String,Object>) output.get("address")).get("street") );
    }
}

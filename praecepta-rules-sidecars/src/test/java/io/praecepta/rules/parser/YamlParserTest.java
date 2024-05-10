package io.praecepta.rules.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.sidecars.parser.impl.PraeceptaYamlParser;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class YamlParserTest {

    @Test
    public void testParser(){
        PraeceptaYamlParser praeceptaYamlParser = new PraeceptaYamlParser();

        PraeceptaSideCarDataHolder<String, Map<String,Object>> holder = new PraeceptaSideCarDataHolder();
        String message = "# Details of a user\n" +
                "---\n" +
                "name: Test User\n" +
                "age: 30\n" +
                "address:\n" +
                "  line1: My Address Line 1\n" +
                "  line2: Address line 2\n" +
                "  city: Washington D.C.\n" +
                "  zip: 20000\n" +
                "roles:\n" +
                "  - User\n" +
                "  - Editor";

        holder.addInput(message);

        praeceptaYamlParser.parse(holder);

        Map<String,Object> output = holder.retriveOutput();
        assertNotNull(output);
        assertEquals("Test User", output.get("name"));
    }
}

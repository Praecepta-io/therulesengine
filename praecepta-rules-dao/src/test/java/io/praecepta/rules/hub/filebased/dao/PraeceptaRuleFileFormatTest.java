package io.praecepta.rules.hub.filebased.dao;

import org.junit.Test;

public class PraeceptaRuleFileFormatTest {

	@Test
	public void test() {
		String a1 = "hello1";
        String a2 = "hello2";
        Integer a3 = 333;

        String result = String.format("Test: %3$d, %1$s, %1$s, %2$s", a1, a2, a3);

        System.out.println(result);
        
        String spaceFileName = String.format("%1$s_%2$s_%3$s_%4$s", "TestSpaceName", "TestClientId-GTOICS", "TestAppName", "TestVersion");
        
        System.out.println(spaceFileName);
        
        String grpFileName = String.format("%1$s_%2$s_%3$s_%4$s_%5$s", "TestSpaceName", "TestClientId-GTOICS", "TestAppName", "TestVersion", "TestGrpName");
        
        System.out.println(grpFileName);

	
	}

}

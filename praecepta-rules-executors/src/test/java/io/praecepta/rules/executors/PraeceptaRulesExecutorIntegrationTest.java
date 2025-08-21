package io.praecepta.rules.executors;
import org.junit.BeforeClass;
import org.junit.Test;
import io.praecepta.rules.executor.PraeceptaRulesExecutorLauncher;

public class PraeceptaRulesExecutorIntegrationTest {

    @BeforeClass
    public static void setup() {
        System.setProperty("contextPath", "io.praecepta.rules.executor.inspira.spring.config.PraeceptaInspiraRulesExecutorTestConfig");
        System.setProperty("praecepta.rule.data.collector.props.location", "praecepta-inspira-kafka-data-collector.properties");
        System.setProperty("praecepta.rule.load.props.location", "praecepta-rule-load.properties");
        System.setProperty("SPACE_NAME", "INSPIRA");
        System.setProperty("CLIENT_ID", "ABC_BANK");
        System.setProperty("APP_NAME", "RETAIL_DIVISION");
        System.setProperty("VERSION", "V1");
        System.setProperty("RULE_GRP_NAME", "CREDIT_CHECK");
    }

    @Test
    public void testEndToEndExecution() throws Exception {
        PraeceptaRulesExecutorLauncher.main(new String[]{});
    }
}

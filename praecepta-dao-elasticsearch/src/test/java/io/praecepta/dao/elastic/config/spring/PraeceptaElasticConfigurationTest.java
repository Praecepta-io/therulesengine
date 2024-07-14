package io.praecepta.dao.elastic.config.spring;

import org.junit.Test;

import io.praecepta.dao.elastic.config.PraeceptaElasticConfiguration;

public class PraeceptaElasticConfigurationTest {

	@Test
	public void test() {
		
		PraeceptaElasticConfiguration esConfig = new PraeceptaElasticConfiguration();
		
		esConfig.setHostName("https://search-praecepta-eastic-dev-w7ocogmmjf2lupt4u545du3ubi.us-east-1.es.amazonaws.com:443");
		esConfig.setUseSSL(true);
		esConfig.setUseSubject(true);
		esConfig.setConnectionSubjectName("PraeceptaES");
		esConfig.setConnectionSubjectSupport("Praecep1@E$");
		
		
		esConfig.elasticsearchClient();
	}

}

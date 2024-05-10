package io.praecepta.rules.executors;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.praecepta.rules.executor.PraeceptaRulesExecutorLauncher;

public class RulesExecutorTest {
	//@Test
	public void test() {
		try {
			PraeceptaRulesExecutorLauncher.main(null);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}

package io.praecepta.rules.builders;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_SUB_TYPE;
import io.praecepta.rules.builders.PraeceptaRuleBuilder.RULE_SET_UP_PERSISTANT_TYPE;

public class PraeceptaRuleBuilderTest {

	@Test
	public void testBuildWithPropsClasspath() {
		
		PraeceptaRuleBuilder.buildWithPropsClasspath(null);
	}

	@Test
	public void testBuildWithPropsFileLocation() {
		
		PraeceptaRuleBuilder.buildWithPropsFileLocation("J:\\Source Code\\XploreTech\\praecepta-rules-setup\\src\\test\\resources\\praecepta-test-rule-load.properties");
	}
	
	@Test
	public void testBuildWithEnvParmPropsFile() {
		
		System.setProperty("praecepta.rule.load.props.location", "J:\\Source Code\\XploreTech\\praecepta-rules-setup\\src\\test\\resources\\praecepta-test-rule-load.properties");
		
		PraeceptaRuleBuilder.buildWithEnvParmPropsFile();
	}
	
	@Test
	public void testBuildWithProps() {
		
		Map<String, String> ruleLoadPropsInput = new HashMap<>();
		
		ruleLoadPropsInput.put("rule_loader_type", RULE_SET_UP_PERSISTANT_TYPE.SQL_DB.name());

		ruleLoadPropsInput.put(RULE_SET_UP_PERSISTANT_TYPE.SQL_DB.name() + ".type", RULE_SET_UP_PERSISTANT_SUB_TYPE.MySQL.name());
		
		ruleLoadPropsInput.put("SQL_DB.connection_props.MySQL.ip" , "0.0.0.01");
		
		PraeceptaRuleBuilder.buildWithDefaultProps().buildWithProps(ruleLoadPropsInput).build(null, null, null, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBuildWithPropsNegative() {
		
		Map<String, String> ruleLoadPropsInput = new HashMap<>();
		
		ruleLoadPropsInput.put("rule_loader_type", RULE_SET_UP_PERSISTANT_TYPE.SQL_DB.name());
		
		ruleLoadPropsInput.put(RULE_SET_UP_PERSISTANT_TYPE.SQL_DB.name() + ".type", "raja");
		
		ruleLoadPropsInput.put("SQL_DB.connection_props.MySQL.ip" , "0.0.0.01");
		
		PraeceptaRuleBuilder.buildWithDefaultProps().buildWithProps(ruleLoadPropsInput).build(null, null, null, null);
	}
	
}

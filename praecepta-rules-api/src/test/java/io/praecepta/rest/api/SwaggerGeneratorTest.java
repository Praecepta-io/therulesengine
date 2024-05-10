package io.praecepta.rest.api;

import static io.praecepta.rest.api.swagger.PraeceptaSwaggerGenerator.generateSwagger;

public class SwaggerGeneratorTest {
	
	public static void main(String[] args) {
		
		System.out.println(generateSwagger("io.praecepta.rest.api.controller.PraeceptaRuleSpaceController", true,"url1"));
		System.out.println(generateSwagger("io.praecepta.rest.api.controller.PraeceptaRuleGroupController", true,"url2"));
//		System.out.println(generateSwagger("io.praecepta.rest.api.controller.PraeceptaSidecarController", true,"url3"));
		
//		io.praecepta.rest.api.controller.PraeceptaRuleSpaceController
	}

}

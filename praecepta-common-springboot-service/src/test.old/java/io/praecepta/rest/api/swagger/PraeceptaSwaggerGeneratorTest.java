package io.praecepta.rest.api.swagger;

import static io.praecepta.rest.api.swagger.PraeceptaSwaggerGenerator.generateSwagger;

public class PraeceptaSwaggerGeneratorTest {

	public static void main(String[] args) {

		boolean jsonFormat = true;
		boolean yamlFormat = false;

//		System.out.println(generateSwagger(TestController.class.getName(), jsonFormat,"url"));
//		System.out.println(generateSwagger(TestController.class.getName(), yamlFormat,"url"));
//		System.out.println(generateSwagger(TestController.class.getName(), jsonFormat,"url"));
//		System.out.println(generateSwagger(TestController.class.getName(), yamlFormat,"url"));
		
		System.out.println(generateSwagger("io.praecepta.rest.api.controller.PraeceptaRuleSpaceController", jsonFormat,"url"));
		System.out.println(generateSwagger("io.praecepta.rest.api.controller.PraeceptaSidecarController", jsonFormat,"url"));
	}
}

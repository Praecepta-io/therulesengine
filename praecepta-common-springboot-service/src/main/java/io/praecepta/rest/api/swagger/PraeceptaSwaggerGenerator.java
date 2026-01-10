package io.praecepta.rest.api.swagger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.GenericOpenApiContextBuilder;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

public class PraeceptaSwaggerGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaSwaggerGenerator.class);

	enum SwaggerType {
		JSON, YAML
	}

	private static Map<String, Map<SwaggerType, String>> classNameToSwaggerMap = new HashMap<>();

	public static String generateSwagger(Class<?> controllerClazz, boolean isTheJsonFormatNeeded, String url) {

		LOG.info(" Generating Swagger for Class : {}", controllerClazz.getName());

		return generateSwagger(controllerClazz.getName(), isTheJsonFormatNeeded, url);
	}

	public static String generateSwagger(String controllerClazzName, boolean isTheJsonFormatNeeded, String url) {

		LOG.info(" Swagger Generation Request for Class Name : {} with isTheJsonFormatNeeded flag as {} ",
				controllerClazzName, isTheJsonFormatNeeded);
		String openapiFormatOutput = "";

		Map<SwaggerType, String> swaggerTypeToOutputStringMap = null;

		if (classNameToSwaggerMap.containsKey(controllerClazzName)) {
			swaggerTypeToOutputStringMap = classNameToSwaggerMap.get(controllerClazzName);
		}

		if (swaggerTypeToOutputStringMap == null) {

			swaggerTypeToOutputStringMap = new HashMap<>();

		} else {

			if (isTheJsonFormatNeeded) {

				openapiFormatOutput = swaggerTypeToOutputStringMap.get(SwaggerType.JSON);

			} else {

				openapiFormatOutput = swaggerTypeToOutputStringMap.get(SwaggerType.YAML);
			}

			if (!PraeceptaObjectHelper.isStringEmpty(openapiFormatOutput)) {

				LOG.info(" Returning the Swagger From Local Cache or Map");
				return openapiFormatOutput;
			}
		}

		LOG.info(" Swagger Outout not present in the Local Cache. It's ready to build");
		SwaggerConfiguration config = new SwaggerConfiguration();

		Set<String> classess = new HashSet<>();
		classess.add(controllerClazzName);

		config.setResourceClasses(classess);
		config.setPrettyPrint(true);

		GenericOpenApiContextBuilder builder = new JaxrsOpenApiContextBuilder().openApiConfiguration(config);

		try {
			OpenApiContext context = builder.buildContext(true);
			OpenAPI openAPI = context.read();
			
			Info openApiInfo = new Info();
//			openApiInfo.setTitle("Percepta Rules Engine");
			openApiInfo.setTitle("Percepta Rules Engine API Defintion for "+url);
			openApiInfo.version("1.0");
			
			openAPI.setInfo(openApiInfo);
			
			List<Server> servers = new ArrayList<>();
			Server server = new Server();
			server.setUrl(url);
			 servers.add(server);
			openAPI.setServers(servers);
			if (isTheJsonFormatNeeded) {
				if (config.isPrettyPrint() != null && config.isPrettyPrint()) {
					openapiFormatOutput = context.getOutputJsonMapper().writer(new DefaultPrettyPrinter())
							.writeValueAsString(openAPI);
				} else {
					openapiFormatOutput = context.getOutputJsonMapper().writeValueAsString(openAPI);
				}

				swaggerTypeToOutputStringMap.put(SwaggerType.JSON, openapiFormatOutput);
			} else {
				if (config.isPrettyPrint() != null && config.isPrettyPrint()) {
					openapiFormatOutput = context.getOutputYamlMapper().writer(new DefaultPrettyPrinter())
							.writeValueAsString(openAPI);
				} else {
					openapiFormatOutput = context.getOutputYamlMapper().writeValueAsString(openAPI);
				}
				swaggerTypeToOutputStringMap.put(SwaggerType.YAML, openapiFormatOutput);
			}
		} catch (Exception e) {
			LOG.error(" Exception while Generating Swagger for Class Name : {} with isTheJsonFormatNeeded flag as {} ",
					controllerClazzName, isTheJsonFormatNeeded);
		}

		classNameToSwaggerMap.put(controllerClazzName, swaggerTypeToOutputStringMap);

		LOG.info(" Swagger Output Generated Class Name : {} with isTheJsonFormatNeeded flag as {} ",
				controllerClazzName, isTheJsonFormatNeeded);
		
		LOG.info(" Here is the Swagger Output in Pretty Format -->  {}", openapiFormatOutput);

		return openapiFormatOutput;
	}

}

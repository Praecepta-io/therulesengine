package io.praecepta.rest.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.data.PraeceptaRequestStore;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestService;
import io.praecepta.rest.api.annotation.PraeceptaExposeAsRestServiceMethod;
import io.praecepta.rest.api.service.IPraeceptaApiService;
import io.praecepta.rest.constants.TestServiceAndMethodNames;
import io.praecepta.rest.enums.PraeceptaWsRequestStoreType;
import io.swagger.v3.oas.annotations.Operation;

@Path("/myTestController")
@PraeceptaExposeAsRestService(serviceName = TestServiceAndMethodNames.TEST_CONTROLLER_NAME, servicePath = "/myTestController")
public class TestController implements IPraeceptaApiService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
			
	@Operation(operationId = "testGet")
    @GET
	@PraeceptaExposeAsRestServiceMethod(get = true, functionPath = "/testGet/:username/:age", methodName = TestServiceAndMethodNames.TEST_GET_METHOD_NAME)
	@Produces("application/json")
	@Path("/testGet/{username}/{age}")
	public TestJson testGet(@PathParam(value = "username") String username, @PathParam(value = "age")  Integer age) {
		
		System.out.println("Path Params received --> userName - "+username + " .., age - "+age);
		
		TestJson getOutput = new TestJson();
		
		getOutput.setUserName(username);
		getOutput.setAge(age);
		
		System.out.println("JSON on Server --> "+getOutput);
		
		return getOutput;
	}
	
	public void blankMethod() {
		
	}
	
	@PraeceptaExposeAsRestServiceMethod(post = true, functionPath = "/testPost/:appname/:rulegroupname", methodName = TestServiceAndMethodNames.TEST_POST_METHOD_NAME)
	@Operation(operationId = TestServiceAndMethodNames.TEST_POST_METHOD_NAME)
    @POST
    @Consumes("application/json")
	@Produces("application/json")
    @Path("/testPost/{appname}/{rulegroupname}")
	public String testPraecepta(@PathParam(value = "appname")String appName, @PathParam(value = "rulegroupname") String rulegroupname, TestJson testInput) {
		
		Map<String, Object> outputMap = new HashMap<>();
		
		outputMap.put("appname", appName);
		outputMap.put("rulegroupname", rulegroupname);
		outputMap.put("userName", testInput.getUserName());
		outputMap.put("age", testInput.getAge());
		
		return GsonHelper.toJson(outputMap);
	}
	
	@PraeceptaExposeAsRestServiceMethod(post = true, functionPath = "/testPostWithoutQueryParams", methodName = TestServiceAndMethodNames.TEST_POST_METHOD_WITHOUT_QUERY_PARAMA_NAME)
	@Operation(operationId = TestServiceAndMethodNames.TEST_POST_METHOD_WITHOUT_QUERY_PARAMA_NAME)
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/testPostWithoutQueryParams")
	public TestJson tesPosttPraecepta(TestJson testInput) {
		
		LOG.info(" Test Inout -> {} ", GsonHelper.toJson(testInput));
		return testInput;
	}

	@Override
	public void execute(PraeceptaRequestStore requestStore) {
		String operation = (String) requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_OPERATION);
		
		LOG.info("Operation Name: {}", operation);
		
		Map<String, Object> pathParams = (Map<String, Object>) requestStore
				.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT_PATH_PARAMS);
		
		LOG.info("Path Params: {}", pathParams);
		
		switch (operation) {
		case TestServiceAndMethodNames.TEST_GET_METHOD_NAME:
			LOG.info("Here in Test Get");
			
			Object userNameObj = pathParams.get(":username");
			Object ageObj = pathParams.get(":age");
			
			String userName = PraeceptaObjectHelper.isObjectEmpty(userNameObj) ?  "" : userNameObj.toString();
			Integer age = PraeceptaObjectHelper.isObjectEmpty(ageObj) ?  0 : Integer.valueOf(ageObj.toString());
			
			TestJson getOutput = testGet(userName, age);
			
			requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT, GsonHelper.toJson(getOutput));
			break;
	
		case TestServiceAndMethodNames.TEST_POST_METHOD_NAME:
			LOG.info("Here in Test Post");
			
			TestJson input = GsonHelper.toEntity((String)requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT), TestJson.class);
			
			Object appNameObj = pathParams.get(":appname");
			Object ruleGroupNameObj = pathParams.get(":rulegroupname");
			
			String appName = PraeceptaObjectHelper.isObjectEmpty(appNameObj) ?  "" : appNameObj.toString();
			String ruleGroupName = PraeceptaObjectHelper.isObjectEmpty(ruleGroupNameObj) ?  "" : ruleGroupNameObj.toString();
			
			String outputJson = testPraecepta(appName, ruleGroupName, input);
			
			requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT, outputJson);
			break;
			
		case TestServiceAndMethodNames.TEST_POST_METHOD_WITHOUT_QUERY_PARAMA_NAME:
			LOG.info("Here in Test Post Without Query Param");
			
			TestJson testInput = GsonHelper.toEntity((String)requestStore.fetchFromPraeceptaStore(PraeceptaWsRequestStoreType.WS_INPUT), TestJson.class);
			
			TestJson testOutput =tesPosttPraecepta(testInput);
			
			requestStore.upsertToPraeceptaStore(PraeceptaWsRequestStoreType.WS_OUTPUT, GsonHelper.toJson(testOutput));
			break;
		}
	}
	
	class TestJson{
		
		private String userName;
		
		private Integer age;

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		@Override
		public String toString() {
			return "TestJson [userName=" + userName + ", age=" + age + "]";
		}
		
		
	}

}

package io.praecepta.data.injestors.common.impl;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord.RecordEntry;
import io.praecepta.data.configs.common.IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE;
import io.praecepta.data.configs.common.db.PraeceptaDBDataConfigType;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;

public class PraeceptaDBDataInjestorTest {

	PraeceptaDBInjestorConfig dbConfig = null;

	PraeceptaDBDataInjestor dataInjestorMock = null;

	PraeceptaDataRecord dataRecord = null;

	@Before
	public void init() {

		dataInjestorMock = mock(PraeceptaDBDataInjestor.class);

		dbConfig = new PraeceptaDBInjestorConfig();

		dbConfig.addConfigElement(PraeceptaDBDataConfigType.DB_DRIVER, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"com.mysql.jdbc.Driver");
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.DB_URL, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"jdbc:mysql://127.0.0.1:3306/praecepta?autoReconnect=true");
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"root");
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"root");

		dbConfig.addNonMandatoryConfigElements(PraeceptaDBDataConfigType.INSERT_QUERY.getElementName(),
				"INSERT INTO `praecepta`.`rule_response_info`(`RULE_GROUP_ID`,`RESPONSE`,`RESPONSE_METADATA`,`CREATED_DATE`)VALUES(?,?,?,NOW())");

		dataRecord = new PraeceptaDataRecord(1);

		String rulesResponse = "{\"attribute1\":\"val1\"}";

		Map<String, Object> responseMetaData = new HashMap<>();

		responseMetaData.put("TEST", GsonHelper.toEntity("{\"status\":\"success\"}", Map.class));

		dataRecord.addRecordEntry(rulesResponse, "TEST1", responseMetaData);

	}

	@Test
	public void testOpenInjestorConnection() {

		dataInjestorMock.openInjestorConnection(dbConfig);

		assertNotEquals(dataInjestorMock.getInjestorStatus(), CONNECTION_STATUS.INITIALIZED);

		dataInjestorMock.terminateDataInjestor();

	}

	@Test
	public void testInitialize() throws Exception {

		PraeceptaDBDataInjestor spyObj = spy(PraeceptaDBDataInjestor.class);

		doNothing().when((PraeceptaAbstractDataInjestor) spyObj).initializeDataInjestor();

		DataSource dataSource = mock(DriverManagerDataSource.class);

		doReturn(dataSource).when(spyObj).initializeDataSource(new HashMap<>());

		doNothing().when(spyObj).initialize(dbConfig);

		spyObj.initialize(dbConfig);
	}

	@Test
	public void testInjestData() throws Exception {

		PraeceptaDBDataInjestor spyObj = spy(PraeceptaDBDataInjestor.class);

		doNothing().when((PraeceptaAbstractDataInjestor) spyObj).openInjestorConnection(dbConfig);

		testOpenInjestorConnection();

		doNothing().when((PraeceptaAbstractDataInjestor) spyObj).injestData(dataRecord);

		dataInjestorMock.injestData(dataRecord);

		dataInjestorMock.terminateDataInjestor();

	}

	@Test
	public void testPerformInjestData() throws Exception {
		
		String query="INSERT INTO `praecepta`.`rule_group_status_info` ( `RULE_GROUP_ID`, `RULE_GROUP_NAME`, `RULE_SPACE_NAME`, `APP_NAME`, `CLIENT_NAME`, `VERSION`, `RESPONSE`, `RESPONSE_METADATA`, `RULE_GROUP_EXECUTION_STATUS`, `CREATED_DATE`) VALUES (?, ?, ?, ?,?,?, ?, ?, ?, NOW());";

		DataSource dataSource = mock(DriverManagerDataSource.class);
		
		JdbcTemplate template = mock(JdbcTemplate.class);
		
		PreparedStatement stmtObj=mock(PreparedStatement.class);
		
		
		doNothing().when(stmtObj).setString(any(int.class), any(String.class));
		
		doReturn(1).when(stmtObj).executeUpdate();

		Field field2 = PraeceptaDBDataInjestor.class.getDeclaredField("dataSource");
		field2.setAccessible(true);
		field2.set(dataInjestorMock, dataSource);
		
		Field field1 = PraeceptaDBDataInjestor.class.getDeclaredField("QUERY_TO_INSERT");
		field1.setAccessible(true);
		field1.set(dataInjestorMock, query);
		
		Field field3 = PraeceptaDBDataInjestor.class.getDeclaredField("objJdbcTemplate");
		field3.setAccessible(true);
		field3.set(dataInjestorMock, template);

		Method privateMethod = PraeceptaDBDataInjestor.class.getDeclaredMethod("performInjestData", RecordEntry.class);

		privateMethod.setAccessible(true);

		privateMethod.invoke(dataInjestorMock, dataRecord.getRecordEntries().getFirst());

	}

	// @Test
	public void testInjestDataEndToEnd() throws Exception {
		PraeceptaDBDataInjestor dataInjestor = new PraeceptaDBDataInjestor();

		dataInjestor.openInjestorConnection(dbConfig);

		PraeceptaDataRecord dataRecord = new PraeceptaDataRecord(1);

		String rulesResponse = "{\"attribute1\":\"val1\"}";

		Map<String, Object> responseMetaData = new HashMap<>();

		responseMetaData.put("TEST", GsonHelper.toEntity("{\"status\":\"success\"}", Map.class));

		dataRecord.addRecordEntry(rulesResponse, "TEST1", responseMetaData);

		dataInjestor.injestData(dataRecord);

		dataInjestor.terminateDataInjestor();

	}

}

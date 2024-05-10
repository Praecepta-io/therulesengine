package io.praecepta.data.injestors.common.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord.RecordEntry;
import io.praecepta.data.configs.common.GenericAbstractPraeceptaDataConfig;
import io.praecepta.data.configs.common.db.PraeceptaDBDataConfigType;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;
import io.praecepta.data.injestor.common.exception.PraeceptaDataInjestorException;
import io.praecepta.data.injestors.common.enums.RULE_GROUP_INFO_KEYS;

public class PraeceptaDBDataInjestor<DB_CONFIG extends GenericAbstractPraeceptaDataConfig>
		extends PraeceptaAbstractDataInjestor<DB_CONFIG> {

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaDBDataInjestor.class);

	private DataSource dataSource;

	private JdbcTemplate objJdbcTemplate;

	private String QUERY_TO_INSERT;
	private String DEFAULT_QUERY="INSERT INTO `praecepta`.`rule_group_status_info` ( `RULE_GROUP_ID`, `RULE_GROUP_NAME`, `RULE_SPACE_NAME`, `APP_NAME`, `CLIENT_NAME`, `VERSION`, `RESPONSE`, `RESPONSE_METADATA`, `RULE_GROUP_EXECUTION_STATUS`, `CREATED_DATE`) VALUES (?, ?, ?, ?,?,?, ?, ?, ?, NOW())";


	@Override
	public void openInjestorConnection(DB_CONFIG dbInjestorConfig) {

		LOG.info("Before  establishing  connection for DB data injestor");

		super.openInjestorConnection(dbInjestorConfig);

		try {
			initialize(dbInjestorConfig);
		} catch (Exception e) {
			LOG.error("error while establishing db connection");
		}
	}

	protected void initialize(DB_CONFIG dbInjestorConfig) throws Exception {

		LOG.info("Before initializing DB data injestor");

		super.initializeDataInjestor();

		Map<String, String> dbConfigPropertis = dbInjestorConfig.getDataConfigurationWithValues();

		if (!PraeceptaObjectHelper.isObjectEmpty(dbConfigPropertis)) {

			try {
				dataSource = initializeDataSource(dbConfigPropertis);

				QUERY_TO_INSERT = dbConfigPropertis.get(PraeceptaDBDataConfigType.INSERT_QUERY.getElementName());

			} catch (Exception e) {
				LOG.error("error while initializing DataSource", e);
				throw e;
			}

		} else {
			LOG.info("DB data injestor config properties found empty/null");
		}

		LOG.info("Done initializing DB data injestor");
		System.out.println("Done initializing DB data injestor" + dataSource);
	}

	@Override
	public void injestData(PraeceptaDataRecord dataRecord) {

		LOG.info("start processing data injest");

		if (dataRecord == null) {
			throw new PraeceptaDataInjestorException("Data Record object found null");
		}

		if (getInjestorStatus() == null || getInjestorStatus() == CONNECTION_STATUS.INITIALIZED) {
			throw new PraeceptaDataInjestorException(
					"Perform InjestData should be called only after initializing the snder");
		}

		if (PraeceptaObjectHelper.isObjectNull(dataSource)) {
			throw new PraeceptaDataInjestorException("Datasource should be initialized to perform InjestData");
		}

		if (PraeceptaObjectHelper.isObjectEmpty(getQuery())) {
			LOG.info("Insert query from action configuration found null/empty,hence using default query");
			QUERY_TO_INSERT=DEFAULT_QUERY;
		}

		LinkedBlockingDeque<RecordEntry> recordEntries = dataRecord.getRecordEntries();

		while (recordEntries != null && !PraeceptaObjectHelper.isObjectEmpty(recordEntries)) {

			RecordEntry recordDataObj = recordEntries.poll();

			LOG.info("Before injesting data ");

			try {
				performInjestData(recordDataObj);
			} catch (Exception e) {
				LOG.error("error while inserting data to table");
			}
			LOG.info("Done injesting data ");
		}
	}

	@Override
	public void terminateDataInjestor() {

		if (!PraeceptaObjectHelper.isObjectNull(dataSource)) {
			try {
				((DriverManagerDataSource) dataSource).getConnection().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	protected DriverManagerDataSource initializeDataSource(Map<String, String> dbConfigPropertis) throws Exception {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(dbConfigPropertis.get(PraeceptaDBDataConfigType.DB_DRIVER.getElementName()));
		dataSource.setUrl(dbConfigPropertis.get(PraeceptaDBDataConfigType.DB_URL.getElementName()));
		dataSource.setUsername(dbConfigPropertis.get(PraeceptaDBDataConfigType.USERNAME.getElementName()));
		dataSource.setPassword(dbConfigPropertis.get(PraeceptaDBDataConfigType.PASSWORD.getElementName()));
		if (!PraeceptaObjectHelper.isObjectNull(dataSource)) {
			objJdbcTemplate = initializeJdbcTemplate(dataSource);
		}
		return dataSource;
	}

	public JdbcTemplate initializeJdbcTemplate(DriverManagerDataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		return jdbcTemplate;
	}
	

	private void performInjestData(RecordEntry recordDataObj) throws SQLException {
		LOG.info("Before inserting data to db");
		
		
		Map<String,Object> responseMetaData=recordDataObj.getMetaData();
	
		objJdbcTemplate.update(getQuery(), new PreparedStatementSetter() {
		      public void setValues(PreparedStatement ps) throws SQLException {
		        ps.setString(1, recordDataObj.getMessageTextId());
		        ps.setString(2, (String)responseMetaData.get(RULE_GROUP_INFO_KEYS.RULE_GROUP_NAME.name()));
		        ps.setString(3, (String)responseMetaData.get(RULE_GROUP_INFO_KEYS.SPACE_NAME.name()));
		        ps.setString(4, (String)responseMetaData.get(RULE_GROUP_INFO_KEYS.APP_NAME.name()));
		        ps.setString(5, (String)responseMetaData.get(RULE_GROUP_INFO_KEYS.CLIENT_NAME.name()));
		        ps.setString(6, (String)responseMetaData.get(RULE_GROUP_INFO_KEYS.VERSION.name()));
		        ps.setString(7, recordDataObj.getMessageText());
		        ps.setObject(8, GsonHelper.toJson(responseMetaData));
		        ps.setObject(9, responseMetaData.get(RULE_GROUP_INFO_KEYS.RULE_GROUP_EXECUTION_STATUS.name()));
		      }
		    });

	}

	private String getQuery() {

		return this.QUERY_TO_INSERT;
	}

	public JdbcTemplate getJdbcTemplate() {
		return objJdbcTemplate;
	}

}

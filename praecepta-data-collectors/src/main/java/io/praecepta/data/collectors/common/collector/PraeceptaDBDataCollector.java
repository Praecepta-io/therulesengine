package io.praecepta.data.collectors.common.collector;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.collectors.common.exception.PraeceptaDataCollectorException;
import io.praecepta.data.configs.common.db.PraeceptaDBDataConfigType;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;

public class PraeceptaDBDataCollector extends PraeceptaAbstractDataCollector<PraeceptaDBInjestorConfig>{
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaDBDataCollector.class);

	private DataSource dataSource;

	private JdbcTemplate objJdbcTemplate;
	
	private String queryToExecute;
	
	@Override
	public void openCollectorConnection(PraeceptaDBInjestorConfig dbConfig) {
		super.openCollectorConnection(dbConfig);
		
		initializeDBConnection(dbConfig);
	}

	private void initializeDBConnection(PraeceptaDBInjestorConfig dbConfig) {
		
		Map<String, String> dbConfigPropertis = dbConfig.getDataConfigurationWithValues();

		if (!PraeceptaObjectHelper.isObjectEmpty(dbConfigPropertis)) {

			try {
				dataSource = initializeDataSource(dbConfigPropertis);

				queryToExecute = dbConfigPropertis.get(PraeceptaDBDataConfigType.SELECT_QUERY.getElementName());

			} catch (Exception e) {
				LOG.error("error while initializing DataSource", e);
				throw new RuntimeException(e);
			}

		} else {
			LOG.info("DB data injestor config properties found empty/null");
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
	
	@Override
	protected PraeceptaDataRecord performCollection() {
		// This method cannot be called directly. Must call startDataCollector(). Underlying will call this method to poll individual Collection 
		LOG.info("Inside DB Fetch performCollection ");
		
		if(getCollectorStatus() == null || getCollectorStatus() == CONNECTION_STATUS.INITIALIZED) {
			throw new PraeceptaDataCollectorException("Perform Collector should be called only after Starting the Data Collector");
		}
		
		List<Map<String, Object>> resultSetRowsFromDB =  objJdbcTemplate.queryForList(queryToExecute);
		
		LOG.info("After JDBC Template Query Execution is Done ");

		if (!PraeceptaObjectHelper.isObjectEmpty(resultSetRowsFromDB)) {
			
			PraeceptaDataRecord dataRecord = new PraeceptaDataRecord(resultSetRowsFromDB.size());
			resultSetRowsFromDB.forEach(resultSetRecord -> {
				dataRecord.addRecordEntry(GsonHelper.fromMapToJsonPerseveNumber(resultSetRecord), null, null);

			});
			return dataRecord;
		}
		return null;
	}

	@Override
	public void terminateDataCollector() {
		
		if (!PraeceptaObjectHelper.isObjectNull(dataSource)) {
			try {
				((DriverManagerDataSource) dataSource).getConnection().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.terminateDataCollector();
		
	}
	

	
}

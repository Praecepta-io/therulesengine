package io.praecepta.data.injestors.common.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.praecepta.core.data.PraeceptaDictionaryData;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.configs.common.db.PraeceptaDBDataConfigType;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;
import io.praecepta.data.injestor.common.exception.PraeceptaDataInjestorException;

public class PraeceptaDBDataInjestor
		extends PraeceptaAbstractDataInjestor<PraeceptaDBInjestorConfig, Collection<PraeceptaDictionaryData>> {

	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaDBDataInjestor.class);

	private DataSource dataSource;

	private JdbcTemplate objJdbcTemplate;

	private String insertQuery;

	private String attributeNamesToInsert;

	@Override
	public void openInjestorConnection(PraeceptaDBInjestorConfig dbInjestorConfig) {

		LOG.info("Before  establishing  connection for DB data injestor");

		super.openInjestorConnection(dbInjestorConfig);

		try {
			initialize(dbInjestorConfig);
		} catch (Exception e) {
			LOG.error("error while establishing db connection");
		}
	}

	protected void initialize(PraeceptaDBInjestorConfig dbInjestorConfig) throws Exception {

		LOG.info("Before initializing DB data injestor");

		super.initializeDataInjestor();

		Map<String, String> dbConfigPropertis = dbInjestorConfig.getDataConfigurationWithValues();

		if (!PraeceptaObjectHelper.isObjectEmpty(dbConfigPropertis)) {

			try {
				dataSource = initializeDataSource(dbConfigPropertis);

				insertQuery = dbConfigPropertis.get(PraeceptaDBDataConfigType.INSERT_QUERY.getElementName());
				
				attributeNamesToInsert = dbConfigPropertis.get(PraeceptaDBDataConfigType.INSERT_ATTRIBUTE_NAMES.getElementName());
				
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
	public void injestData(Collection<PraeceptaDictionaryData> dataRecordsToInsert) {

		LOG.info("start processing data injest");

		if (PraeceptaObjectHelper.isObjectNull(dataRecordsToInsert)) {
			throw new PraeceptaDataInjestorException("There are no Data Records to insert");
		} else {
			
			LOG.debug("Number of records to Insert --> {}", dataRecordsToInsert.size());
		}

		if (getInjestorStatus() == null || getInjestorStatus() == CONNECTION_STATUS.INITIALIZED) {
			throw new PraeceptaDataInjestorException(
					"Perform InjestData should be called only after initializing the snder");
		}

		if (PraeceptaObjectHelper.isObjectNull(dataSource)) {
			throw new PraeceptaDataInjestorException("Datasource should be initialized to perform InjestData");
		}

		if (PraeceptaObjectHelper.isObjectEmpty(insertQuery)) {
			LOG.warn("Insert query from action configuration found null/empty,hence no insertion required");
			return;
		} else {
			LOG.debug("Insert query to use --> {}", insertQuery);
		}
		
		if (PraeceptaObjectHelper.isObjectEmpty(attributeNamesToInsert)) {
			LOG.warn("Insert Attribute names are not provided, insertion cannot be performed.");
			return;
		} else {
			LOG.debug("Columns Names to use --> {}", attributeNamesToInsert);
		}
		
		String[] columnNames = attributeNamesToInsert.split("\\|");
		
		if(columnNames.length > 0) {

			try {
				performInjestData(columnNames, dataRecordsToInsert);
			} catch (Exception e) {
				LOG.error("error while inserting data to table ", e);
			}
		}

	}

	@Override
	public void terminateDataInjestor() {
		LOG.info(" Initiated DB Injecstor Termination ");

		if (!PraeceptaObjectHelper.isObjectNull(dataSource)) {
			try {
				( dataSource).getConnection().close();
			} catch (Exception e) {
				LOG.error("Database Connection Close has an error  ", e);
			}
		}
		super.terminateDataInjestor();

	}

	protected DataSource initializeDataSource(Map<String, String> dbConfigPropertis) throws Exception {

		HikariConfig config = new HikariConfig();
		
		config.setJdbcUrl(dbConfigPropertis.get(PraeceptaDBDataConfigType.DB_URL.getElementName())); // Change as per your Oracle setup
        config.setUsername(dbConfigPropertis.get(PraeceptaDBDataConfigType.USERNAME.getElementName()));
        config.setPassword(dbConfigPropertis.get(PraeceptaDBDataConfigType.PASSWORD.getElementName()));
        config.setDriverClassName(dbConfigPropertis.get(PraeceptaDBDataConfigType.DB_DRIVER.getElementName()));
        
     // Optional tuning
        config.setMaximumPoolSize(100);
        config.setMinimumIdle(5);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);
        config.setPoolName("OracleHikariCP");
        
        DataSource dataSource = new HikariDataSource(config);
        
		if (!PraeceptaObjectHelper.isObjectNull(dataSource)) {
			objJdbcTemplate = initializeJdbcTemplate(dataSource);
		}
		return dataSource;
	}

	public JdbcTemplate initializeJdbcTemplate(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		return jdbcTemplate;
	}
	
	protected void performInjestData(String[] columnNames, Collection<PraeceptaDictionaryData> dataRecordsToInsert) throws SQLException {
		
		LOG.debug(" Inserting the records Size ->"+dataRecordsToInsert.size());
		
		List<Object[]> batchArgs = new ArrayList<>();
		
		dataRecordsToInsert.stream().forEach( eachDictionaryData -> {
			
			List<Object> oneRowData = new ArrayList<>();
			
			for(int i=0; i < columnNames.length; i++) {
				oneRowData.add(eachDictionaryData.getDictionaryDetails().get(columnNames[i]));
			}
			 
			 batchArgs.add(oneRowData.toArray());
		});
		
		LOG.debug(" Batch Args "+batchArgs);
		int[] updateCounts = objJdbcTemplate.batchUpdate(insertQuery, batchArgs);
		
		LOG.debug(" Number of Records Inserted - {} ", updateCounts.length);
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return objJdbcTemplate;
	}

}

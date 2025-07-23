package io.praecepta.data.collectors.common.collector;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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
	
	private String selctQueryProvided;
	
	private AtomicInteger currentOffset = new AtomicInteger(0);
	
	private Object obj = new Object();
	
	private int chunkSize = 500;
	
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

				selctQueryProvided = dbConfigPropertis.get(PraeceptaDBDataConfigType.SELECT_QUERY.getElementName());

			} catch (Exception e) {
				LOG.error("error while initializing DataSource", e);
				throw new RuntimeException(e);
			}

		} else {
			LOG.info("DB data injestor config properties found empty/null");
		}
		
	}
	
	protected DataSource initializeDataSource(Map<String, String> dbConfigPropertis) throws Exception {

		
		HikariConfig config = new HikariConfig();
		
		config.setJdbcUrl(dbConfigPropertis.get(PraeceptaDBDataConfigType.DB_URL.getElementName())); // Change as per your Oracle setup
        config.setUsername(dbConfigPropertis.get(PraeceptaDBDataConfigType.USERNAME.getElementName()));
        config.setPassword(dbConfigPropertis.get(PraeceptaDBDataConfigType.PASSWORD.getElementName()));
        config.setDriverClassName(dbConfigPropertis.get(PraeceptaDBDataConfigType.DB_DRIVER.getElementName()));
        
     // Optional tuning
        config.setMaximumPoolSize(10);
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
		LOG.info(" initializing the Jdbc Template"); 
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
		
		synchronized (obj) {
		
			String queryToExecute = new StringBuilder().append(selctQueryProvided).append(" OFFSET ").append( currentOffset ).append(" ROWS FETCH NEXT ").append( chunkSize).append( " ROWS ONLY").toString();
			
			List<Map<String, Object>> resultSetRowsFromDB =  objJdbcTemplate.queryForList(queryToExecute);
			
			LOG.info("After JDBC Template Query Execution is Done ");
	
			if (!PraeceptaObjectHelper.isObjectEmpty(resultSetRowsFromDB)) {
				
				PraeceptaDataRecord dataRecord = new PraeceptaDataRecord(resultSetRowsFromDB.size());
				resultSetRowsFromDB.forEach(resultSetRecord -> {
					dataRecord.addRecordEntry(GsonHelper.fromMapToJsonPerseveNumber(resultSetRecord), null, null);
	
				});
				
				currentOffset.addAndGet(chunkSize);
				
				return dataRecord;
			}
		}
		return null;
	}

	@Override
	public void terminateDataCollector() {
		
		if (!PraeceptaObjectHelper.isObjectNull(dataSource)) {
			try {
				( dataSource).getConnection().close();
			} catch (Exception e) {
				LOG.error("Database Connection Close has an error  ", e);
			}
		}
		super.terminateDataCollector();
		
	}
	
}

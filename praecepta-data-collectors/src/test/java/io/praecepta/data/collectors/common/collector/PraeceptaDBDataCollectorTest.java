package io.praecepta.data.collectors.common.collector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Test;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.configs.common.IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE;
import io.praecepta.data.configs.common.db.PraeceptaDBDataConfigType;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;

public class PraeceptaDBDataCollectorTest {

	@Test
	public void test() {
		
		PraeceptaDBDataCollector dbCollector = new PraeceptaDBDataCollector();
		
		PraeceptaDBInjestorConfig dbConfig = new PraeceptaDBInjestorConfig();
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.DB_DRIVER, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"com.mysql.jdbc.Driver");
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.DB_URL, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"jdbc:mysql://127.0.0.1:3306/praecepta?autoReconnect=true&useSSL=false");
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"root");
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"*****");
		
		dbConfig.addNonMandatoryConfigElements(PraeceptaDBDataConfigType.SELECT_QUERY.getElementName(), COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"SELECT * FROM praecepta.rule_space_info;");
		
		dbCollector.openCollectorConnection(dbConfig);
		
		dbCollector.startDataCollector();
		
//		PraeceptaDataRecord record = dbCollector.performCollection();
		
//		System.out.println(record.getRecordEntries().toString());
		
		
		assertEquals(dbCollector.getCollectorStatus(), CONNECTION_STATUS.COLLECTOR_STARTED);
		
		int counter = 0;
		
		while(dbCollector.isDataCollectable()) {
			System.out.println("Here in Data Collectible");
			
			Collection<PraeceptaDataRecord> recordsPolled = dbCollector.getCollectedData();
			
			if (!PraeceptaObjectHelper.isObjectEmpty(recordsPolled)) {
				
				recordsPolled.stream().forEach(eachRecord ->{
					System.out.println("Each DB Record --> "+eachRecord.getRecordEntries().toString());
				});
			}
			counter++;
			
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(counter == 5) {
				dbCollector.terminateDataCollector();
			}
		}
		
	}

}

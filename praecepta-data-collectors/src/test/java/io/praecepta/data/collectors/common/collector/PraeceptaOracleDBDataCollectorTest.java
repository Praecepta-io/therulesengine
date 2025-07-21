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

public class PraeceptaOracleDBDataCollectorTest {

	@Test
	public void test() {
		
		PraeceptaDBDataCollector dbCollector = new PraeceptaDBDataCollector();
		
		PraeceptaDBInjestorConfig dbConfig = new PraeceptaDBInjestorConfig();
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.DB_DRIVER, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"oracle.jdbc.OracleDriver");
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.DB_URL, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"jdbc:oracle:thin:@//localhost:1521/ORCL");
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"<UserName>");
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"<Pass>");
		
		dbConfig.addNonMandatoryConfigElements(PraeceptaDBDataConfigType.SELECT_QUERY.getElementName(), COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"select Age, \r\n" + 
				"         CASE \r\n" + 
				"            WHEN Has_Salary_account = 'Yes' THEN 'Y'\r\n" + 
				"            WHEN Has_Salary_account = 'No' THEN 'N'\r\n" + 
				"            ELSE NULL\r\n" + 
				"        END  hasSalaryAccount, \r\n" + 
				"        CASE \r\n" + 
				"            WHEN Eligible_for_Quick_Credit = 'Yes' THEN 'Y'\r\n" + 
				"            WHEN Eligible_for_Quick_Credit = 'No' THEN 'N'\r\n" + 
				"            ELSE NULL\r\n" + 
				"        END  eligibleForQuickCredit, \r\n" + 
				"        CASE \r\n" + 
				"            WHEN Active_fixed_deposit = 'Yes' THEN 'Y'\r\n" + 
				"            WHEN Active_fixed_deposit = 'No' THEN 'N'\r\n" + 
				"            ELSE NULL\r\n" + 
				"        END   activeFixedDepositAcc, \r\n" + 
				"        Average_balanace_6_months average12MonthsBal, \r\n" + 
				"        Turnover_12_months turnOver12Mon   from inspira_demo");
		
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

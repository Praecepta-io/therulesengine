package io.praecepta.data.injestors.common.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import io.praecepta.core.data.PraeceptaDictionaryData;
import io.praecepta.data.configs.common.IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE;
import io.praecepta.data.configs.common.db.PraeceptaDBDataConfigType;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;

public class PraeceptaOracleDBDataInjectorTest {
	
	@Test
	public void test() {
		
		PraeceptaDBDataInjestor dbInjector = new PraeceptaDBDataInjestor();
		
		PraeceptaDBInjestorConfig dbConfig = new PraeceptaDBInjestorConfig();
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.DB_DRIVER, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"oracle.jdbc.OracleDriver");
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.DB_URL, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"jdbc:oracle:thin:@//praecepta-inspira-oracle-db.c4x0qa0se8vg.us-east-1.rds.amazonaws.com:1521/ORCL");
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"<USerName>");
		dbConfig.addConfigElement(PraeceptaDBDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"<Pass>");
		
		dbConfig.addNonMandatoryConfigElements(PraeceptaDBDataConfigType.INSERT_QUERY.getElementName(), COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, 
				"INSERT INTO  RULE_RESULT(CIFID,ACID,FIRST_NAME,EMAIL,MMF,RSA) VALUES (?,?,?,?,?,?) ");
		
		dbConfig.addNonMandatoryConfigElements(PraeceptaDBDataConfigType.INSERT_ATTRIBUTE_NAMES.getElementName(), COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING, 
				"CIFID|ACID|FIRST_NAME|EMAIL|MMF|RSA");
		
		dbInjector.openInjestorConnection(dbConfig);
		
		List<PraeceptaDictionaryData> dataRecords = new ArrayList<>(1);

		PraeceptaDictionaryData dataRecord1 = buildDataRecord("CFID1", "ACID1", "NAME1","1@1.com","MMF1", "RSA1");
		PraeceptaDictionaryData dataRecord2 = buildDataRecord("CFID2", "ACID2", "NAME2","2@2.com","MMF2", "RSA2");
		PraeceptaDictionaryData dataRecord3 = buildDataRecord("CFID3", "ACID3", "NAME3","3@3.com","MMF3", "RSA3");
		PraeceptaDictionaryData dataRecord4 = buildDataRecord("CFID4", "ACID4", "NAME4","4@4.com","MMF4", "RSA4");
		
		dataRecords.add(dataRecord1);
		dataRecords.add(dataRecord2);
		dataRecords.add(dataRecord3);
		dataRecords.add(dataRecord4);
		
		CountDownLatch latch = new CountDownLatch(5);

		Runnable run = () -> {
			dbInjector.injestData(dataRecords);
			latch.countDown();
		};
		
		Thread t1 = new Thread(run);
		Thread t2 = new Thread(run);
		Thread t3 = new Thread(run);
		Thread t4 = new Thread(run);
		Thread t5 = new Thread(run);
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dbInjector.terminateDataInjestor();
		
	}

	private PraeceptaDictionaryData buildDataRecord(String CIFID, String ACID, String FIRST_NAME, String EMAIL, String MMF, String RSA) {
		PraeceptaDictionaryData dataRecord = new PraeceptaDictionaryData();

		Map<String, Object> responseData = new HashMap<>();

		responseData.put("CIFID", CIFID);
		responseData.put("ACID", ACID);
		responseData.put("FIRST_NAME", FIRST_NAME);
		responseData.put("EMAIL", EMAIL);
		responseData.put("MMF", MMF);
		responseData.put("RSA", RSA);

		dataRecord.setDictionaryDetails(responseData);
		return dataRecord;
	}

}

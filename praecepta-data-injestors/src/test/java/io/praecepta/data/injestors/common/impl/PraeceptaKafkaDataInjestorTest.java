package io.praecepta.data.injestors.common.impl;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.Before;
import org.junit.Test;

import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.configs.common.IPraeceptaDataConfig.COLLECTOR_CONFIG_DATA_ELEMENT_TYPE;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;
import io.praecepta.data.configs.common.kafka.PraeceptaKafkaDataConfigType;
import io.praecepta.data.configs.common.kafka.PraeceptaKafkaInjestorConfig;

public class PraeceptaKafkaDataInjestorTest {

	PraeceptaKafkaInjestorConfig kafkaConfig = null;

	PraeceptaKafkaDataInjestor dataInjestor = null;

	@Before
	public void init() {

		dataInjestor = mock(PraeceptaKafkaDataInjestor.class);

		kafkaConfig = new PraeceptaKafkaInjestorConfig();

		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.BROKERS, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"localhost:9092");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PORT, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.INTEGRER,
				9092);
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.USERNAME, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"Test User");
		kafkaConfig.addConfigElement(PraeceptaKafkaDataConfigType.PASSWORD, COLLECTOR_CONFIG_DATA_ELEMENT_TYPE.STRING,
				"Test Password");

		kafkaConfig.addNonMandatoryConfigElements("max.timeout", "200");

		kafkaConfig.addNonMandatoryConfigElements("key.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");

		kafkaConfig.addNonMandatoryConfigElements("value.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");

		kafkaConfig.addNonMandatoryConfigElements("kafka.sender.topic", "TEST.TOPIC2");

	}

	@Test
	public void testOpenInjestorConnection() {

		doNothing().when(dataInjestor).openInjestorConnection(any(PraeceptaKafkaInjestorConfig.class));

		dataInjestor.openInjestorConnection(kafkaConfig);

		assertNotEquals(dataInjestor.getInjestorStatus(), CONNECTION_STATUS.INITIALIZED);

	}

	@Test
	public void testInitialize() throws Exception {

		PraeceptaKafkaDataInjestor spyObj = spy(PraeceptaKafkaDataInjestor.class);

		doNothing().when((PraeceptaAbstractDataInjestor) spyObj).initializeDataInjestor();

		Method privateMethod = PraeceptaKafkaDataInjestor.class.getDeclaredMethod("initialize",
				PraeceptaKafkaInjestorConfig.class);

		privateMethod.setAccessible(true);

		privateMethod.invoke(spyObj, kafkaConfig);

	}

	@Test
	public void testInjestData() throws Exception {

		PraeceptaDataRecord dataRecord = new PraeceptaDataRecord(1);

		String rulesResponse = "{\"attribute1\":\"val1\"}";

		Map<String, Object> responseMetaData = new HashMap<>();

		dataRecord.addRecordEntry(rulesResponse, null, responseMetaData);

		doReturn(CONNECTION_STATUS.INITIALIZED).when((PraeceptaAbstractDataInjestor) dataInjestor).getInjestorStatus();

		Field field1 = PraeceptaKafkaDataInjestor.class.getDeclaredField("SENDER_TOPIC");
		field1.setAccessible(true);

		KafkaProducer producer = mock(KafkaProducer.class);

		Field field2 = PraeceptaKafkaDataInjestor.class.getDeclaredField("kafkaProducer");
		field2.setAccessible(true);
		field2.set(dataInjestor, producer);

		dataInjestor.injestData(dataRecord);

	}

}

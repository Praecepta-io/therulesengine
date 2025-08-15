package io.praecepta.data.injestors.common.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord.RecordEntry;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;
import io.praecepta.data.configs.common.kafka.PraeceptaKafkaInjestorConfig;
import io.praecepta.data.injestor.common.exception.PraeceptaDataInjestorException;

public class PraeceptaKafkaDataInjestor<K extends Serializable, V extends Serializable>
		extends PraeceptaAbstractDataInjestor<PraeceptaKafkaInjestorConfig, PraeceptaDataRecord> {
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaKafkaDataInjestor.class);

	private KafkaProducer<K, V> kafkaProducer;

	private String SENDER_TOPIC;

	@Override
	public void openInjestorConnection(PraeceptaKafkaInjestorConfig kafkaInjestorConfig) {
		LOG.info("Before  establishing  Data Injestor connection");
		super.openInjestorConnection(kafkaInjestorConfig);
		initialize(kafkaInjestorConfig);
	}

	private void initialize(PraeceptaKafkaInjestorConfig kafkaInjestorConfig) {
		LOG.info("Before initializing Data Injestor");
		
		super.initializeDataInjestor();
		
		Map<String, String> kafkaConfigPropertis = kafkaInjestorConfig.getDataConfigurationWithValues();
		
		if (!PraeceptaObjectHelper.isObjectEmpty(kafkaConfigPropertis)) {

			kafkaProducer = new KafkaProducer(kafkaConfigPropertis);

			SENDER_TOPIC = kafkaConfigPropertis.get("kafka.sender.topic");
		}else {
			LOG.info("Data injestor config properties found empty/null");
		}
		
		LOG.info("Done initializing Data Injestor");
	}

	@Override
	public void injestData(PraeceptaDataRecord dataRecord) {
		
		LOG.info("start processing data injest");
		
		if(dataRecord == null) {
			throw new PraeceptaDataInjestorException("Data Record object found null");
		}
        
		if(getInjestorStatus() == null || getInjestorStatus() == CONNECTION_STATUS.INITIALIZED) {
			throw new PraeceptaDataInjestorException("Perform InjestData should be called only after initializing the snder");
		}
		
		if(PraeceptaObjectHelper.isObjectEmpty(SENDER_TOPIC)) {
			throw new PraeceptaDataInjestorException("Sender topic name should be configured to perform InjestData");
		}
		
		LinkedBlockingDeque<RecordEntry> recordEntries = dataRecord.getRecordEntries();
		
		while (recordEntries != null && !PraeceptaObjectHelper.isObjectEmpty(recordEntries)) {
			RecordEntry recordDataObj = recordEntries.poll();
			
			List<Header> metaDataHeaders = null;

			if (!PraeceptaObjectHelper.isObjectEmpty(recordDataObj.getMetaData())) {
				metaDataHeaders = recordDataObj.getMetaData().entrySet().stream()
						.map(entryObj -> new RecordHeader(entryObj.getKey(), entryObj.getValue()!=null?entryObj.getValue().toString().getBytes():null))
						.collect(Collectors.toList());
			}

			ProducerRecord producerRecordData = new ProducerRecord(SENDER_TOPIC,null, recordDataObj.getMessageTextId(),
					recordDataObj.getMessageText(),metaDataHeaders);
			
			LOG.info("Before injesting data ");
			
			kafkaProducer.send(producerRecordData);
		}
	}

	@Override
	public void terminateDataInjestor() {
		kafkaProducer.close();
		super.terminateDataInjestor();
	}
}

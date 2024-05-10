package io.praecepta.data.collectors.common.collector;

import java.io.Serializable;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.data.collectors.common.configs.kafka.PraeceptaKafkaCollectorConfig;
import io.praecepta.data.collectors.common.dto.PraeceptaDataRecord;
import io.praecepta.data.collectors.common.exception.PraeceptaDataCollectorException;
import io.praecepta.data.configs.common.enums.CONNECTION_STATUS;

public class PraeceptaKafkaDataCollector<K extends Serializable,V extends Serializable> extends PraeceptaAbstractDataCollector<PraeceptaKafkaCollectorConfig>{
	private KafkaConsumer<K, V> consumer;
	@Override
	public void openCollectorConnection(PraeceptaKafkaCollectorConfig kafkaCollectorConfig) {
		super.openCollectorConnection(kafkaCollectorConfig);
		consumer=initializeDataCollector(kafkaCollectorConfig);
		startDataCollector();
	}
	
	private KafkaConsumer<K, V> initializeDataCollector(PraeceptaKafkaCollectorConfig kafkaCollectorConfig) {
		Map<String,String> kafkaConfigPropertis=kafkaCollectorConfig.getDataConfigurationWithValues();
		kafkaConfigPropertis.putIfAbsent("auto.offset.reset", "earliest");
		kafkaConfigPropertis.putIfAbsent("max.poll.records", "5");
		KafkaConsumer<K, V> consumer = new KafkaConsumer(kafkaConfigPropertis);
		consumer.subscribe(Arrays.asList(kafkaConfigPropertis.get("kafka.receiver.topic")));
		return consumer;
	}

	@Override
	protected PraeceptaDataRecord performCollection() {
		// This method cannot be called directly. Must call startDataCollector(). Underlying will call this method to poll individual Collection 
		
		if(getCollectorStatus() == null || getCollectorStatus() == CONNECTION_STATUS.INITIALIZED) {
			throw new PraeceptaDataCollectorException("Perform Collector should be called only after Starting the Data Collector");
		}
		
		ConsumerRecords<K, V> records = consumer.poll(Duration.ofSeconds(1));

		if (!PraeceptaObjectHelper.isObjectEmpty(records)&&records.count()>0) {
			PraeceptaDataRecord dataRecord = new PraeceptaDataRecord(records.count());
			records.forEach(consumerRecord -> {
				dataRecord.addRecordEntry(consumerRecord.value().toString(), getMessageId(consumerRecord), getHeadersMap(consumerRecord));

			});
			return dataRecord;
		}
		return null;
	}

	@Override
	public void terminateDataCollector() {
		super.terminateDataCollector();
		this.consumer.unsubscribe();
        this.consumer.close();
	}
	
	private String getMessageId(ConsumerRecord record) {
		if(!PraeceptaObjectHelper.isObjectEmpty(record.key())) {
			return record.key().toString();
		}
		return UUID.randomUUID().toString();
	}
	
	private Map<String,Object> getHeadersMap(ConsumerRecord record){
		Map<String,Object> headersMap=new HashMap<>();
		if(!PraeceptaObjectHelper.isObjectEmpty(record.headers())) {
			record.headers().forEach(objHeader->{
				if(!PraeceptaObjectHelper.isObjectNull(objHeader)) {
					headersMap.put(objHeader.key(), new String(objHeader.value()));
				}
			});
		}
		return headersMap;
	}
}

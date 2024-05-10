package io.praecepta.rules.executor.config.condition.kafka;

import io.praecepta.rules.executor.config.condition.ConditionConfigSelector;
import io.praecepta.rules.executor.config.enums.DATA_COLLECTOR_TYPE;

public class KafkaConditionConfigSelector extends ConditionConfigSelector{

	@Override
	protected DATA_COLLECTOR_TYPE getCollectorType() {
		return DATA_COLLECTOR_TYPE.KAFKA;
	}
}

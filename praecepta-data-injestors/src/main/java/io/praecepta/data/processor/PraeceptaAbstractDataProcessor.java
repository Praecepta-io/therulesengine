package io.praecepta.data.processor;

import io.praecepta.core.data.intf.IPraeceptaDataProcessor;
import io.praecepta.data.configs.common.GenericAbstractPraeceptaDataConfig;
import io.praecepta.data.configs.common.db.PraeceptaDBInjestorConfig;

import java.util.Collection;

public abstract class PraeceptaAbstractDataProcessor<E,PROCESSOR_CONFIG extends GenericAbstractPraeceptaDataConfig> implements IPraeceptaDataProcessor<E> {
   public abstract void initializeProcessor(PROCESSOR_CONFIG config);
}

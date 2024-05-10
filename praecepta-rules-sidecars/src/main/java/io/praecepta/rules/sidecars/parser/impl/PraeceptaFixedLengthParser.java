package io.praecepta.rules.sidecars.parser.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.sidecars.IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.parser.IPraeceptaSideCarParser;
import io.praecepta.rules.sidecars.parser.dataconverter.IpraeceptaDataTypeConverter;
import io.praecepta.rules.sidecars.parser.exception.PraeceptaParserException;
import io.praecepta.rules.sidecars.parser.registry.PraeceptaDataTypeConvertorRegistry;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaFixedLengthParser implements IPraeceptaSideCarParser {

    private final static Logger logger = LoggerFactory.getLogger(PraeceptaFixedLengthParser.class);
    public static final String DATA_TYPE = "dataType";
    public static final String START_POSITION = "start";
    public static final String END_POSITION = "end";
    public static final String DATA_TYPE_NUMBER = "number";
    public static final String DATA_TYPE_DECIMAL = "decimal";

    @Override
    public void initializeParser() {

    }

    /**
     *
     * @param sideCarDataHolder
     */
    @Override
    public void parse(PraeceptaSideCarDataHolder<String, Map<String, Object>> sideCarDataHolder) {

        logger.debug(" Inside the Fixed Length Parser");

        if(!PraeceptaObjectHelper.isObjectEmpty(sideCarDataHolder)) {

            logger.debug(" Side Car Holder Present in Fixed Length Parser");

            String inputMessage = sideCarDataHolder.retriveInput();

			if (!PraeceptaObjectHelper.isObjectEmpty(inputMessage)) {

				logger.info(" Input Message received in the Parser - {} ", inputMessage);

				if (validate(inputMessage)) {

					Map<String, Object> outputMessage = new HashMap<>();

					Map<String, Object> additionalInfoMap = sideCarDataHolder.getAdditionalHolderInfo();

					logger.info(" Run time Configuration for Fixed Length Parser - {} ", additionalInfoMap);

					if (!PraeceptaObjectHelper.isObjectEmpty(additionalInfoMap)) {
						Map<String, Object> sideCarConfigs = (Map<String, Object>) additionalInfoMap
								.get(IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.RUN_TIME_CONFIG);

						if (!PraeceptaObjectHelper.isObjectEmpty(sideCarConfigs)) {

							sideCarConfigs.keySet().forEach(key -> {
								Map<String, Object> attributeMappingsForAKey = (Map<String, Object>) sideCarConfigs.get(key);

								logger.info(" Attributes Mapping for Key - {} is - {}", key, attributeMappingsForAKey);

								if(!PraeceptaObjectHelper.isObjectEmpty(attributeMappingsForAKey)) {
									
									Object startPos = attributeMappingsForAKey.get(START_POSITION);
									Object endPos = attributeMappingsForAKey.get(END_POSITION);
									
									if(!PraeceptaObjectHelper.isObjectEmpty(startPos) && !PraeceptaObjectHelper.isObjectEmpty(endPos)) {
										
										int startPostionValue = Integer.valueOf(startPos.toString());
										int endPostionValue = Integer.valueOf(endPos.toString());
										
										if (inputMessage.length() >= endPostionValue) {
											
											String message = inputMessage.substring(startPostionValue, endPostionValue).trim();
											
											if (attributeMappingsForAKey.get(DATA_TYPE) != null) {
												IpraeceptaDataTypeConverter dataTypeConverter = PraeceptaDataTypeConvertorRegistry
														.getInstance()
														.getDataTypeConvertor((String) attributeMappingsForAKey.get(DATA_TYPE));
												outputMessage.put(key, dataTypeConverter.convert(message, attributeMappingsForAKey));
											} else {
												outputMessage.put(key, message);
											}
										}
									}
								}
							});
						}

						if (!PraeceptaObjectHelper.isObjectEmpty(outputMessage)) {

							sideCarDataHolder.addOutput(outputMessage);
						}
					} else {
						throw new PraeceptaParserException(
								" Input received is not a Valid Fixed Length Message to Parse. Check the Input passed -  "
										+ inputMessage);
					}

				}
			}
        }
    }

    @Override
    public boolean validate(String input) {
        return !PraeceptaObjectHelper.isStringEmpty(input);
    }


}

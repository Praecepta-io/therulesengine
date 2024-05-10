package io.praecepta.rules.sidecars.parser.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.sidecars.IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.parser.IPraeceptaSideCarParser;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaDelimiterParser implements IPraeceptaSideCarParser {

    private final static Logger logger = LoggerFactory.getLogger(PraeceptaDelimiterParser.class);
    public static final String DELIMITER_CHAR = "delimiterChar";
    public static final String DELIMITER_PARSER_ATTRIBUTE_NAMES = "delimiterParserAttributeNames";
    public static final String INDEX = "index";

    @Override
    public void initializeParser() {

    }

    @Override
    public void parse(PraeceptaSideCarDataHolder<String, Map<String, Object>> sideCarDataHolder) {

        if(!PraeceptaObjectHelper.isObjectEmpty(sideCarDataHolder)) {

            logger.debug(" Side Car Holder Present in Delimiter  Parser");

            String inputMessage = sideCarDataHolder.retriveInput();

            if (!PraeceptaObjectHelper.isObjectEmpty(inputMessage)) {

                logger.debug(" Input Message received in the Parser - {} ", inputMessage);

                if (validate(inputMessage)) {
                    Map<String,Object> outputMessage = new HashMap<>();

                    Map<String,Object> additionalInfoMap = sideCarDataHolder.getAdditionalHolderInfo();
                    
                    logger.info(" Additional Info Map Details - {} ", additionalInfoMap);
                    
                    if (!PraeceptaObjectHelper.isObjectEmpty(additionalInfoMap)) {
                    	
                    	Map<String, Object> sideCarConfigs = (Map<String, Object>) additionalInfoMap
								.get(IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.RUN_TIME_CONFIG);
                    	
                    	logger.info(" Run time Configuration for Delimited File Parser - {} ", sideCarConfigs);
                    	
                    	if (!PraeceptaObjectHelper.isObjectEmpty(sideCarConfigs)) {
                    		
                    		String delimiterToUse = (String) sideCarConfigs.get(DELIMITER_CHAR);
                    		
                    		logger.info(" Delimiter to Use in the Delimiter Parser is - {} ", delimiterToUse);
                    		
		                    if(delimiterToUse != null){
		                        String[] splitMessage = inputMessage.split(delimiterToUse);
		                        
		                        if(sideCarConfigs.get(DELIMITER_PARSER_ATTRIBUTE_NAMES) != null){
		                        	
		                            Map<Integer, String> attributeSplitPositionToNamesMapping = (Map<Integer, String>) sideCarConfigs.get(DELIMITER_PARSER_ATTRIBUTE_NAMES);
		                            
		                            for (int position = 0; position < splitMessage.length; position++) {
		                            	
		                                if(attributeSplitPositionToNamesMapping.get(position) != null)
		                                    outputMessage.put(attributeSplitPositionToNamesMapping.get(position), splitMessage[position]);
		                                else
		                                    outputMessage.put(INDEX +position, splitMessage[position]);
		                            }
		                        }else{
		                            for (int position = 0; position < splitMessage.length; position++) {
		                                outputMessage.put(INDEX +position, splitMessage[position]);
		                            }
		                        }
		
		                        sideCarDataHolder.addOutput(outputMessage);
		                    }
                    	}
                    }
                }

            }

        }
    }

    @Override
    public boolean validate(String input) {
        return IPraeceptaSideCarParser.super.validate(input);
    }
}

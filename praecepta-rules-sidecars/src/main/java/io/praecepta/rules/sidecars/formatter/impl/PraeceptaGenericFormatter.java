package io.praecepta.rules.sidecars.formatter.impl;

import static io.praecepta.rules.engine.execution.PraeceptaCriteriaExecutor.getValueForNestedAttribute;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import io.praecepta.core.helper.GsonHelper;
import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.sidecars.IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector;
import io.praecepta.rules.sidecars.formatter.IPraeceptaSideCarFormatter;
import io.praecepta.rules.sidecars.formatter.attribute.IPraeceptaAttributeFormatter;
import io.praecepta.rules.sidecars.formatter.attribute.PraeceptaAttributeFormatterTypeRegistry;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaGenericFormatter implements IPraeceptaSideCarFormatter {

	private final static Logger logger = LoggerFactory.getLogger(PraeceptaGenericFormatter.class);
	public static final String FORMATTED_ATTRIBUTE_NAME = "formattedAttributeName";
    public static final String SOURCE_ATTRIBUTE_NAME = "sourceAttributeName";
    public static final String FORMATTER_CONFIGS = "formatterConfigs";
    public static final String FORMATTER_TYPE = "formatterType";
    public static final String PATTERN = "pattern";
    public static final String ADDITIONAL_INFO_MAP = "additionalInfoMap";
//    public static final String SIDE_CAR_CONFIG = "sideCarConfig";
    public static final String FORMATE_DETAILS = "FORMATE_DETAILS";

    public static final String FORMATTER_TEMPLATE = "FORMATTER_TEMPLATE";
    public static final String FORMATTER_ATRIBUTES = "formatterAtributes";

    public static final String TEMPLATE_NAME = "TEMPLATE_NAME";
    
    Map<String, StringTemplateLoader> templateNameToLoader;
    
    private Configuration cfg;

	public PraeceptaGenericFormatter(){
		initializeFormatter();
	}
    
	@Override
	public void initializeFormatter() {
		
		templateNameToLoader = new HashMap<>();
		cfg = new Configuration(Configuration.VERSION_2_3_23);
		
	}

	@Override
	public void format(PraeceptaSideCarDataHolder<Map<String, Object>, String> input) {

		logger.debug("inside formatter ");
        Map<String, Object> sideCarConfigs = (Map<String, Object>) input.getAdditionalHolderInfo().get(IPraeceptaPipesAndFiltersInfoTrackerSideCarInjector.RUN_TIME_CONFIG);
        logger.info("Formatter sidecar Configs : {}", sideCarConfigs);
        
		if (!PraeceptaObjectHelper.isObjectEmpty(sideCarConfigs)) {
			String templateName = (String) sideCarConfigs.get(TEMPLATE_NAME);

			/*
			 * String formatterDetails = (String) sideCarConfigs.get(FORMATE_DETAILS);
			 * Map<String,Object> templateMap = GsonHelper.toEntity(formatterDetails,
			 * Map.class);
			 */
			Map<String, Object> attributeLevelFormatterDetails = (Map<String, Object>) sideCarConfigs
					.get(FORMATE_DETAILS);

			Map<String, Object> outputMap = new HashMap<>();

//			String inputMessage = input.retriveInput(String.class);

			Map<String, Object> inputMap = input.retriveInput();
			
			logger.debug("Input Message {} ", inputMap);

			if (!PraeceptaObjectHelper.isObjectEmpty(attributeLevelFormatterDetails)) {

				List<Map<String, Object>> formatterAttributesList = (List<Map<String, Object>>) attributeLevelFormatterDetails
						.get(FORMATTER_ATRIBUTES);

				formatterAttributesList.forEach(formatterAttribute -> {
					
					boolean populateValueFromInput = true;
					
					String sourceAttributeName = (String) formatterAttribute.get(SOURCE_ATTRIBUTE_NAME);
					
					logger.debug(" Initiating the Format for Attribute {} ", sourceAttributeName);

					Object valueToFormat = getValueForNestedAttribute(sourceAttributeName, inputMap);

					Map<String, Object> formatterRunetimeConfig = (Map<String, Object>) formatterAttribute
							.get(FORMATTER_CONFIGS);
					
					// Value To Format is not Empty and Formatter Runtime Configuration exist to proceed with below Lines to format
					if (!PraeceptaObjectHelper.isObjectEmpty(valueToFormat) && !PraeceptaObjectHelper.isObjectEmpty(formatterRunetimeConfig)) {
						
						String formatterType = (String) formatterRunetimeConfig.get(FORMATTER_TYPE);
						String formatterPattern = (String) formatterRunetimeConfig.get(PATTERN);
						
						logger.debug("Formatter Type - {} And Formatter Pattern Provided - {} ", formatterType, formatterPattern);
						
						if (!PraeceptaObjectHelper.isObjectEmpty(formatterType) && !PraeceptaObjectHelper.isObjectEmpty(formatterPattern) ) {

							IPraeceptaAttributeFormatter formatter = PraeceptaAttributeFormatterTypeRegistry
									.getFormatterByType(formatterType);

							if (formatter != null) {

								Object formattedValue = formatter.format(formatterPattern, valueToFormat,
										(Map<String, Object>) formatterRunetimeConfig.get(ADDITIONAL_INFO_MAP));
								
								logger.debug("Formatted Value - {} ", formattedValue);

								populateValue(outputMap, formatterAttribute, formattedValue);
								
								logger.debug(" Here is the Output Map after Map Update {}  ", outputMap);
								
								populateValueFromInput = false; // This is assigned as False as the Value is getting assigned after the Format is done
								
							} 
						}
					} 
					
					// Below Code will be Executed incase Any of the below attributes are Empty
					// valueToFormat or Formatter Config or Pattern or Format Type 
					if(populateValueFromInput) {
						
						logger.debug(" Populating Default Value  ");
						populateValue(outputMap, formatterAttribute, valueToFormat);
						
						logger.debug(" Here is the Output Map after Map Update {}  ", outputMap);
					}
				});
			}

			String formatterTemplate = getTemplate(sideCarConfigs);

			logger.info(" Formatter Template --> {} ", formatterTemplate);

			if (formatterTemplate != null) {
				for (String key : inputMap.keySet()) {
					outputMap.putIfAbsent(key, inputMap.get(key));
				}
				
				logger.debug(" Here is the Output Map after Input Map Merge {}  ", outputMap);

				try {

					StringTemplateLoader templateLoader = templateNameToLoader.get(templateName);

					if (templateLoader == null) {

						templateLoader = new StringTemplateLoader();
						templateLoader.putTemplate(templateName, formatterTemplate);
						cfg.setTemplateLoader(templateLoader);
					}

					templateNameToLoader.put(templateName, templateLoader);

					Writer out = new StringWriter();

					cfg.getTemplate(templateName).process(outputMap, out);

					String outputMessage = out.toString();
					logger.info("Message after Format : {}", outputMessage);
					input.addOutput(outputMessage);

				} catch (TemplateNotFoundException e) {
					logger.error("Failed to FTL Template {}", e);
				} catch (IOException e) {
					logger.error("Failed to FTL Template {}", e);
				} catch (TemplateException e) {
					logger.error("Failed to FTL Template {}", e);
				}

			} else {
				String outputMessage = GsonHelper.toJson(outputMap);
				logger.info("Message after Format {} ", outputMessage);
				input.addOutput(outputMessage);
			}
        }
		
	}

	protected String getTemplate(Map<String, Object> sideCarConfigs) {
		String formatterTemplate = (String) sideCarConfigs.get(FORMATTER_TEMPLATE);
		return formatterTemplate;
	}
	
	/**
     * Method to populate value for nested attribute/ direct attribute based on . present in the attribute name
     * @param outputMap
     * @param formatterAttribute
     * @param formattedValue
     */
    private static void populateValue(Map<String, Object> outputMap, Map<String, Object> formatterAttribute, Object formattedValue) {
        Map<String, Object> tempMap = outputMap;
        
        // Populate Value for  Nested Map
        if(((String) formatterAttribute.get(FORMATTED_ATTRIBUTE_NAME)).contains(".")) {
            String[] st = ((String) formatterAttribute.get(FORMATTED_ATTRIBUTE_NAME)).split("\\.");
            for (int i = 0; i < st.length; i++) {
                if (i != st.length - 1) {
                    if(tempMap.get(st[i]) == null){
                        tempMap.put(st[i] , new HashMap<>());
                    }
                    tempMap = (Map<String, Object>) tempMap.get(st[i]);
                }else {
                    tempMap.put(st[i], formattedValue);
                }
            }

        }else{
        	// Populate Value for  Top level or Non - Nested Map
            outputMap.put((String) formatterAttribute.get(FORMATTED_ATTRIBUTE_NAME), formattedValue);
        }
    }


}

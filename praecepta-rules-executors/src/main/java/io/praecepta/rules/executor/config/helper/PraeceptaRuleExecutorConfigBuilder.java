package io.praecepta.rules.executor.config.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.builders.PraeceptaRuleBuilder;
import io.praecepta.rules.builders.exceptions.PraeceptaRuleBuilderException;
import io.praecepta.rules.executor.config.enums.DATA_COLLECTOR_CONFIG_KEYS;
import io.praecepta.rules.executor.config.enums.DATA_COLLECTOR_TYPE;

public class PraeceptaRuleExecutorConfigBuilder {
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaRuleExecutorConfigBuilder.class);

	private static String dataCollectorConfigFile = "praecepta-data-collector.properties";

	private static final Map<String, String> collectorConfigProps = new HashMap<>();

	private static final PraeceptaRuleExecutorConfigBuilder configBuilder = new PraeceptaRuleExecutorConfigBuilder();

	private static final String DATA_COLLECTOR_ENV_PROPS = "praecepta.rule.data.collector.props.location";
	
	private static InputStream streamToRead;

	public static PraeceptaRuleExecutorConfigBuilder buildWithPropsFileLocation(String dataCollectorPropsFile) {

		if (!PraeceptaObjectHelper.isStringEmpty(dataCollectorPropsFile)) {

			buildWithPropsClasspath(null);
			
		} else {
			
			try {
				streamToRead = new FileInputStream(dataCollectorPropsFile);
				
				captureDataLoadProps(streamToRead);
				
			} catch (FileNotFoundException e) {
				
				logger.error("Provided Data Collector File Location in the System Param is not Available ", e);
			}

		}

		return configBuilder;
	}

	public static PraeceptaRuleExecutorConfigBuilder buildWithPropsClasspath(String classPathFile) {

		if (!PraeceptaObjectHelper.isStringEmpty(classPathFile)) {

			dataCollectorConfigFile = classPathFile;

		}
		logger.info("Data Collector Props File to Use from Classpath {} ", dataCollectorConfigFile);

		InputStream streamToRead = PraeceptaRuleBuilder.class.getClassLoader()
				.getResourceAsStream(dataCollectorConfigFile);

		captureDataLoadProps(streamToRead);
		return configBuilder;
	}

	public static PraeceptaRuleExecutorConfigBuilder buildWithEnvParmPropsFile() {

		String dataCollectorPropsFile = System.getProperty(DATA_COLLECTOR_ENV_PROPS);

		return buildWithPropsFileLocation(dataCollectorPropsFile);
	}

	private static void captureDataLoadProps(InputStream streamToRead) {

		if (!PraeceptaObjectHelper.isObjectEmpty(streamToRead)) {
			Properties props = new Properties();

			try {
				props.load(streamToRead);

				logger.info("Props Loaded  --> {} ", props);
			} catch (IOException e) {
				throw new PraeceptaRuleBuilderException(" Unable to load Data Collector Properties ");
			}

			props.forEach((propKey, propValue) -> {
				if (!PraeceptaObjectHelper.isObjectEmpty(propKey)) {

					String propValueToUse = PraeceptaObjectHelper.isObjectEmpty(propValue) ? "" : propValue.toString();

					collectorConfigProps.put(propKey.toString(), propValueToUse);
				}
			});
		}
	}

	public void buildConfigs() {

		String collectorType = collectorConfigProps.get(DATA_COLLECTOR_CONFIG_KEYS.DATA_COLLECTOR_TYPE.value);

		logger.info(" Data Collector Type Type from Props File --> {} ", collectorType);

		if (!PraeceptaObjectHelper.isObjectEmpty(collectorType)) {

			String connectionPropsPrefix = new String().join(".", collectorType,
					DATA_COLLECTOR_CONFIG_KEYS.CONNECTION_PROPS.value);

			System.setProperty(DATA_COLLECTOR_CONFIG_KEYS.CONN_PROPS_PREFIX.value, connectionPropsPrefix);

			logger.info(" Data Collector Connection Props Prefix {} ", connectionPropsPrefix);

			Map<String, String> connectionProps = collectorConfigProps.entrySet().stream()
					.filter(entry -> entry.getKey().contains(connectionPropsPrefix))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			logger.info("Data Collector Connection Props from Props File --> {} ", connectionProps);
			if (!PraeceptaObjectHelper.isObjectEmpty(connectionProps)) {
				connectionProps.forEach((propName, PropValue) -> {
					logger.info(" Adding System Prop Key {} with Value {}", propName, PropValue);
					System.setProperty(propName, PropValue);
				});
			}

			if (!PraeceptaObjectHelper.isObjectEmpty(collectorType)) {
				System.setProperty(DATA_COLLECTOR_TYPE.class.getSimpleName(), collectorType.toString());
			}
		}

	}
}

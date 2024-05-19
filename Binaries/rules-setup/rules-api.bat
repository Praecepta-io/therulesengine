@echo off

set prop_file_name=%1

echo %prop_file_name%

IF "%prop_file_name%"=="" (
	echo Default Praecepta Property File Will be used file 

	goto defultPraecepta
  
) ELSE (
echo %prop_file_name%

	echo "%prop_file_name%" will be used as Praecepta Property File 
	
	goto customPraecepta
	
)

:defultPraecepta
java -Dlogback.configurationFile=log/praecepta-rules-api-logback.xml  -DLOG_LEVEL=INFO -cp praecepta-rules-api-1.0.1.jar;lib/* io.praecepta.rest.api.PraeceptaRuleGroupApiLauncher

exit /b

:customPraecepta

java -Dpraecepta.rule.load.props.location=%prop_file_name% -Dlogback.configurationFile=log/praecepta-rules-api-logback.xml  -DLOG_LEVEL=INFO -cp praecepta-rules-api-1.0.1.jar;lib/* io.praecepta.rest.api.PraeceptaRuleGroupApiLauncher

exit /b
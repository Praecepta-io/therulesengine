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
java -Dlogback.configurationFile=log/praecepta-choreographic-rules-engine-logback.xml  -DLOG_LEVEL=INFO  -cp praecepta-rules-executors-1.0.1.jar;lib/* io.praecepta.rules.executor.PraeceptaRulesExecutorLauncher

exit /b

:customPraecepta

java -Dpraecepta.rule.load.props.location=%prop_file_name% -Dlogback.configurationFile=log/praecepta-choreographic-rules-engine-logback.xml  -DLOG_LEVEL=INFO  -cp praecepta-rules-executors-1.0.1.jar;lib/* io.praecepta.rules.executor.PraeceptaRulesExecutorLauncher

exit /b

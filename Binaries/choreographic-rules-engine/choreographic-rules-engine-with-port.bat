echo off

echo "Usage : $0 propfile port datacollectorfile"

set prop_file_name=%1

set port=%2

set data_collector_file=%3

set BOTH=0

if "%prop_file_name%"=="" (

	if "%port%"=="" (
	echo "Set Both"
		set BOTH=1
	) 
) 

if "%BOTH%"=="0" if "%prop_file_name%"=="" set "prop_file_name=praecepta-rule-load.properties" 

if "%BOTH%"=="0" if "%port%"=="" set "port=4567"

if "%BOTH%"=="0" if "%data_collector_file%"=="" set "data_collector_file=praecepta-data-collector.properties"

if "%BOTH%"=="1" (

set "prop_file_name=praecepta-rule-load.properties" 
set "port=4567"
set "data_collector_file=praecepta-data-collector.properties"

)

echo "Prop File name" "%prop_file_name%"

echo  "Port" "%port%"

echo "data_collector_file" "%data_collector_file%"

java -Dpraecepta.rule.load.props.location=%prop_file_name% -Dserver.port=%port% -Dpraecepta.rule.data.collector.props.location=%data_collector_file% -Dlogback.configurationFile=log/praecepta-choreographic-rules-engine-logback.xml  -DLOG_LEVEL=INFO  -cp praecepta-rules-executors-1.0.2.jar;lib/* io.praecepta.rules.executor.PraeceptaRulesExecutorLauncher

exit /b


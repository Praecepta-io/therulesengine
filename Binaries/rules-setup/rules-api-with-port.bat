echo off

echo "Usage : $0 propfile port"

set prop_file_name=%1

set port=%2

set BOTH=0

if "%prop_file_name%"=="" (

	if "%port%"=="" (
	echo "Set Both"
		set BOTH=1
	) 
) 

if "%BOTH%"=="0" if "%prop_file_name%"=="" set "prop_file_name=praecepta-rule-load.properties" 

if "%BOTH%"=="0" if "%port%"=="" set "port=4567"

if "%BOTH%"=="1" (

set "prop_file_name=praecepta-rule-load.properties" 
set "port=4567"


)

echo "Prop File name" "%prop_file_name%"

echo  "Port" "%port%"

java -Dpraecepta.rule.load.props.location=%prop_file_name% -Dserver.port=%port%  -Dlogback.configurationFile=log/praecepta-rules-api-logback.xml  -DLOG_LEVEL=INFO -cp praecepta-rules-api-1.0.1.jar;lib/* io.praecepta.rest.api.PraeceptaRuleGroupApiLauncher

exit /b
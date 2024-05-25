#### Author - Rajasrikar Rao Punugoti, Senior Director, Broadridge Financials - https://www.linkedin.com/in/rpunugoti/
# Praecepta - The Rules Engine - https://www.praecepta.io/
Efficient, Salable, Reliable and Cost Effective Orchestration Business Rules Engine for Polyglot Software Systems

<details open>
<summary><h1>What is Praecepta</h1></summary>
Praecepta is a modern and intelligent globally distributed multi tenanat business rules engine that’s easy to set up, deploy and manage. Use Praecepta to easily build personalized complex business rules using out of the box services to productize and run across any technology or platform.

![image](https://github.com/Praecepta-io/therulesengine/assets/122503259/244671cc-903d-404a-bd17-9d71fbc53f59)
</details>

<details open>
<summary><h1>Why Praecepta</h1></summary>

<h3>$${\color{blue}Microservices Architecture}$$</h3>

$${\color{green}Rule SetUp, Execution, Software As A Service [SAAS]}$$ 

Highly scalable, elastic multi-tenant and decoupled responsive microservices that can operate in any environment or platform can bootstrap Rule space setup and exection engine in an effective manner. It also offers a powerful Software as a Service (SaaS) layer which can quickly deliver the needs of end users.

![image](https://github.com/Praecepta-io/therulesengine/assets/122503259/c15d10e5-85aa-4dae-9aec-95b3134996a9)

<h3>$${\color{blue}Installation and Deployment Model}$$</h3>

$${\color{green}Cloud, Hybrid, On-Prem}$$

Services are containerized for supporting the autonomy strategy of independently deployable artifacts across on-prem, hybrid and multicloud/cloud-agnostic environments will provide a good balance of resource isolation with fairly low overhead. This model can host and support multiple workloads and tenanats.

![image](https://github.com/Praecepta-io/therulesengine/assets/122503259/dee44efd-b8e0-4e5a-b61b-8e457bca58ce)

<h3>$${\color{blue}Seamless Integration}$$</h3>

$${\color{green}Java, .Net, Python, NodeJs}$$

System composed of independent and collaborating encapsulated REST API services with well defined open api specification for each component. Simple and light weight polyglot client libraries are availble to integrate with industry standard programming languages will increase the productivity.

![image](https://github.com/Praecepta-io/therulesengine/assets/122503259/19eb6280-071c-46ee-bcd5-24c335d16436)

<h3>$${\color{blue}Accelerate Product Delivery}$$</h3>

$${\color{green}No-Code, Connectors, Sidecars}$$

Out of the box configuration driven connectors make data ingestion from Relational, NoSQL and Real time Streaming sources easy. Sophisticated and fully functional modern operational UI provided to visualize and define the initialization of rules engine set up and execution [no programming language is required]. Orchstrate to Parse, Enrich and Format using extensibile sidecar mechanism for pre and post rule space execution.

![image](https://github.com/Praecepta-io/therulesengine/assets/122503259/00748801-33ed-4b99-afd3-626280699ff8)

</details>

<details open>
<summary><h1>Using Praecepta Application</h1></summary>

- [x] Softwares Required :
1. `Java Development Kit [JDK] 8`
2. `Maven 3.2.5`
3. `Git Tools`
- [x] Code Checkout :
1. `Create a new Folder with Name - Praecepta in 'C' folder`
2. `Open Command Prompt and type --> cd C:\Praecepta`
3. `git clone https://github.com/Praecepta-io/therulesengine.git`
- [x] Building The Application :
1. `Go to folder --> cd C:\Praecepta\therulesengine`
2. `Execute command --> mvn clean install`      
- [x] Running The Application With Different Options:
1. `Rules Set Up On A Default Port and Rules Set Up Properties File`
   
| Description | Command |
| --- | --- |
| Open Command Prompt from and run | cd C:\Praecepta\therulesengine\praecepta-rules-api\target |
| Execute Following Commoand | java  -cp praecepta-rules-api-0.0.1-SNAPSHOT.jar;lib/* io.praecepta.rest.api.PraeceptaRuleGroupApiLauncher |
| Open API Swagger | http://localhost:4567/swaggerJson/ruleSpaceController and http://localhost:4567/swaggerJson/ruleGroupController |

2. `Rules Set Up On A Port Specified and Rules Set Up Properties File Provided`
   
| Description | Command |
| --- | --- |
| Open Command Prompt from and run | cd C:\Praecepta\therulesengine\praecepta-rules-api\target |
| Execute Following Commoand | java -Dserver.port=8080 -Dpraecepta.rule.load.props.location=setup.properties -cp praecepta-rules-api-0.0.1-SNAPSHOT.jar;lib/* io.praecepta.rest.api.PraeceptaRuleGroupApiLauncher |
| Open API Swagger | http://localhost:8080/swaggerJson/ruleSpaceController and http://localhost:8080/swaggerJson/ruleGroupController |

3. `Run Rules Execution by Passing the Rules Input in An API call`
   
| Description | Command |
| --- | --- |
| Open Command Prompt from and run | cd C:\Praecepta\therulesengine\praecepta-orchestration-rules-executors\target |
| Execute Following Commoand | java -cp praecepta-orchestration-rules-executors-0.0.1-SNAPSHOT.jar;lib/* io.praecepta.rest.api.PraeceptaRuleExecutorApiLauncher |
| Open API Swagger | http://localhost:4567/swaggerJson/ruleExecutionController |

4. `Run Rules Execution by Passing the Rules Input in An API call With a Specified Port and Rules Set Up Properties File Provided`
   
| Description | Command |
| --- | --- |
| Open Command Prompt from and run | cd C:\Praecepta\therulesengine\praecepta-orchestration-rules-executors\target |
| Execute Following Commoand | java -Dserver.port=8080 -Dpraecepta.rule.load.props.location=setup.properties -cp praecepta-orchestration-rules-executors-0.0.1-SNAPSHOT.jar;lib/* io.praecepta.rest.api.PraeceptaRuleExecutorApiLauncher |
| Open API Swagger | http://localhost:8080/swaggerJson/ruleExecutionController |

5. `Run Rules Execution by Passing the Rules Input in a Message Broker`
   
| Description | Command |
| --- | --- |
| Open Command Prompt from and run | cd C:\Praecepta\therulesengine\praecepta-rules-executors\target |
| Execute Following Commoand | java -cp praecepta-rules-executors-0.0.1-SNAPSHOT.jar;lib/* io.praecepta.rules.executor.PraeceptaRulesExecutorLauncher |

6. `Run Rules Execution by Passing the Rules Input in a Message Broker with Different Data Collector and Rules Set Up Properties File Provided`
   
| Description | Command |
| --- | --- |
| Open Command Prompt from and run | cd C:\Praecepta\therulesengine\praecepta-rules-executors\target |
| Execute Following Commoand | java -Dpraecepta.rule.data.collector.props.location=data-collector.properties -Dpraecepta.rule.load.props.location=setup.properties -cp praecepta-rules-executors-0.0.1-SNAPSHOT.jar;lib/* io.praecepta.rules.executor.PraeceptaRulesExecutorLauncher |

</details>

### Sample Rule Set Up Property File
````
# SQL_DB, FILE_SYSTEM, CACHE, NO_SQL_DB, REST_API 
rule_loader_type=FILE_SYSTEM

#--------- Start here for SQL DB Specific --------------

# Mandatory Field if <b>rule_loader_type</b> is SQL_DB
# Supported SQL DB's are MySQL, Oracle, H2, Postgres, SQLServer
SQL_DB.type=

SQL_DB.connection_props.MySQL.db.driver=com.mysql.jdbc.Driver
SQL_DB.connection_props.MySQL.db.url=jdbc:mysql://127.0.0.1:3306/praecepta?autoReconnect=true
SQL_DB.connection_props.MySQL.db.username=root
SQL_DB.connection_props.MySQL.db.password=root
SQL_DB.connection_props.MySQL.db.dialect=io.hibernate.dialect.MySQLDialect
SQL_DB.connection_props.MySQL.show_sql=true
SQL_DB.connection_props.MySQL.model.packages=io.praecepta.rules.hub.dbbased.model

# DB Connection Props. 
# All DB Props should be prefix with --> SQL_DB.connection_props.<SQL_DB.type>
#SQL_DB.connection_props.MySQL.ip=
#SQL_DB.connection_props.MySQL.port=
#SQL_DB.connection_props.MySQL.user_name=

#--------- End here for SQL DB Specific --------------

#--------- Start here for File System Specific --------------

# Mandatory Field if <b>rule_loader_type</b> is FILE_SYSTEM
# Supported File Systems are Windows, Unix
FILE_SYSTEM.type=Windows

# File System Props. 
# All File System prefix with --> FILE_SYSTEM.connection_props.<FILE_SYSTEM.type>
FILE_SYSTEM.connection_props.Windows.folder_name=G:\\Praecepta\\IntegrationTest
FILE_SYSTEM.connection_props.Windows.file_name=praeceptaMetaData.json

#--------- End here for File System Specific --------------

#--------- Start here for No SQL Specific --------------

# Mandatory Field if <b>rule_loader_type</b> is NO_SQL_DB
# Supported File Systems are MongoDb, Cassandra, ElasticSearch, DynamoDb
NO_SQL_DB.type=

# File System Props. 
# All File System prefix with --> FILE_SYSTEM.connection_props.<FILE_SYSTEM.type>
NO_SQL_DB.connection_props.ElasticSearch.ip=
NO_SQL_DB.connection_props.ElasticSearch.port=
NO_SQL_DB.connection_props.ElasticSearch.user_name=
NO_SQL_DB.connection_props.ElasticSearch.password=

#--------- End here for No SQL Specific --------------
````
### Sample Data Collector Property File
````
# KAFKA, AMQ,REST
data_collector_type=KAFKA

#collector type KAFKA props
KAFKA.connection_props.bootstrap.servers=localhost:9092
KAFKA.connection_props.port=9092
KAFKA.connection_props.userName=test
KAFKA.connection_props.password=test
KAFKA.connection_props.max.timeout=200
KAFKA.connection_props.key.deserializer=io.apache.kafka.common.serialization.StringDeserializer
KAFKA.connection_props.value.deserializer=io.apache.kafka.common.serialization.StringDeserializer
KAFKA.connection_props.group.id=test_praecepta
KAFKA.connection_props.kafka.receiver.topic=TEST.TOPIC
KAFKA.connection_props.max.poll.records=10
KAFKA.connection_props.auto.offset.reset=earliest
````
> [!NOTE]
> This Code is Licensed to use Open Souce Projects Only

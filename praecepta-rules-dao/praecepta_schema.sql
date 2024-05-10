-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: praecepta
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `rule_group_status_info`
--

DROP TABLE IF EXISTS `rule_group_status_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rule_group_status_info` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `RULE_GROUP_ID` varchar(255) DEFAULT NULL,
  `RULE_GROUP_NAME` varchar(255) NOT NULL,
  `RULE_SPACE_NAME` varchar(255) NOT NULL,
  `APP_NAME` varchar(255) NOT NULL,
  `CLIENT_NAME` varchar(255) NOT NULL,
  `VERSION` varchar(255) NOT NULL,
  `RESPONSE` longtext,
  `RESPONSE_METADATA` longtext,
  `RULE_GROUP_EXECUTION_STATUS` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_group_status_info`
--

LOCK TABLES `rule_group_status_info` WRITE;
/*!40000 ALTER TABLE `rule_group_status_info` DISABLE KEYS */;
INSERT INTO `rule_group_status_info` VALUES (1,NULL,'RuleGroup1','test1','xploretech','CLNT1','V1',NULL,'{\"RULE_GROUP_NAME\":\"RuleGroup1\",\"CLIENT_NAME\":\"CLNT1\",\"APP_NAME\":\"xploretech\",\"VERSION\":\"V1\",\"SPACE_NAME\":\"test1\"}',NULL,'2023-08-28 16:43:01');
/*!40000 ALTER TABLE `rule_group_status_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_groups_info`
--

DROP TABLE IF EXISTS `rule_groups_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rule_groups_info` (
  `RULE_GROUP_ID` bigint NOT NULL AUTO_INCREMENT,
  `RULE_SPACE_ID` bigint NOT NULL,
  `RULE_GROUP_NAME` varchar(255) NOT NULL,
  `RULE_SPACE_NAME` varchar(255) NOT NULL,
  `APP_NAME` varchar(255) NOT NULL,
  `CLIENT_NAME` varchar(255) NOT NULL,
  `VERSION` varchar(255) NOT NULL,
  `RULES` longtext,
  `ACTIVE` tinyint(1) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `LAST_UPDATED_BY` varchar(255) DEFAULT NULL,
  `LAST_UPDATED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`RULE_GROUP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_groups_info`
--

LOCK TABLES `rule_groups_info` WRITE;
/*!40000 ALTER TABLE `rule_groups_info` DISABLE KEYS */;
INSERT INTO `rule_groups_info` VALUES (1,1,'RuleGroup1','test1','xploretech','CLNT1','V1','\n\n\n\n{\"xploretechCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"n1Attr1\",\"conditionOperator\":\"EQUAL_CHARS\",\"conditionValueHolder\":{\"fromValue\":\"Raja\",\"toValue\":\"Raja\"},\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n1Attr2\",\"conditionOperator\":\"LESS_THAN_NUMBER\",\"conditionValueHolder\":{\"fromValue\":10,\"toValue\":20},\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n1Attr3\",\"conditionOperator\":\"NOT_EMPTY\",\"conditionValueHolder\":{\"fromValue\":\"Rao\"},\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n1Attr4\",\"conditionOperator\":\"MATCHING_COLLECTION\",\"conditionValueHolder\":{\"fromValue\":\"[2, 3,4]\",\"toValue\":\"[2, 3,4]\"},\"parameters\":{}}}}}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"n2Attr1\",\"conditionOperator\":\"GREATER_THAN_DATE\",\"conditionValueHolder\":{\"fromValue\":\"10-MAR-2021\",\"toValue\":\"09-MAY-2022\"},\"comparingFormat\":\"dd-MMM-yyyy\",\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n2Attr2\",\"conditionOperator\":\"NOT_MATCHING_COLLECTION\",\"conditionValueHolder\":{\"fromValue\":\"[2, 3,4]\",\"toValue\":\"[11, 100]\"},\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n2Attr3\",\"conditionOperator\":\"EMPTY\",\"conditionValueHolder\":{},\"parameters\":{}}}}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"n3Attr1\",\"conditionOperator\":\"EQUAL_DATE\",\"conditionValueHolder\":{\"fromValue\":\"10-17-2021\",\"toValue\":\"06-23-2022\"},\"comparingFormat\":\"MM-dd-yyyy\",\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n3Attr2\",\"conditionOperator\":\"EQUAL_NUMBER\",\"conditionValueHolder\":{\"fromValue\":10,\"toValue\":10},\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n3Attr3\",\"conditionOperator\":\"EQUAL_CHARS\",\"conditionValueHolder\":{\"fromValue\":\"XYZ\",\"toValue\":\"XYZ\"},\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n3Attr4\",\"conditionOperator\":\"NOT_EQUAL_NUMBER\",\"conditionValueHolder\":{\"fromValue\":5,\"toValue\":1},\"parameters\":{}}}}}}}}},\"orderNumber\":0,\"ruleName\":\"3239c642-4bc6-4ba8-bf8d-647b634d5978\",\"actionToPerform\": [         {           \"actionStrategy\": \"ADD_TO_PAYLOAD\",           \"actionAttributeName\": \"attribute1\",           \"valueToAssign\": \"20\",           \"sourceValueAttributeName\": \"attribute2\"         }       ],       \"actionToPerformOnFailure\": [         {           \"actionStrategy\": \"ADD_TO_PAYLOAD\",           \"actionAttributeName\": \"attribute_new\",           \"valueToAssign\": \"60\"         }       ]}],\"actionToPerform\": [         {           \"actionStrategy\": \"SEND_MESSAGE_TO_KAFKA\",   \"actionParameters\":[{\"attributeName\":\"bootstrap.servers\",\"attributeValue\":\"localhost:9092\"},{\"attributeName\":\"port\",\"attributeValue\":\"9092\"},{\"attributeName\":\"max.timeout\",\"attributeValue\":\"200\"},{\"attributeName\":\"userName\",\"attributeValue\":\"test\"},{\"attributeName\":\"password\",\"attributeValue\":\"test\"},{\"attributeName\":\"key.serializer\",\"attributeValue\":\"org.apache.kafka.common.serialization.StringSerializer\"},{\"attributeName\":\"value.serializer\",\"attributeValue\":\"org.apache.kafka.common.serialization.StringSerializer\"},{\"attributeName\":\"kafka.sender.topic\",\"attributeValue\":\"TEST.TOPIC2\"}]         },{           \"actionStrategy\": \"SEND_MESSAGE_TO_DB\",   \"actionParameters\": [{\"attributeName\":\"db.driver\",\"attributeValue\":\"com.mysql.jdbc.Driver\"},{\"attributeName\":\"db.url\",\"attributeValue\":\"jdbc:mysql://127.0.0.1:3306/praecepta?autoReconnect=true\"},{\"attributeName\":\"connection.min.pool.size\",\"attributeValue\":\"1\"},{\"attributeName\":\"userName\",\"attributeValue\":\"root\"},{\"attributeName\":\"password\",\"attributeValue\":\"root\"},{\"attributeName\":\"connection.max.pool.size\",\"attributeValue\":\"1\"}],\"dbMetadata\":{\"insert.query\":\"INSERT INTO `praecepta`.`rule_group_status_info` ( `RULE_GROUP_ID`, `RULE_GROUP_NAME`, `RULE_SPACE_NAME`, `APP_NAME`, `CLIENT_NAME`, `VERSION`, `RESPONSE`, `RESPONSE_METADATA`, `RULE_GROUP_EXECUTION_STATUS`, `CREATED_DATE`) VALUES (?, ?, ?, ?,?,?, ?, ?, ?, NOW())\"}        }       ]}',1,'XPLORE_TECH_USER','2023-06-13 20:50:37','XPLORE_TECH_USER','2023-06-13 20:50:37'),(2,1,'RuleGroup2','test1','xploretech','CLNT1','V1','{\"xploretechCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"n1Attr1\",\"conditionOperator\":\"EQUAL_CHARS\",\"conditionValueHolder\":{\"fromValue\":\"Raja\",\"toValue\":\"Raja\"},\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n1Attr2\",\"conditionOperator\":\"LESS_THAN_NUMBER\",\"conditionValueHolder\":{\"fromValue\":10,\"toValue\":20},\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n1Attr3\",\"conditionOperator\":\"NOT_EMPTY\",\"conditionValueHolder\":{\"fromValue\":\"Rao\"},\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n1Attr4\",\"conditionOperator\":\"MATCHING_COLLECTION\",\"conditionValueHolder\":{\"fromValue\":\"[2, 3,4]\",\"toValue\":\"[2, 3,4]\"},\"parameters\":{}}}}}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"n2Attr1\",\"conditionOperator\":\"GREATER_THAN_DATE\",\"conditionValueHolder\":{\"fromValue\":\"10-MAR-2021\",\"toValue\":\"09-MAY-2022\"},\"comparingFormat\":\"dd-MMM-yyyy\",\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n2Attr2\",\"conditionOperator\":\"NOT_MATCHING_COLLECTION\",\"conditionValueHolder\":{\"fromValue\":\"[2, 3,4]\",\"toValue\":\"[11, 100]\"},\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n2Attr3\",\"conditionOperator\":\"EMPTY\",\"conditionValueHolder\":{},\"parameters\":{}}}}},\"nextConditionJoinOperator\":\"OR\",\"nextMultiCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"n3Attr1\",\"conditionOperator\":\"EQUAL_DATE\",\"conditionValueHolder\":{\"fromValue\":\"10-17-2021\",\"toValue\":\"06-23-2022\"},\"comparingFormat\":\"MM-dd-yyyy\",\"parameters\":{}},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n3Attr2\",\"conditionOperator\":\"EQUAL_NUMBER\",\"conditionValueHolder\":{\"fromValue\":10,\"toValue\":10},\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n3Attr3\",\"conditionOperator\":\"EQUAL_CHARS\",\"conditionValueHolder\":{\"fromValue\":\"XYZ\",\"toValue\":\"XYZ\"},\"parameters\":{}},\"nextConditionJoinOperator\":\"OR\",\"nextCondition\":{\"condition\":{\"subjectName\":\"n3Attr4\",\"conditionOperator\":\"NOT_EQUAL_NUMBER\",\"conditionValueHolder\":{\"fromValue\":5,\"toValue\":1},\"parameters\":{}}}}}}}}},\"orderNumber\":0,\"ruleName\":\"3239c642-4bc6-4ba8-bf8d-647b634d5978\",\"actionToPerform\": [         {           \"actionStrategy\": \"ADD_TO_PAYLOAD\",           \"actionAttributeName\": \"attribute1\",           \"valueToAssign\": \"20\",           \"sourceValueAttributeName\": \"attribute2\"         }       ],       \"actionToPerformOnFailure\": [         {           \"actionStrategy\": \"ADD_TO_PAYLOAD\",           \"actionAttributeName\": \"attribute_new\",           \"valueToAssign\": \"60\"         }       ]}],\"actionToPerform\": [         {           \"actionStrategy\": \"SEND_MESSAGE_TO_KAFKA\",   \"actionParameters\":[{\"attributeName\":\"bootstrap.servers\",\"attributeValue\":\"localhost:9092\"},{\"attributeName\":\"port\",\"attributeValue\":\"9092\"},{\"attributeName\":\"max.timeout\",\"attributeValue\":\"200\"},{\"attributeName\":\"userName\",\"attributeValue\":\"test\"},{\"attributeName\":\"password\",\"attributeValue\":\"test\"}]         }       ],       \"actionToPerformOnFailure\": [{\"actionStrategy\": \"ADD_TO_PAYLOAD\",\"actionAttributeName\": \"attribute_new1\",      \"valueToAssign\": \"6\"}]}',1,'XPLORE_TECH_USER','2023-06-13 20:50:37','XPLORE_TECH_USER','2023-06-13 20:50:37'),(5,1,'tesRuleGroup','test1','xploretech','CLNT1','V1','{\"ruleGroupName\":\"tesRuleGroup\",\"active\":true,\"xploretechCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"emp.name\",\"conditionOperator\":\"EQUAL_CHARS\",\"valueToCompare\":\"vara\"},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"condition\":{\"subjectName\":\"emp.sql\",\"conditionOperator\":\"GREATER_THAN_EQUAL_NUMBER\",\"valueToCompare\":123456.78}}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"emp.email\",\"conditionOperator\":\"EQUAL_CHARS\",\"valueToCompare\":\"varam.kotapati@gmail.com\"},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"condition\":{\"subjectName\":\"emp.dob\",\"conditionOperator\":\"EQUAL_DATE\",\"valueToCompare\":\"2020-10-19\",\"parameters\":{\"fromDateFormat\":\"yyyy-MM-dd\"}}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"attribute1\",\"valueToAssign\":\"10\",\"sourceValueAttributeName\":\"attribute2\",\"actionRegistered\":false}],\"actionToPerformOnFailure\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"attribute_new\",\"valueToAssign\":\"50\",\"actionRegistered\":false}],\"orderNumber\":0,\"ruleName\":\"ruleName1\"}],\"ruleSpaceKey\":{\"spaceName\":\"test1\",\"clientId\":\"CLNT1\",\"appName\":\"xploretech\",\"version\":\"V1\"},\"spaceName\":\"test1\",\"clientName\":\"CLNT1\",\"appName\":\"xploretech\"}',1,'XPLORE_TECH_USER','2023-09-14 16:03:33','XPLORE_TECH_USER','2023-09-14 16:03:33'),(6,1,'tesRuleGroup2','test1','xploretech','CLNT1','V1','{\"ruleGroupName\":\"tesRuleGroup2\",\"active\":true,\"xploretechCriterias\":[{\"predicates\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"emp.name\",\"conditionOperator\":\"EQUAL_CHARS\",\"valueToCompare\":\"vara\"},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"condition\":{\"subjectName\":\"emp.sql\",\"conditionOperator\":\"GREATER_THAN_EQUAL_NUMBER\",\"valueToCompare\":123456.78}}},\"nextConditionJoinOperator\":\"AND\",\"nextMultiCondition\":{\"multiCondition\":{\"condition\":{\"subjectName\":\"emp.email\",\"conditionOperator\":\"EQUAL_CHARS\",\"valueToCompare\":\"varam.kotapati@gmail.com\"},\"nextConditionJoinOperator\":\"AND\",\"nextCondition\":{\"condition\":{\"subjectName\":\"emp.dob\",\"conditionOperator\":\"EQUAL_DATE\",\"valueToCompare\":\"2020-10-19\",\"parameters\":{\"fromDateFormat\":\"yyyy-MM-dd\"}}}}}},\"actionToPerform\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"attribute1\",\"valueToAssign\":\"10\",\"sourceValueAttributeName\":\"attribute2\",\"actionRegistered\":false}],\"actionToPerformOnFailure\":[{\"actionStrategy\":\"ADD_TO_PAYLOAD\",\"actionAttributeName\":\"attribute_new\",\"valueToAssign\":\"50\",\"actionRegistered\":false}],\"orderNumber\":0,\"ruleName\":\"ruleName1\"}],\"ruleSpaceKey\":{\"spaceName\":\"test1\",\"clientId\":\"CLNT1\",\"appName\":\"xploretech\",\"version\":\"V1\"},\"spaceName\":\"test1\",\"clientName\":\"CLNT1\",\"appName\":\"xploretech\"}',1,'XPLORE_TECH_USER','2023-09-20 21:28:31','XPLORE_TECH_USER','2023-09-20 21:28:31');
/*!40000 ALTER TABLE `rule_groups_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_side_cars_info`
--

DROP TABLE IF EXISTS `rule_side_cars_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rule_side_cars_info` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `RULE_GROUP_NAME` varchar(255) NOT NULL,
  `RULE_SPACE_NAME` varchar(255) NOT NULL,
  `APP_NAME` varchar(255) NOT NULL,
  `CLIENT_NAME` varchar(255) NOT NULL,
  `VERSION` varchar(255) NOT NULL,
  `METADATA` longtext,
  `ACTIVE` tinyint(1) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `LAST_UPDATED_BY` varchar(255) DEFAULT NULL,
  `LAST_UPDATED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_side_cars_info`
--

LOCK TABLES `rule_side_cars_info` WRITE;
/*!40000 ALTER TABLE `rule_side_cars_info` DISABLE KEYS */;
INSERT INTO `rule_side_cars_info` VALUES (1,'RuleGroup1','test1','xploretech','CLNT1','V1','{\"ruleGrpName\":\"RuleGroup1\",\"preRuleGrpSideCars\":[{\"order\":1,\"type\":\"ENRICHER\",\"sideCarType\":\"SIMPLE_REST_API\",\"sideCarConfigs\":{\"clientType\":\"REST\",\"requestType\":\"GET\",\"endPointURL\":\"http://localhost:4567/myTestController/testGet/{userName}/{age}\",\"pathParams\":[{\"paramName\":\"userName\",\"paramValue\":\"test.userName\"},{\"paramName\":\"age\",\"paramValue\":\"test.age\"}]}}]}',1,'XPLORE_TECH_USER','2023-07-06 21:32:44','XPLORE_TECH_USER','2023-07-06 21:32:44'),(2,'RuleGroup2','test1','xploretech','CLNT1','V1','{\"ruleGrpName\":\"RuleGroup2\",\"preRuleGrpSideCars\":[{\"order\":1,\"sideCarType\":\"ENRICHER\",\"type\":\"SIMPLE_REST\",\"sideCarConfigs\":{\"clientType\":\"REST\",\"requestType\":\"GET\",\"endPointURL\":\"http://localhost:4567/myTestController/testGet/{userName}/{age}\"}}]}',1,'XPLORE_TECH_USER','2023-07-06 21:44:40','XPLORE_TECH_USER','2023-07-06 21:44:40');
/*!40000 ALTER TABLE `rule_side_cars_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_space_info`
--

DROP TABLE IF EXISTS `rule_space_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rule_space_info` (
  `RULE_SPACE_ID` bigint NOT NULL AUTO_INCREMENT,
  `RULE_SPACE_NAME` varchar(255) NOT NULL,
  `APP_NAME` varchar(255) NOT NULL,
  `CLIENT_ID` varchar(255) NOT NULL,
  `VERSION` varchar(255) NOT NULL,
  `ACTIVE` tinyint(1) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `LAST_UPDATED_BY` varchar(255) DEFAULT NULL,
  `LAST_UPDATED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`RULE_SPACE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_space_info`
--

LOCK TABLES `rule_space_info` WRITE;
/*!40000 ALTER TABLE `rule_space_info` DISABLE KEYS */;
INSERT INTO `rule_space_info` VALUES (1,'test1','xploretech','CLNT1','V1',1,'XPLORE_TECH_USER','2023-06-13 20:50:37','XPLORE_TECH_USER','2023-06-13 20:50:37');
/*!40000 ALTER TABLE `rule_space_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-02  8:09:37

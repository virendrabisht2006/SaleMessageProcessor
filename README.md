# SaleMessageProcessor

This is an simple sale message recorder Application which process input sale message and record data in-memory.
This SaleMessageProcessor application built up using spring boot and provide a LIST of API which will are as follow. I have tried use maximum of Java8 and TDD.

#Requirement:

1- All sales must be recorded, store in in-memory for each product.
2- All messages must be processed, each processed record print in console as output
3- After every 10th message received your application should log a report detailing the number of sales of each product and their total value.
Having a message counter, after every 10th message print the stats of the sale.
4- After 50 messages your application should log that it is pausing, stop accepting new messages and log a report of the adjustments
that have been made to each sale type while the application was running. Once 50th message received its print all adjustment for all product
and throws a exception saying that application is going for pause and can not accept new request.

Build the source code using maven command: maven> mvn clean install

Start "Application.java"

#Assumption
The sale data will be loaded first before starting for processing.  I have kept "simple-sale.csv" comma separated file containing the sale data.
You might be interested in working with more complex data. You can edit "simple-sale.csv" file with extra data.
And also simple sale data will be ", " i.e. comma separated. like below. The 1st line is header for file.

ProductType,Quantity, Value, AdjustmentOperation,AdjustmentValue
Apple,100,20, , ,
Apple,120,22,SUBTRACT,2

You can store the data from this file, by calling /load rest end point.

#API Exposed:
Method= GET, URL:http://localhost:8080/health
Method= GET, URL:http://localhost:8080/metrics
Description: This will help to get the complete health of system example: status, memory.

Method= GET, URL:/v1/rest/sales
Description: The API get all sales record processed by application at this moment.

Method= GET, URL: /v1/rest/load
Description: This might be the first operation you would like to do. Based on sample data it loads the sales data.
For your testing you might be interested for more complex data, you can edit sample-sales.csv file available in classpath and call load API.
You can check the loaded sales data with /v1/rest/sales API.

Also perform After every 10th message received your application should log a report detailing the number of sales of each product and their total value.
After 50 messages your application should log that it is pausing, stop accepting new messages and log a report of the adjustments
that have been made to each sale type while the application was running.

Method= POST, URL: /v1/rest/recordMessage
Description: This API will store/add sales message in system. This is POST request, you can make request through POSTMAN or REST template.
You can calling this API with below Json as example.

Input Request body without Adjustment:

{
    "productType": "type1",
    "quantity": 100,
    "value": 10,
    "adjustment":null
}

Input Request body with Adjustment:
{
    "productType": "type1",
    "quantity": 100,
    "value": 10,
    "adjustment":{
        "operation":"ADD",
        "value": 2.0
        }
}

Adjustment Operation can be: ADD, SUBTRACT, MULTIPLY, DIVIDE

Also perform After every 10th message received your application should log a report detailing the number of sales of each product and their total value.
After 50 messages your application should log that it is pausing, stop accepting new messages and log a report of the adjustments
that have been made to each sale type while the application was running.

Method= GET, URL:/v1/rest/messageMetric
Description: I have taken two counter messageCounter and pauseCounter. messageCounter to keep the track of incoming message for print stats after every 10th message.
and pauseCounter to will keep track of all incoming messafe, when counter reach to 50th message application will pause and could not accept new
request will through SaleException.

#Library used

1- Spring boot parent -- To enable spring boot application
2- Spring starter actuator-- for health check and metric
3- lombok -- for builder, to create immutable objects
4- assertj -- for assertion
5- log4j -- for logging
6- swagger-annotation -- For API Documentation |



package com.sale.message.processor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sale.message.processor.Compress;
import com.sale.message.processor.calc.Calculator;
import com.sale.message.processor.exception.SaleException;
import com.sale.message.processor.model.Adjustment;
import com.sale.message.processor.model.Message;
import com.sale.message.processor.model.MessageMetric;
import com.sale.message.processor.util.DataLoader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import javax.websocket.server.PathParam;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/v1/rest")
@Api(value = "/v1/rest", description = "The api process and record the sale message of different type.")
public class MessageController {

    private final static Logger logger = Logger.getLogger(MessageController.class);

    private static final String FILE_INSTRUCTION = "sample-sale.csv";
    private static long messageCounter = 0;
    private static long pauseCounter = 0;

    @Autowired
    private ObjectMapper objectMapper;

    private String response = null;
    private Map<String, List<Message>> salesData = new HashMap<>();

    @RequestMapping(value = "/recordMessage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "/v1/rest/recordMessage", notes = "APi will process and record sale message",
            responseContainer = "Return HTTP 200 code on success message processed.")
    public ResponseEntity<?> recordMessage(@RequestBody Message message, UriComponentsBuilder ucBuilder) throws SaleException {
        logger.info("About to record the incoming sale message : " + message);

        recordSale(message);

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }

    private void recordSale(Message message) throws SaleException {
        if (salesData.containsKey(message.getProductType())) {
            salesData.get(message.getProductType()).add(message);
        } else {
            List<Message> messageForProductTYpe = new ArrayList<>();
            messageForProductTYpe.add(message);
            salesData.put(message.getProductType(), messageForProductTYpe);
        }

        logger.info("Sale recorded for productType: " + message.getProductType());

        messageCounter = messageCounter + 1;
        pauseCounter = pauseCounter + 1;

        if (messageCounter == 10) {
            recordEveryTenthMessage();
            //reset to 0, so that again next 10 message can be recorded
            logger.info("About to reset messageCounter.");
            messageCounter = 0;
        }

        if (pauseCounter == 50) {
            logger.info("Pause counter reached to its limit 50, Application will not process new request");
            recordAdjustmentSaleAfterFiftyMessage();
        }
    }

    @RequestMapping(value = "/load", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/v1/rest/load", notes = "Load data from file and store into memory, this might first operation to start",
            responseContainer = "Return HTTP 200 code on success and URI to query all recorded sales")
    public ResponseEntity<?> loadStocks(UriComponentsBuilder ucBuilder) throws SaleException {
        logger.info("About to loadSales stocks from  file");
        List<Message> messages = DataLoader.loadSales(FILE_INSTRUCTION);

        messages.stream().forEach(message -> recordSale(message));

        response = "Sales date from file recorded successfully to check call API 'v1/rest/sales' ";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/v1/rest/sales").buildAndExpand().toUri());
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/sales", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/v1/rest/sales", notes = "The API get all sales record",
            responseContainer = "Return HTTP 200 code on success return all recorded sales")
    public ResponseEntity<?> getAllSale(UriComponentsBuilder ucBuilder) throws JsonProcessingException {
        response = objectMapper.writeValueAsString(salesData);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/messageMetric", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/v1/rest/messageMetric", notes = "API will provide the statics of request for how many message received and " +
            "when to application will pause",
            responseContainer = "Return HTTP 200 code on successful metric.")
    public ResponseEntity<?> getMessageMetric() {
        logger.info("About to get message metric.");
        ResponseEntity responseEntity = null;
        try {
            MessageMetric messageMetric = new MessageMetric(messageCounter, pauseCounter);
            responseEntity = new ResponseEntity<String>(objectMapper.writeValueAsString(messageMetric), HttpStatus.OK);
        } catch (JsonProcessingException jpe) {
            logger.error("Error in parsing object messageMetric: {} ", jpe);
            responseEntity = new ResponseEntity(jpe, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/testQueryParam", method = RequestMethod.GET)
    @ResponseBody
    @Compress
    public ResponseEntity testQueryParam(@PathParam("firstName") String firstName, @PathParam("middleName") String middleName) {
        logger.info("Test Query param method called time: {}" + new Date());
        response = "Hello " + firstName + " " + middleName;
        HttpHeaders headers = new HttpHeaders();
        //headers.add(HttpHeaders.EXPIRES, String.valueOf(100));
        // headers.add("Cache-Control: max-age", String.valueOf(2592000));
        // headers.add(HttpHeaders.CONTENT_ENCODING, "gzip");
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }

    private void recordAdjustmentSaleAfterFiftyMessage() throws SaleException {
        logger.info("Application is pausing and can not except new request");

        Map<String, List<Message>> productTypeWithAdjustmentSales = salesData.entrySet().stream().
                collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> entry.getValue().stream().filter(m -> m.getAdjustment() != null).collect(Collectors.toList())
                        )
                );

        logger.info("Below are the Adjustments made  in sale during application running");
        productTypeWithAdjustmentSales.entrySet().stream()
                .flatMap(entry ->
                        entry.getValue().stream()).forEach(
                        m -> recordAdjustmentForProduct(m)
                );
        throw new SaleException("Application is pausing and can not except new request");
    }

    private void recordEveryTenthMessage() {
        logger.info("MessageCounter reached to limit 10, about to record statistics for product sale.");

        Map<String, Integer> productTypeToNumberOfSale = salesData.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey(),
                        entry -> entry.getValue().size()));

        Map<String, Double> productTypeToTotalValue = Calculator.calculateTotalValueForEachProductType(salesData);

        productTypeToTotalValue.entrySet().stream().forEach(entry ->
                logger.info("Product Type: " + entry.getKey() + " No of Sale: " + productTypeToNumberOfSale.get(entry.getKey()) +
                        ", Total Vale: " + entry.getValue()));

    }

    private void recordAdjustmentForProduct(Message message) {
        Adjustment adjustment = message.getAdjustment();
        logger.info("Operation: " + adjustment.getOperation().getOpDescription() + " Adjusted Value: " + adjustment.getValue()
                + " made for Product Type: " + message.getProductType());
    }

}

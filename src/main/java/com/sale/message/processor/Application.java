package com.sale.message.processor;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    private final static Logger logger = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        logger.debug("About to start application.");
        SpringApplication.run(Application.class, args);
    }
}

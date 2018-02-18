package com.sale.message.processor.exception;


public class SaleException extends RuntimeException {

    public SaleException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public SaleException(String exceptionMessage, Exception e) {
        super(exceptionMessage, e);
    }
}

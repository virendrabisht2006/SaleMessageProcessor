package com.sale.message.processor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class MessageExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SaleException.class)
    public ResponseEntity<?> handleSaleException(Exception exception, WebRequest request) {
        return new ResponseEntity<Object>(exception, HttpStatus.SERVICE_UNAVAILABLE);
    }
}

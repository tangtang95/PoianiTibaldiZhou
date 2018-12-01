package com.poianitibaldizhou.trackme.individualrequestservice.advice;

import com.poianitibaldizhou.trackme.individualrequestservice.exception.BadResponseTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Advice for managing error about wrong response types
 */
@ControllerAdvice
public class BadResponseTypeAdvice {

    /**
     * An advice signaled into the body of the response that activates
     * only when the exception BadResponseTypeException is thrown.
     * The issue is an HTTP 400.
     * The body of the advice contains the message of the exception
     *
     * @param e error that triggers the advice
     * @return http 400 that contains the message of the exception
     */
    @ResponseBody
    @ExceptionHandler(BadResponseTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badResponseTypeHandler(BadResponseTypeException e) {
        return e.getMessage();
    }
}

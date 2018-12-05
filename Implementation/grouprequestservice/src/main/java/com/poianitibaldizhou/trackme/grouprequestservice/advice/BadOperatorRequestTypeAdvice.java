package com.poianitibaldizhou.trackme.grouprequestservice.advice;

import com.poianitibaldizhou.trackme.grouprequestservice.exception.BadOperatorRequestTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Advice for managing error about the matching of aggregator operators with request types
 */
@ControllerAdvice
public class BadOperatorRequestTypeAdvice {

    /**
     * An advice signaled into the body of the response that activates
     * only when the exception BadOperatorRequestTypeException is thrown.
     * The issue is an HTTP 400.
     * The body of the advice contains the message of the exception
     *
     * @param e error that triggers the advice
     * @return http 400 that contains the message of the exception
     */
    @ResponseBody
    @ExceptionHandler(BadOperatorRequestTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badOperatorRequestTypeHandler(BadOperatorRequestTypeException e) {
        return e.getMessage();
    }
}

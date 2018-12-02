package com.poianitibaldizhou.trackme.individualrequestservice.advice;

import com.poianitibaldizhou.trackme.individualrequestservice.exception.RequestNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Advice for managing error about not found request
 */
@ControllerAdvice
public class RequestNotFoundAdvice {

    /**
     * An advice signaled into the body of the response that activates
     * only when the exception RequestNotFoundException is thrown.
     * The issue is an HTTP 404.
     * The body of the advice contains the message of the exception
     *
     * @param e error that triggers the advice
     * @return http 404 that contains the message of the exception
     */
    @ResponseBody
    @ExceptionHandler(RequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String requestNotFoundHandler(RequestNotFoundException e) {
        return e.getMessage();
    }
}




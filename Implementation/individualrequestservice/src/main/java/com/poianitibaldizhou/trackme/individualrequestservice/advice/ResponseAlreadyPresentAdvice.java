package com.poianitibaldizhou.trackme.individualrequestservice.advice;

import com.poianitibaldizhou.trackme.individualrequestservice.exception.ResponseAlreadyPresentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Advice for managing error about response already given to a certain individual request
 */
@ControllerAdvice
public class ResponseAlreadyPresentAdvice {

    /**
     * An advice signaled into the body of the response that activates
     * only when the exception ResponseAlreadyPresentException is thrown.
     * The issue is an HTTP 400.
     * The body of the advice contains the message of the exception
     *
     * @param e error that triggers the advice
     * @return http 400 that contains the message of the exception
     */
    @ResponseBody
    @ExceptionHandler(ResponseAlreadyPresentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String responseAlreadyPresentHandler(ResponseAlreadyPresentException e) {
        return e.getMessage();
    }

}

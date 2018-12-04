package com.poianitibaldizhou.trackme.grouprequestservice.advice;

import com.poianitibaldizhou.trackme.grouprequestservice.exception.GroupRequestNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Advice for managing error about not found group request
 */
@ControllerAdvice
public class GroupRequestNotFoundAdvice {

    /**
     * An advice signaled into the body of the response that activates
     * only when the exception GroupRequestNotFoundException is thrown.
     * The issue is an HTTP 404.
     * The body of the advice contains the message of the exception
     *
     * @param e error that triggers the advice
     * @return http 404 that contains the message of the exception
     */
    @ResponseBody
    @ExceptionHandler(GroupRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String requestNotFoundHandler(GroupRequestNotFoundException e) {
        return e.getMessage();
    }

}

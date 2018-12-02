package com.poianitibaldizhou.trackme.sharedataservice.advice;

import com.poianitibaldizhou.trackme.sharedataservice.exception.GroupRequestNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Advice to manage errors about requests not found
 */
@ControllerAdvice
public class GroupRequestNotFoundAdvice {

    /**
     * An advice signaled into the body of the response that activates
     * only when the exception GroupRequestNotFoundException is thrown.
     * The issue is an HTTP 404.
     * The body of the advice contains the message of the exception
     *
     * @param ex the error which triggers the advice
     * @return an http 404 response that contains the message of the exception
     */
    @ExceptionHandler(GroupRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody String groupRequestNotFoundHandler(GroupRequestNotFoundException ex){
        return ex.getMessage();
    }

}

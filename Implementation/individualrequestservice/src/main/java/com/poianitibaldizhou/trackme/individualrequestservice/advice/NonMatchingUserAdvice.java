package com.poianitibaldizhou.trackme.individualrequestservice.advice;

import com.poianitibaldizhou.trackme.individualrequestservice.exception.NonMatchingUserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NonMatchingUserAdvice {

    @ResponseBody
    @ExceptionHandler(NonMatchingUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String nonMatchingUserHandler(NonMatchingUserException e) {
        return e.getMessage();
    }

}


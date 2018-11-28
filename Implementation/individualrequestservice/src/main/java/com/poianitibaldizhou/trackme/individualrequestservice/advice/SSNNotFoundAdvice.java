package com.poianitibaldizhou.trackme.individualrequestservice.advice;

import com.poianitibaldizhou.trackme.individualrequestservice.exception.SSNNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SSNNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(SSNNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String ssnNotFoundHandler(SSNNotFoundException e) {
        return e.getMessage();
    }
}

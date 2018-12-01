package com.poianitibaldizhou.trackme.individualrequestservice.advice;

import com.poianitibaldizhou.trackme.individualrequestservice.exception.ResponseAlreadyPresentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ResponseAlreadyPresentAdvice {

    @ResponseBody
    @ExceptionHandler(ResponseAlreadyPresentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String responseAlreadyPresentHandler(ResponseAlreadyPresentException e) {
        return e.getMessage();
    }

}

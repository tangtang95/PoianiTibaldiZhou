package com.poianitibaldizhou.trackme.sharedataservice.advice;

import com.poianitibaldizhou.trackme.sharedataservice.exception.GroupRequestNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GroupRequestNotFoundAdvice {

    @ExceptionHandler(GroupRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody String groupRequestNotFoundHandler(GroupRequestNotFoundException ex){
        return ex.getMessage();
    }

}

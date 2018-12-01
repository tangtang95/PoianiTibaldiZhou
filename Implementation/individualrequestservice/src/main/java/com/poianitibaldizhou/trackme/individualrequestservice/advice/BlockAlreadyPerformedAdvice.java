package com.poianitibaldizhou.trackme.individualrequestservice.advice;

import com.poianitibaldizhou.trackme.individualrequestservice.exception.BlockAlreadyPerformedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BlockAlreadyPerformedAdvice {

    @ResponseBody
    @ExceptionHandler(BlockAlreadyPerformedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String blockAlreadyPerformedHandler(BlockAlreadyPerformedException e) {
        return e.getMessage();
    }
}



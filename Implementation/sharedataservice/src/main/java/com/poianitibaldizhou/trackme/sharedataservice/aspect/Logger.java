package com.poianitibaldizhou.trackme.sharedataservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class Logger {

    @Around("execution(public * com.poianitibaldizhou.trackme.sharedataservice.service.*.*(..))")
    public Object logShareDataServiceCall(ProceedingJoinPoint joinPoint) {
        Object retValue = null;
        log.info("Before method: " + joinPoint.getSignature().toString());
        try {
            retValue = joinPoint.proceed();
            log.info("After method: " + joinPoint.getSignature().toString());
        } catch (Throwable throwable) {
            log.info("Exception: " + joinPoint.getSignature().toString());
        }
        return retValue;
    }

}
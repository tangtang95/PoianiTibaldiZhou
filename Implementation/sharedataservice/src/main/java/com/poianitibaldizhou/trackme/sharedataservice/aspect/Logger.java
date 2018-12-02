package com.poianitibaldizhou.trackme.sharedataservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * General logger of the system
 */
@Slf4j
@Component
@Aspect
public class Logger {

    /**
     * Logs methods regarding the call of services of share data service
     *
     * @param joinPoint the join point in which the call happened
     * @return the object which the service call should return
     * @throws Throwable propagated exception from the service call
     */
    @Around("execution(public * com.poianitibaldizhou.trackme.sharedataservice.service.*.*(..))")
    public Object logShareDataServiceCall(ProceedingJoinPoint joinPoint) throws Throwable {
        Object retValue;
        log.info("Before method: " + joinPoint.getSignature().toString());
        try {
            retValue = joinPoint.proceed();
            log.info("After method: " + joinPoint.getSignature().toString());
        } catch (Throwable throwable) {
            log.info("Exception: " + joinPoint.getSignature().toString() + " caused by:" + throwable.toString());
            throw throwable;
        }
        return retValue;
    }

}
package com.flap.app.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
public class LogginingServiceTime {
    @Before("execution(*  com.flapKap.app.service..*.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
      log.info("Entering: " + joinPoint.getSignature().toShortString());
    }

    @After("execution(*  com.kazyon.dashboard.service..*.*(..))")
    public void logMethodExit(JoinPoint joinPoint) {
       log.info("Exiting: " + joinPoint.getSignature().toShortString());
    }


    @Around("execution(* com.flapKap.app.service..*.*(..)) || execution(* com.kazyon.dashboard.feign..*.*(..))")
    public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();

       log.info("Method execution " +methodName+" time: " + (endTime - startTime) + " milliseconds");


        return result;
    }
}

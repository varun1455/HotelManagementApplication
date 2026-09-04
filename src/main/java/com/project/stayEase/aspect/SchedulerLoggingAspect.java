package com.project.stayEase.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class SchedulerLoggingAspect {

    private static final Logger SCHEDULER_LOGGER = LoggerFactory.getLogger("com.project.stayEase.schedulers");

    @Around("execution(@org.springframework.scheduling.annotation.Scheduled * *(..))")
    public Object logScheduledOperation(ProceedingJoinPoint joinPoint) throws Throwable {

        String executionId = UUID.randomUUID().toString();

        MDC.put("executionId", executionId);

        long startTime = System.nanoTime();

        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();

        String methodName = joinPoint.getSignature().getName();

        try{
            Object result = joinPoint.proceed();
            long durationMs = (System.nanoTime() - startTime) / 1_000_000;

            SCHEDULER_LOGGER.info(
                    "event=SCHEDULER_SUCCESS class={} method={} durationMs={}",
                    className,
                    methodName,
                    durationMs
            );

            return result;

        }
        catch (Throwable ex){

            long durationMs = (System.nanoTime() - startTime) / 1_000_000;

            SCHEDULER_LOGGER.error(
                    "event=SCHEDULER_FAILURE class={} method={} durationMs={} errorType={}",
                    className,
                    methodName,
                    durationMs,
                    ex.getClass().getSimpleName(),
                    ex
            );

            throw ex;
        }finally {
            MDC.remove("executionId");
        }
    }

}

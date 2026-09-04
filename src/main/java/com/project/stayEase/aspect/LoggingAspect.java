package com.project.stayEase.aspect;

import com.project.stayEase.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;


@Aspect
@Component
public class LoggingAspect {

    private static final Logger BOOKING_LOGGER = LoggerFactory.getLogger("com.project.stayEase.service.booking");
    private static final Logger PRICING_LOGGER = LoggerFactory.getLogger("com.project.stayEase.service.pricing");

    @Around(
            "execution(public * com.project.stayEase.service.booking..*(..)) || " +
                    "execution(public * com.project.stayEase.service.pricing..*(..))"
    )
    public Object logBusinessOperation(ProceedingJoinPoint joinPoint) throws Throwable {

        Map<String, String> previousContext = MDC.getCopyOfContextMap();

        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();

        String methodName = joinPoint.getSignature().getName();

        Logger logger = getLogger(joinPoint);

        long startTime = System.nanoTime();

        try {
            MDC.put("className", className);
            MDC.put("methodName", methodName);
            addAuthenticatedUserToMdc();

            logger.info("event=START");
            Object result = joinPoint.proceed();

            long durationMs = (System.nanoTime() - startTime) / 1_000_000;
            logger.info("event=SUCCESS durationMs={}", durationMs);

            return result;

        } catch (Throwable ex) {
            long durationMs = (System.nanoTime() - startTime) / 1_000_000;

            logger.error("event=FAILURE durationMs={} errorType={}", durationMs, ex.getClass().getSimpleName(), ex);

            throw ex;

        } finally {
            if (previousContext == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(previousContext);
            }
        }
    }


    private Logger getLogger(ProceedingJoinPoint joinPoint){
        String packageName = joinPoint.getSignature().getDeclaringType().getPackageName();

        if(packageName.startsWith("com.project.stayEase.service.booking")){
            return BOOKING_LOGGER;
        }
        if (packageName.startsWith("com.project.stayEase.service.pricing")) {
            return PRICING_LOGGER;
        }

        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
    }

    private void addAuthenticatedUserToMdc() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user && user.getId() != null) {
            MDC.put("userId", user.getId().toString());
        }
    }
}

package com.project.stayEase.customAnnotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface ValidDateRange {

    String message() default "Invalid date range";

    String start();

    String end();

    boolean allowTodayForStart() default true;

    boolean requireEndSameORAfterStart() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

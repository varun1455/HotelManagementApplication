package com.project.stayEase.customAnnotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startField;
    private String endField;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startField = constraintAnnotation.start();
        this.endField = constraintAnnotation.end();

    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        LocalDate startDate = (LocalDate) beanWrapper.getPropertyValue(startField);
        LocalDate endDate = (LocalDate) beanWrapper.getPropertyValue(endField);
        // Let @NotNull handle null validation
        if (startDate == null || endDate == null) {
            return true;
        }

        if (startDate.isBefore(LocalDate.now())) {

            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                            "Start date must be today or a future date")
                    .addPropertyNode(startField)
                    .addConstraintViolation();

            return false;
        }

        if (endDate.isBefore(startDate)) {

            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                            "End date must be same as startDate or after start date")
                    .addPropertyNode(endField)
                    .addConstraintViolation();

            return false;
        }

        return true;


    }


}

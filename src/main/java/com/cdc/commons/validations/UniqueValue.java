package com.cdc.commons.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueValueValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueValue {

    String message() default "The value must be unique";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String columnName();

    Class<?> domainClass();

}

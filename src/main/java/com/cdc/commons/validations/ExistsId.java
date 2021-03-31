package com.cdc.commons.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ExistsIdValueValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsId {

    String message() default "The entity must exists on DB";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String columnName();

    Class<?> domainClass();

}

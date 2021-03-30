package com.cdc.commons.validations;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValueValidator implements ConstraintValidator<UniqueValue, Object> {

    private String columnName;

    private Class<?> clazz;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public void initialize(UniqueValue params) {
        this.columnName = params.columnName();
        this.clazz = params.domainClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return this.manager.createQuery("select 1 from " + clazz.getName() + " where " + columnName + " = :value")
                .setParameter("value", value)
                .getResultList()
                .isEmpty();
    }
}

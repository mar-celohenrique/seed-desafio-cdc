package com.cdc.commons.validations;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValueValidator implements ConstraintValidator<UniqueValue, Object> {

    private String columnsName;

    private Class<?> clazz;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public void initialize(UniqueValue params) {
        this.columnsName = params.columnName();
        this.clazz = params.domainClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Query query = this.manager.createQuery("select 1 from " + clazz.getName() + " where " + columnsName + " = :value");
        query.setParameter("value", value);
        return query.getResultList().isEmpty();
    }
}

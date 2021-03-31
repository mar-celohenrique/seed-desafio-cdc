package com.cdc.commons.validations;

import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ExistsIdValueValidator implements ConstraintValidator<ExistsId, Object> {

    private String columnName;

    private Class<?> clazz;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public void initialize(ExistsId constraintAnnotation) {
        this.columnName = constraintAnnotation.columnName();
        this.clazz = constraintAnnotation.domainClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        Query query = this.manager.createQuery("select 1 from " + this.clazz.getName() + " where " + this.columnName + " = :value");
        query.setParameter("value", value);

        List<?> list = query.getResultList();
        Assert.isTrue(list.size() <= 1, "The DB has more than one " + this.clazz + " that has " + this.columnName + " with the same value = " + value);

        return !list.isEmpty();
    }
}

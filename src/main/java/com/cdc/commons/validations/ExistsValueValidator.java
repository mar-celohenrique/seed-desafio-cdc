package com.cdc.commons.validations;

import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ExistsValueValidator implements ConstraintValidator<ExistsValue, Object> {

    private String fieldName;

    private Class<?> clazz;

    private boolean checkCount;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public void initialize(ExistsValue constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.clazz = constraintAnnotation.domainClass();
        this.checkCount = constraintAnnotation.checkCount();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Query query = this.manager.createQuery("select 1 from " + this.clazz.getName() + " where " + this.fieldName + " = :value");
        query.setParameter("value", value);

        List<?> list = query.getResultList();

        if (this.checkCount) {
            Assert.isTrue(list.size() <= 1, "The DB has more than one " + this.clazz + " that has " + this.fieldName + " with the same value = " + value);
        }

        return !list.isEmpty();
    }
}

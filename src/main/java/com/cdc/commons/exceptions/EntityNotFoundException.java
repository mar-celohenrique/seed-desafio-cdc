package com.cdc.commons.exceptions;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 4949432016934737120L;

    public EntityNotFoundException(String message) {
        super(message);
    }

}

package com.E3N.pix.domain.validation;

public abstract class Validator {

    private final ValidationHandler handler;

    protected Validator(final ValidationHandler handler) {
        this.handler = handler;
    }

    public abstract ValidationHandler validate();

    protected ValidationHandler validationHandler() {
        return this.handler;
    }
}

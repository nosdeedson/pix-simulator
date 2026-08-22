package com.E3N.pix.domain.validation;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler{

    private final List<Error> errors;

    private Notification(List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create(){
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final String error){
        return (Notification) new Notification(new ArrayList<>()).append(error);
    }

    @Override
    public ValidationHandler append(final String message) {
        this.errors.add(new Error(message));
        return this;
    }

    @Override
    public ValidationHandler append(ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }

    @Override
    public <T> T validate(Validation<T> aValidation) {
        return aValidation.validate();
    }
}

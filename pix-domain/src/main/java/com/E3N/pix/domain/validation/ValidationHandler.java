package com.E3N.pix.domain.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(String message);
    ValidationHandler append(ValidationHandler handler);
    <T> T validate(Validation<T> aValidation);
    List<Error> getErrors();

    default boolean hasError(){
        return getErrors() != null && !getErrors().isEmpty();
    }

    interface Validation<T>{
        T validate();
    }
}

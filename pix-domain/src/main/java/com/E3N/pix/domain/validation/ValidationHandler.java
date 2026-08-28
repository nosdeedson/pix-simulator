package com.E3N.pix.domain.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(String reason, String value, String property);

    ValidationHandler append(ValidationHandler handler);

    <T> T validate(Validation<T> aValidation);

    List<Violation> getViolations();

    default boolean hasError() {
        return getViolations() != null && !getViolations().isEmpty();
    }

    default void relateNotificationToMe(final String property, List<Violation> violations) {
        violations.forEach(it -> {
            this.getViolations().add(new Violation(it.reason(), it.value(), (property + it.property())));
        });
    }

    interface Validation<T> {
        T validate();
    }
}

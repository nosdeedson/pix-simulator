package com.E3N.pix.domain.validation;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private String type;
    private String title;
    private int status;
    private String detail;

    private final List<Violation> violations;

    private Notification(List<Violation> violations, final String type, final String title, final int status, final String detail) {
        this.violations = violations;
        this.detail = detail;
        this.status = status;
        this.title = title;
        this.type = type;
    }

    private Notification(List<Violation> violations) {
        this.violations = violations;
    }

    private static Notification create(final String type, final String title, final int status, final String detail) {
        return new Notification(new ArrayList<>(), type, title, status, detail);
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(String reason, String value, String property) {
        return (Notification) new Notification(new ArrayList<>()).append(reason, value, property);
    }

    @Override
    public ValidationHandler append(String reason, String value, String property) {
        this.violations.add(new Violation(reason, value, property));
        return this;
    }

    @Override
    public ValidationHandler append(ValidationHandler handler) {
        this.violations.addAll(handler.getViolations());
        return this;
    }

    @Override
    public List<Violation> getViolations() {
        return this.violations;
    }

    @Override
    public <T> T validate(Validation<T> aValidation) {
        return aValidation.validate();
    }
}

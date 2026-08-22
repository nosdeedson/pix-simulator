package com.E3N.pix.domain.valueObject.name;

import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;

public class NameValidator extends Validator {

    private final Name name;

    protected NameValidator(final Name name) {
        super(name.getNotification());
        this.name = name;
    }

    @Override
    public ValidationHandler validate() {
        if (this.name.getName() == null) {
            validationHandler().append("Name must not be null");
            return name.getNotification();
        }
        String nameValidated = this.name.getName().trim();
        boolean hasInvalidChar = nameValidated.matches(".*[^\\p{L}\\s].*");;
        if (hasInvalidChar) validationHandler().append("Name should not have special characters");
        if(!nameValidated.contains(" ")) validationHandler().append("Must be a full name.");
        if (nameValidated.length() < 3 || nameValidated.length() > 100) validationHandler()
                .append("Min size of name is 3, Max size is 100.");
        return name.getNotification();
    }
}

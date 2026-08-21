package com.E3N.pix.domain.valueObject.name;

import com.E3N.pix.domain.validation.Error;
import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;

public class NameValidator extends Validator {

    private Name name;

    protected NameValidator(final Name name) {
        super(name.getNotification());
        this.name = name;
    }

    @Override
    public ValidationHandler validate() {
        if (this.name.getName() == null) {
            validationHandler().append(new Error("Name must not be null"));
            return name.getNotification();
        }
        String nameValidated = this.name.getName().trim();
        boolean hasInvalidChar = nameValidated.matches(".*[^\\p{L}\\s].*");;
        if (hasInvalidChar) validationHandler().append(new Error("Name should not have special characters"));
        if(!nameValidated.contains(" ")) validationHandler().append(new Error("Must be a full name."));
        if (nameValidated.length() < 3 || nameValidated.length() > 100) validationHandler()
                .append(new Error("Min size of name is 3, Max size is 100."));
        return name.getNotification();
    }
}

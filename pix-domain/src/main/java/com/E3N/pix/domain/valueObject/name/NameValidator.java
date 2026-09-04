package com.E3N.pix.domain.valueObject.name;

import com.E3N.pix.domain.modules.owner.owner.TypePerson;
import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;

public class NameValidator extends Validator {
    private final static String PROPERTY = "Owner.name";
    private final Name name;

    protected NameValidator(final Name name) {
        super(name.getNotification());
        this.name = name;
    }

    @Override
    public ValidationHandler validate() {
        if (this.name.getName() == null) {
            validationHandler().append("Name must not be null.", name.getName(), PROPERTY);
            return name.getNotification();
        }
        String nameValidated = this.name.getName().trim();
        boolean hasInvalidChar = false;
        if (TypePerson.LEGAL_PERSON.equals(this.name.getType())) {
            hasInvalidChar = !nameValidated.matches("^(?!.*__)[\\p{L}\\s\\-_]+$");
        } else {
            hasInvalidChar = !nameValidated.matches("^[\\p{L}\\s]+$");
        }

        if (hasInvalidChar)
            validationHandler().append("Name should not have special characters.", name.getName(), PROPERTY);
        if (this.name.getType() == null) {
            validationHandler().append("TypePerson must not be null.", name.getName(), PROPERTY);
            return validationHandler();
        }
        if (!nameValidated.contains(" ") && this.name.getType().equals(TypePerson.NATURAL_PERSON))
            validationHandler().append("Must be a full name.", name.getName(), PROPERTY);
        if (nameValidated.length() < 3 || nameValidated.length() > 100) validationHandler()
                .append("Min size of name is 3, Max size is 100.", name.getName(), PROPERTY);
        return name.getNotification();
    }
}

package com.E3N.pix.domain.entryKey;

import com.E3N.pix.domain.validation.Error;
import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;

public class EntryKeyValidator extends Validator {

    private final EntryKey entryKey;

    public EntryKeyValidator(ValidationHandler handler, EntryKey entryKey) {
        super(handler);
        this.entryKey = entryKey;
    }


    @Override
    public ValidationHandler validate() {
        return null;
    }
}

package com.E3N.pix.domain.valueObject.taxIdNumber;

import com.E3N.pix.domain.modules.owner.owner.TypePerson;
import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;
import com.E3N.shared.utils.ValidateCnpj;
import com.E3N.shared.utils.ValidateCpf;

public class TaxIdNumberValidator extends Validator {
    private final static String PROPERTY = "Account.taxIdNumber";
    private final TaxIdNumber taxIdNumber;

    public TaxIdNumberValidator(TaxIdNumber taxIdNumber) {
        super(taxIdNumber.getNotification());
        this.taxIdNumber = taxIdNumber;
    }

    @Override
    public ValidationHandler validate() {
        if (this.taxIdNumber.getTypePerson() == null) {
            validationHandler().append("Type person is required.", taxIdNumber.getTaxIdNumber(), PROPERTY);
            return validationHandler();
        }
        switch (this.taxIdNumber.getTypePerson()) {
            case TypePerson.LEGAL_PERSON: {
                if (!ValidateCnpj.validate(this.taxIdNumber.getTaxIdNumber())) {
                    validationHandler().append("TaxIdNumber is invalid.", taxIdNumber.getTaxIdNumber(), PROPERTY);
                }
                return validationHandler();
            }
            case TypePerson.NATURAL_PERSON: {
                if (!ValidateCpf.validate(this.taxIdNumber.getTaxIdNumber())) {
                    validationHandler().append("TaxIdNumber is invalid.", taxIdNumber.getTaxIdNumber(), PROPERTY);
                }
                return validationHandler();
            }
            default:
                return validationHandler().append("Invalid value.", taxIdNumber.getTaxIdNumber(), PROPERTY);
        }
    }
}

package com.E3N.pix.domain.taxIdNumber;

import com.E3N.pix.domain.owner.TypePerson;
import com.E3N.shared.utils.ValidateCnpj;
import com.E3N.shared.utils.ValidateCpf;

public class TaxIdNumber {
    private final TypePerson typePerson;
    private final String taxIdNumber;

    private TaxIdNumber(TypePerson typePerson, String taxIdNumber) {
        this.typePerson = typePerson;
        this.taxIdNumber = taxIdNumber;
    }

    public static TaxIdNumber from(TypePerson typePerson, String taxIdNumber){
        return switch (typePerson){
            case LEGAL_PERSON -> validateLegalPerson(taxIdNumber);
            case NATURAL_PERSON -> validateNaturalPerson(taxIdNumber);
            default -> null;
        };
    }

    private static TaxIdNumber validateLegalPerson(final String taxIdNumber){
        var isValid = ValidateCnpj.validate(taxIdNumber);
        TaxIdNumber taxId = null;
        if (isValid) taxId = new TaxIdNumber(TypePerson.LEGAL_PERSON, taxIdNumber);
        return taxId;
    }

    private static TaxIdNumber validateNaturalPerson(final String taxIdNumber){
        TaxIdNumber taxId = null;
        var isValid = ValidateCpf.validate(taxIdNumber);
        if (isValid) taxId = new TaxIdNumber(TypePerson.NATURAL_PERSON, taxIdNumber);
        return taxId;
    }
}

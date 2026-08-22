package com.E3N.pix.domain.owner;

import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.valueObject.name.Name;
import com.E3N.pix.domain.valueObject.taxIdNumber.TaxIdNumber;

import java.time.Instant;

public class Owner extends Entity {
    private final TypePerson type;
    private final TaxIdNumber taxIdNumber; // cpf/cnpj
    private final Name name;
    private final Instant keyOwnerShipDate;
    private Instant openClaimCreateionDate;

    public Owner(
            final TypePerson type,
            final String taxIdNumber,
            final String name,
            final Instant keyOwnerShipDate
    ) {
        super();
        this.type = type;
        this.taxIdNumber = TaxIdNumber.getInstance(type, taxIdNumber);
        this.name = Name.getInstance(name);
        this.keyOwnerShipDate = keyOwnerShipDate;
    }

    @Override
    protected void validate() {

    }
}

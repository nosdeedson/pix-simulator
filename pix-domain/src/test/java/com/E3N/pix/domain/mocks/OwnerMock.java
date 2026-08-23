package com.E3N.pix.domain.mocks;

import com.E3N.pix.domain.modules.entry.owner.Owner;
import com.E3N.pix.domain.modules.entry.owner.TypePerson;

public abstract class OwnerMock {
    public static Owner builder(
            final String keyOwnershipDate,
            final String name,
            final String tradeName,
            final String taxIdNumber,
            final TypePerson typePerson,
            final String[] ...openClaimCreationDate
    ){
        if (typePerson.equals(TypePerson.LEGAL_PERSON)){
            return Owner.getInstanceLegalPerson(
                    keyOwnershipDate, name, taxIdNumber, tradeName, typePerson
            );
        } else {
            return Owner.getInstanceNaturalPerson(
                    keyOwnershipDate, name, taxIdNumber, typePerson
            );
        }
    }
}

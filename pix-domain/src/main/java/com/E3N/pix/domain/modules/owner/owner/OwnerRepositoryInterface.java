package com.E3N.pix.domain.modules.owner.owner;

import com.E3N.pix.domain.repository.CRUDRepositoryInterface;

public interface OwnerRepositoryInterface extends CRUDRepositoryInterface<Owner> {

    Owner findByTaxIdNumber(final String taxIdNumber);

    boolean cannotHaveMoreKey(final String taxIdNumber, TypePerson typePerson);

    boolean keyExist(final String taxIdNumber, final String key);
}

package com.E3N.pix.domain.modules.ownership.owner;

import com.E3N.pix.domain.repository.CRUDRepositoryInterface;

import java.util.Optional;

public interface OwnerRepositoryInterface extends CRUDRepositoryInterface<Owner> {

    Optional<Owner> findByTaxIdNumber(final String taxIdNumber);

    Optional<Owner> findByKey(final String key);

    Optional<Owner> findByKeyAndTaxIdNumber(final String key, final String taxIdNumber);

}

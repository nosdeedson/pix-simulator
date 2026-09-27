package com.E3N.pix.domain.modules.ownership.owner;

import com.E3N.pix.domain.repository.CRUDRepositoryInterface;

import java.util.List;
import java.util.Optional;

public interface OwnerRepositoryInterface extends CRUDRepositoryInterface<Owner> {

    Optional<Owner> findByTaxIdNumber(final String taxIdNumber);

    Optional<Owner> findByKey(final String key);

    Optional<Owner> findByKeyAndTaxIdNumber(final String key, final String taxIdNumber);

    Optional<Owner> findByKeyAndParticipant(final String key, final String participant);

    Optional<Owner> findByAccountNumberAndParticipantAndBranchAndTaxIdNumber(final String accountNumber, final String participant, final String branch, final String taxIdNumber);

    List<String> findByKeys(List<String> keys);
}

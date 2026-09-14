package com.E3N.pix.service.ownership;

import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.ownership.dto.OwnerDto;

public class OwnerService {

    private final OwnerRepositoryInterface ownerRepository;

    public OwnerService(OwnerRepositoryInterface ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, Owner> create(final OwnerDto dto) {
        var owner = Owner.getInstance(
                dto.name(),
                dto.tradeName(),
                dto.taxIdNumber(),
                dto.typePerson(),
                dto.account().toEntity()
        );
        if (owner.getNotification().hasError()) {
            return Either.left(owner.getNotification());
        }
        owner = this.ownerRepository.save(owner);
        return Either.right(owner);
    }

}

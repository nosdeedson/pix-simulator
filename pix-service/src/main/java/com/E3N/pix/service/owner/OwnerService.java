package com.E3N.pix.service.owner;

import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.modules.owner.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.owner.dto.OwnerDto;

public class OwnerService {

    private final OwnerRepositoryInterface ownerRepository;

    public OwnerService(OwnerRepositoryInterface ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, Owner> getOrCreate(final OwnerDto dto) {
        var owner = this.ownerRepository.findByTaxIdNumber(dto.taxIdNumber());
        if (owner == null) {
            var account = dto.account().toEntity();
            owner = Owner.getInstance(
                    dto.name(),
                    dto.tradeName(),
                    dto.taxIdNumber(),
                    dto.typePerson(),
                    account
            );
            if (owner.getNotification().hasError()) {
                return Either.left(owner.getNotification());
            }
            owner = this.ownerRepository.save(owner);
            return Either.right(owner);
        }
        return Either.right(owner);
    }

    public Owner update(final OwnerDto dto, final Owner owner) {
        return this.ownerRepository.update(owner);
    }
}

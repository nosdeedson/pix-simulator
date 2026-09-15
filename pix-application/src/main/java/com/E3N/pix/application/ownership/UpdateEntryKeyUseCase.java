package com.E3N.pix.application.ownership;

import com.E3N.pix.domain.modules.ownership.dto.UpdateEntryKeyDto;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;

public class UpdateEntryKeyUseCase {

    private final OwnerRepositoryInterface ownerRepository;

    public UpdateEntryKeyUseCase(OwnerRepositoryInterface ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, Owner> execute(UpdateEntryKeyDto dto) {
        var optionalOwner = ownerRepository.findByTaxIdNumber(dto.taxIdNumber());
        if (optionalOwner.isEmpty()) {
            Notification notification = Notification.create("Not found", 404, "EntryKey does not exist.");
            return Either.left(notification);
        }
        var owner = optionalOwner.get();
        owner.update(dto);
        if (owner.getNotification().hasError()) {
            return Either.left(owner.getNotification());
        }
        return Either.right(ownerRepository.update(owner));
    }
}

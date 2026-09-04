package com.E3N.pix.application;

import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.modules.owner.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.owner.OwnerService;
import com.E3N.pix.service.owner.dto.OwnerDto;

public class CreateEntryKeyUseCase {

    private final OwnerRepositoryInterface ownerRepository;

    public CreateEntryKeyUseCase(OwnerRepositoryInterface ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, Owner> createOrUpdateEntryKey(OwnerDto dto) {
        boolean ownerCannotHaveMoreKey = this.ownerRepository.cannotHaveMoreKey(dto.taxIdNumber(), dto.typePerson());
        if (ownerCannotHaveMoreKey) {
            var notification = Notification.create();
            notification.append("User Cannot have more keys", dto.taxIdNumber(), "Owner.Account.key");
            return Either.left(notification);
        }
        boolean keyExists = this.ownerRepository.keyExist(dto.taxIdNumber(), dto.account().entryKeyDto().key());
        if (keyExists) {
            var notification = Notification.create();
            notification.append("Key already exist, start a portability process", dto.account().entryKeyDto().key(), "Owner.key");
            return Either.left(notification);
        }
        var ownerService = new OwnerService(this.ownerRepository);
        var result = ownerService.getOrCreate(dto);
        Owner owner = null;
        if (result instanceof Either.Left<Notification, Owner>(Notification notification)) {
            return Either.left(notification);
        } else if (result instanceof Either.Right<Notification, Owner>(Owner value)) {
            owner = value;
        }
        var account = dto.account().toEntity();
        if (owner != null) owner.validateNewAccount(account);
        if (owner != null && owner.getNotification().hasError()) {
            return Either.left(owner.getNotification());
        }
        return Either.right(this.ownerRepository.update(owner));
    }
}

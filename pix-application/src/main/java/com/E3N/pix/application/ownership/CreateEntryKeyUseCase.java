package com.E3N.pix.application.ownership;

import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.ownership.OwnerService;
import com.E3N.pix.service.ownership.dto.OwnerDto;

import java.util.Optional;

public class CreateEntryKeyUseCase {

    private final OwnerRepositoryInterface ownerRepository;

    public CreateEntryKeyUseCase(OwnerRepositoryInterface ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, Owner> createOrUpdateEntryKey(OwnerDto dto) {
        var optionalOwner = this.ownerRepository.findByKey(dto.account().entryKeyDto().key());
        OwnerService ownerService = new OwnerService();
        if (optionalOwner.isPresent()) {
            var owner = optionalOwner.get();
            var exist = ownerService.validateKeyExistence(owner, dto);
            if (exist.hasError())
                return Either.left(exist);
        }
        optionalOwner = this.ownerRepository.findByTaxIdNumber(dto.taxIdNumber());
        if (optionalOwner.isEmpty()) {
            Owner owner = ownerService.create(dto);
            if (owner.getNotification().hasError()) {
                return Either.left(owner.getNotification());
            }
            ownerRepository.save(owner);
            return Either.right(owner);
        } else {
            Owner owner = optionalOwner.get();
            if (owner.canNotHaveMoreKeys()) {
                return Either.left(Notification.create("Owner is not allowed to have more keys", dto.taxIdNumber(), "Owner"));
            }
            owner.addNewAccountOrNewKey(dto.account().toEntity());
            if (owner.getNotification().hasError()) {
                return Either.left(owner.getNotification());
            }
            return Either.right(this.ownerRepository.update(owner));
        }
    }
}

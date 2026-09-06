package com.E3N.pix.application;

import com.E3N.pix.domain.modules.owner.account.Account;
import com.E3N.pix.domain.modules.owner.entryKey.EntryKey;
import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.modules.owner.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.owner.OwnerService;
import com.E3N.pix.service.owner.dto.OwnerDto;

import java.util.Optional;

public class CreateEntryKeyUseCase {

    private final OwnerRepositoryInterface ownerRepository;

    public CreateEntryKeyUseCase(OwnerRepositoryInterface ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, Owner> createOrUpdateEntryKey(OwnerDto dto) {
        var optionalOwner = this.ownerRepository.findByKey(dto.account().entryKeyDto().key());
        if (optionalOwner.isPresent()) {
            var owner = optionalOwner.get();
            if (owner.getTaxIdNumber().getTaxIdNumber().equals(dto.taxIdNumber())) {
                Optional<Account> sameParticipantAnAccountNumber = owner.getAccounts().stream()
                        .filter(it -> it.sameParticipant(dto.account().participant(), dto.account().accountNumber()))
                        .findFirst();
                if (sameParticipantAnAccountNumber.isPresent()) {
                    return Either.left(Notification.create("Key already exists.", dto.account().entryKeyDto().key(), "Owner.Key"));
                }
                return Either.left(Notification.create(
                        "Key is registered in another bank, create a portability or claims ownership",
                        dto.account().entryKeyDto().key(), "Owner.key")
                );
            }
        }
        optionalOwner = this.ownerRepository.findByTaxIdNumber(dto.taxIdNumber());
        Owner owner = null;
        if (optionalOwner.isEmpty()) {
            OwnerService ownerService = new OwnerService(ownerRepository);
            Either<Notification, Owner> result = ownerService.create(dto);
            if (result instanceof Either.Left<Notification, Owner>(Notification notification)) {
                return Either.left(notification);
            } else if (result instanceof Either.Right<Notification, Owner>(Owner newOwner)) {
                return Either.right(newOwner);
            }
        } else {
            owner = optionalOwner.get();
            if (owner.canNotHaveMoreKeys()){
                return Either.left(Notification.create("Owner is not allowed to have more keys", dto.taxIdNumber(), "Owner"));
            }
        }
        Optional<Account> optionalAccount = Optional.empty();
        if (owner != null) {
            owner.addNewAccountOrNewKey(dto.account().toEntity());
        }
        if (owner != null && owner.getNotification().hasError()){
            return Either.left(owner.getNotification());
        }
        return Either.right(this.ownerRepository.update(owner));
    }
}

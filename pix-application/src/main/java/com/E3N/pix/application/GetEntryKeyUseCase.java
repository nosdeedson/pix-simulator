package com.E3N.pix.application;

import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.modules.owner.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;

import java.util.Optional;

public class GetEntryKeyUseCase {

    private final OwnerRepositoryInterface ownerRepository;

    public GetEntryKeyUseCase(OwnerRepositoryInterface ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, Owner> getEntrykey(final String key){
        Optional<Owner> optionalOwner = this.ownerRepository.findByKey(key);
        if (optionalOwner.isPresent()){
            var owner = optionalOwner.get();
            var keys = owner.getAccounts().getFirst().getEntryKeys();
            var keyRequested = keys.stream().filter(it -> it.getKey().getKey().equals(key)).findFirst().get();
            owner.getAccounts().getFirst().getEntryKeys().removeAll(keys);
            owner.getAccounts().getFirst().getEntryKeys().add(keyRequested);
            return Either.right(owner);
        }
        return Either.left(Notification.create("https://pix.com/soap/contract", "Bad Request", 404, "Entry key does not exist"));
    }
}

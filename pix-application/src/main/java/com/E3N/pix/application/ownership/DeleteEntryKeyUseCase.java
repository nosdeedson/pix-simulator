package com.E3N.pix.application.ownership;

import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class DeleteEntryKeyUseCase {

    private final OwnerRepositoryInterface ownerRepository;

    public DeleteEntryKeyUseCase(OwnerRepositoryInterface ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, Void> deleteEntryKey(final String key, final String participant, Reason reason) {
        var optionalOwner = this.ownerRepository.findByKeyAndParticipant(key, participant);
        if (optionalOwner.isEmpty()) {
            Notification notification = Notification.create("Not found", 404, "EntryKey not found");
            return Either.left(notification);
        }
        var owner = optionalOwner.get();
        AtomicInteger qtdKeyDelete = new AtomicInteger();
        for (Account acc : owner.getAccounts()) {
            for (EntryKey k : acc.getEntryKeys()) {
                if (k.getKey().getKey().equals(key)) {
                    k.delete(reason);
                }
                qtdKeyDelete.getAndIncrement();
            }
            if (qtdKeyDelete.get() == owner.getAccounts().size()) {
                acc.delete();
            }
        }
        ownerRepository.update(owner);
        return Either.right(null);
    }
}

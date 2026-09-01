package com.E3N.pix.service.entryKey;

import com.E3N.pix.domain.modules.entry.entryKey.EntryKey;
import com.E3N.pix.domain.modules.entry.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.entryKey.dto.EntryKeyDto;

public class EntryKeyService {

    private final OwnerRepositoryInterface entryKeyRepository;

    public EntryKeyService(OwnerRepositoryInterface entryKeyRepository) {
        this.entryKeyRepository = entryKeyRepository;
    }

    public Either<Notification, EntryKey> createEntryKey(final EntryKeyDto dto) {
        var account = dto.accountDto().toEntity();
        var owner = dto.ownerDto().toEntity();
        var entry = EntryKey.getInstance(
                dto.key(),
                dto.typeKey(),
                account,
                owner,
                dto.reason(),
                dto.requestId()
        );
        if (entry.getNotification().hasError()) {
            return Either.left(entry.getNotification());
        }
        var entity = this.entryKeyRepository.save(entry);
        return Either.right(entity);

    }
}

package com.E3N.pix.domain.entryKey;

import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.owner.Owner;
import com.E3N.pix.domain.account.Account;
import com.E3N.pix.domain.key.TypeKey;
import com.E3N.pix.domain.key.Key;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntryKey extends Entity {
    private final Account account;
    private Instant creationDate;
    private List<Key> keys = new ArrayList<>();
    private final Owner owner;
    private final Reason reason;
    private final UUID requestId;

    public EntryKey(
                    final String key,
                    final TypeKey type,
                    final Account account,
                    final Owner owner,
                    final Reason reason,
                    final UUID requestId) {
        var today = Instant.now();
        super(UUID.randomUUID(), today, today, null);
        this.keys.add(Key.getInstance(key, type));
        this.account = account;
        this.owner = owner;
        this.reason = reason;
        this.requestId = requestId;
        this.creationDate = today;
    }
}

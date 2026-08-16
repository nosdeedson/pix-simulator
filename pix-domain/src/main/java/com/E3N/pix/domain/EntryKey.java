package com.E3N.pix.domain;

import java.time.Instant;
import java.util.UUID;

public class EntryKey extends Entity {
    private final Key key;
    private final Account account;
    private final Owner owner;
    private final String reason;
    private final String requestId;

    public EntryKey(final UUID id,
                    final Instant createdAt,
                    final Instant updatedAt,
                    final Instant deletedAt,
                    final String key,
                    final TypeKey type,
                    final Account account,
                    final Owner owner,
                    final String reason,
                    final String requestId) {
        super(id, createdAt, updatedAt, deletedAt);
        this.key = Key.getInstance(key, type);
        this.account = account;
        this.owner = owner;
        this.reason = reason;
        this.requestId = requestId;
    }
}

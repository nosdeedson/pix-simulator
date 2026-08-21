package com.E3N.pix.domain.entryKey;

import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.owner.Owner;
import com.E3N.pix.domain.account.Account;
import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.domain.valueObject.key.Key;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntryKey extends Entity {
    @Override
    protected void validate() {

    }

//    private final Account account;
//    private final Instant creationDate;
//    private final List<Key> keys = new ArrayList<>();
//    private final Owner owner;
//    private final Reason reason;
//    private final UUID requestId;

//    private EntryKey(
//                    final String key,
//                    final TypeKey type,
//                    final Account account,
//                    final Owner owner,
//                    final Reason reason,
//                    final String requestId
//    ) {
//        super();
//        this.keys.add(new Key(key, type));
//        this.account = account;
//        this.owner = owner;
//        this.creationDate = Instant.now();
//        this.reason = reason;
//        this.requestId = UUID.fromString(requestId);
//    }

//    public Account getAccount() {
//        return account;
//    }
//
//    public Instant getCreationDate() {
//        return creationDate;
//    }
//
//    public List<Key> getKeys() {
//        return keys;
//    }
//
//    public Owner getOwner() {
//        return owner;
//    }
//
//    public Reason getReason() {
//        return reason;
//    }
//
//    public UUID getRequestId() {
//        return requestId;
//    }
}

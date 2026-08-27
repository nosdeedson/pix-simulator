package com.E3N.pix.domain.modules.entry.entryKey;

import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.modules.entry.account.Account;
import com.E3N.pix.domain.modules.entry.owner.Owner;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.key.Key;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.shared.utils.ValidateUUID;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntryKey extends Entity {

    private Account account;
    private Instant creationDate;
    private Key key;
    private Owner owner;
    private Reason reason;
    private UUID requestId;

    private Notification notification;

    private EntryKey(
            final String key,
            final TypeKey type,
            final Account account,
            final Owner owner,
            final Reason reason,
            final String requestId
    ) {
        super();
        this.key = Key.getInstance(key, type);
        this.account = account;
        this.owner = owner;
        this.creationDate = Instant.now();
        this.reason = reason;
        this.requestId = ValidateUUID.isValid(requestId) ? UUID.fromString(requestId) : null;
        validate();
    }

    public static EntryKey getInstance(
            final String key,
            final TypeKey type,
            final Account account,
            final Owner owner,
            final Reason reason,
            final String requestId
    ) {
        return new EntryKey(key, type, account, owner, reason, requestId);
    }

    @Override
    protected void validate() {
        this.notification = Notification.create();
        this.notification = (Notification) new EntryKeyValidator(this).validate();
        if (this.notification.hasError()){
            this.account = null;
            this.key = null;
            this.creationDate = null;
            this.owner = null;
            this.reason = null;
            this.requestId = null;
        } else {
            this.notification = null;
        }
    }

    public Account getAccount() {
        return account;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Key getKey() {
        return key;
    }

    public Owner getOwner() {
        return owner;
    }

    public Reason getReason() {
        return reason;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public Notification getNotification() {
        return notification;
    }
}

package com.E3N.pix.domain.modules.entry.entryKey;

import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.key.Key;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.shared.utils.ValidateUUID;

import java.time.Instant;
import java.util.UUID;

public class EntryKey extends Entity {

    private Instant creationDate;
    private Key key;
    private Reason reason;
    private UUID requestId;
    private final String correlationId;
    private final Instant responseTime;
    private Instant keyOwnerShipDate;

    private Notification notification;

    private EntryKey(
            final String key,
            final TypeKey type,
            final Reason reason,
            final String requestId
    ) {
        super();
        this.key = Key.getInstance(key, type);
        this.creationDate = Instant.now();
        this.reason = reason;
        this.requestId = ValidateUUID.isValid(requestId) ? UUID.fromString(requestId) : null;
        this.correlationId = UUID.randomUUID().toString().replace("-", "");
        this.responseTime = Instant.now();
        validate();
    }

    public static EntryKey getInstance(
            final String key,
            final TypeKey type,
            final Reason reason,
            final String requestId
    ) {
        return new EntryKey(key, type, reason, requestId);
    }

    @Override
    protected void validate() {
        this.notification = Notification.create();
        this.notification = (Notification) new EntryKeyValidator(this).validate();
        if (this.notification.hasError()) {
            this.key = null;
            this.creationDate = null;
            this.reason = null;
            this.requestId = null;
        }
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Key getKey() {
        return key;
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

    public String getCorrelationId() {
        return correlationId;
    }

    public Instant getResponseTime() {
        return responseTime;
    }
}

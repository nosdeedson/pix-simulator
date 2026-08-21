package com.E3N.pix.domain;

import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.UUID;

public abstract class Entity {
    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;

    public Entity() {
        var today = Instant.now();
        this.id = UUID.randomUUID();
        this.createdAt = today;
        this.updatedAt = today;
        this.deletedAt = null;
    }

    protected abstract void validate();
}

package com.E3N.pix.domain;

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

    public Entity(
            UUID id,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    protected abstract void validate();
}
